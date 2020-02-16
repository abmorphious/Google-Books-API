package daabsoft.com.googlebooks;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    TextView result;
    APIUtil apiUtil;
    RecyclerView.LayoutManager rlayoutManager;
    RecyclerView recyclerView;
    ArrayList<BookInfo> resultBooks;
    BookDbOpenHelper bookDbOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView)findViewById(R.id.books_result);

        rlayoutManager = new LinearLayoutManager(this);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(rlayoutManager);
        bookDbOpenHelper = new BookDbOpenHelper(this);

        resultBooks = new ArrayList<BookInfo>();
        ConnectivityManager cM = (ConnectivityManager)this.getSystemService(Service.CONNECTIVITY_SERVICE);
        NetworkInfo isAvailable = cM.getActiveNetworkInfo();

        //Read preferences
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        boolean repeatChecked = sp.getBoolean("check_box_preference_2", false);
        Snackbar sb = Snackbar.make(recyclerView, repeatChecked+"",Snackbar.LENGTH_SHORT);
        sb.show();

        if(isAvailable != null)
        {
            if(!isAvailable.isConnected())
            {
                SQLiteDatabase db = bookDbOpenHelper.getReadableDatabase();
                Cursor cursor = db.query(BookDbContract.TABLE_NAME,
                        new String[] {BookDbContract.BookEntry.BOOK_TITLE
                        ,BookDbContract.BookEntry.BOOK_PUBLISHED_DATE
                                ,BookDbContract.BookEntry.BOOK_DESCRIPTION},
                        null,null,null,null,null);

                int titlePos = cursor.getColumnIndex(BookDbContract.BookEntry.BOOK_TITLE);
                int pubPos = cursor.getColumnIndex(BookDbContract.BookEntry.BOOK_PUBLISHED_DATE);
                int descPos = cursor.getColumnIndex(BookDbContract.BookEntry.BOOK_DESCRIPTION);

                while (cursor.moveToNext())
                {
                    String title = cursor.getColumnName(titlePos);
                    String pub_date = cursor.getColumnName(pubPos);
                    String desc = cursor.getColumnName(descPos);
                    BookInfo bInfo = new BookInfo(title, pub_date, desc);
                    resultBooks.add(bInfo);
                }
                cursor.close();
            }else{
                apiUtil = new APIUtil();
                URL builtUrl = apiUtil.buildUrl("Java");
                new BooksQueryTask().execute(builtUrl);
            }
        }else{
            SQLiteDatabase db = bookDbOpenHelper.getReadableDatabase();
            Cursor cursor = db.query(BookDbContract.TABLE_NAME,
                    new String[] {BookDbContract.BookEntry.BOOK_TITLE
                            ,BookDbContract.BookEntry.BOOK_PUBLISHED_DATE
                            ,BookDbContract.BookEntry.BOOK_DESCRIPTION},
                    null,null,null,null,null);

            int titlePos = cursor.getColumnIndex(BookDbContract.BookEntry.BOOK_TITLE);
            int pubPos = cursor.getColumnIndex(BookDbContract.BookEntry.BOOK_PUBLISHED_DATE);
            int descPos = cursor.getColumnIndex(BookDbContract.BookEntry.BOOK_DESCRIPTION);

            while (cursor.moveToNext())
            {
                String title = cursor.getString(titlePos);
                String pub_date = cursor.getString(pubPos);
                String desc = cursor.getString(descPos);
                BookInfo bInfo = new BookInfo(title, pub_date, desc);

                resultBooks.add(bInfo);
            }
            cursor.close();
            RecyclerView.Adapter rAdapter = new MyAdapter(MainActivity.this, resultBooks);
            recyclerView.setAdapter(rAdapter);
        }
    }

    @Override
    protected void onDestroy() {
        bookDbOpenHelper.close();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        if(menu instanceof MenuBuilder){
            ((MenuBuilder) menu).setOptionalIconsVisible(true);
        }

        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.app_bar_switch:
                Toast.makeText(this, "Switch 1", Toast.LENGTH_SHORT).show();
                break;
            case R.id.app_bar_switch2:
                Toast.makeText(this, "Switch 2", Toast.LENGTH_SHORT).show();
                break;
            case R.id.settings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        URL sUrl = new APIUtil().buildUrl(s);
        new BooksQueryTask().execute(sUrl);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String result = null;
            try{
                result = apiUtil.getJSONResult(searchUrl);
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {

            SQLiteDatabase db = bookDbOpenHelper.getWritableDatabase();
            ContentValues vals = new ContentValues();
            ArrayList<Book> books = APIUtil.getBooksFromJSON(s);
            String res = "";

            resultBooks.removeAll(resultBooks);

            for(Book b : books)
            {
                BookInfo bookInfo = new BookInfo(b.title, b.publisher, b.description);
                vals.put(BookDbContract.BookEntry.BOOK_TITLE, b.title);
                vals.put(BookDbContract.BookEntry.BOOK_PUBLISHED_DATE, b.publisher);
                vals.put(BookDbContract.BookEntry.BOOK_DESCRIPTION, b.description);
                try {
                    db.insertOrThrow(BookDbContract.TABLE_NAME, null, vals);
                }catch (SQLException e)
                {
                    Log.e("SQL_Err_Insert_Book", e.getMessage());
                }
                resultBooks.add(bookInfo);
            }
            RecyclerView.Adapter rAdapter = new MyAdapter(MainActivity.this, resultBooks);
            recyclerView.setAdapter(rAdapter);
            rAdapter.notifyDataSetChanged();

            super.onPostExecute(s);
        }
    }
}
