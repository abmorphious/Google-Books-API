package daabsoft.com.googlebooks;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;

public class APIUtil {
     String BASE_URL = "https://www.googleapis.com/books/v1/volumes";

    public APIUtil() {
    }

    public URL buildUrl(String title)
    {
        String queryParameter = "q";
        URL url = null;
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(queryParameter, title)
                //.appendQueryParameter(QueryParameterAPIKey, API_KEY)
                .build();
        try {
            url = new URL(uri.toString());
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return  url;
    }

    public String getJSONResult(URL url)
    {
        Log.i("CREATED_ON_START", "onCreate() called with: Observer ");
        HttpURLConnection con = null;
        try {
            con = (HttpURLConnection)url.openConnection();
            InputStream is = con.getInputStream();
            Scanner scanner = new Scanner(is);
            scanner.useDelimiter("\\A");
            while(scanner.hasNext())
            {
                return scanner.next();
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            con.disconnect();
        }
        return null;
    }

    public static ArrayList<Book> getBooksFromJSON(String strJSON)
    {
        String ID = "id";
        String TITLE = "title";
        String SUBTITLE = "subtitle";
        String AUTHORS = "authors";
        String PUBLISHER = "publisher";
        String PUBLISHED_DATE = "publishedDate";
        String ITEMS = "items";
        String VOLUME_INFO = "volumeInfo";
        String DESCRIPTION = "description";
        int booksNum = 0;

        ArrayList<Book> books = new ArrayList<Book>();
        try
        {
            JSONObject jsonBooks = new JSONObject(strJSON);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);

            if(arrayBooks != null) {
                booksNum = arrayBooks.length();
            }
            for(int i = 0; i<booksNum; i++)
            {
                JSONObject jsBooks = arrayBooks.getJSONObject(i);
                JSONObject jsVolume = jsBooks.getJSONObject(VOLUME_INFO);
                int authorsNum = jsVolume.getJSONArray(AUTHORS).length();
                String [] authors = new String[authorsNum];
                for(int j=0; j<authorsNum; j++)
                {
                    authors[j] = jsVolume.getJSONArray(AUTHORS).get(j).toString();
                }

                Book book = new Book(
                        jsBooks.getString(ID),
                        jsVolume.getString(TITLE),
                        jsVolume.isNull(SUBTITLE) ? "" : jsVolume.getString(SUBTITLE),
                        authors,
                        jsVolume.getString(PUBLISHER),
                        jsVolume.getString(PUBLISHED_DATE),
                        jsVolume.getString(DESCRIPTION)
                );

                books.add(book);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }
}
