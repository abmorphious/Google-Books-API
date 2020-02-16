package daabsoft.com.googlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class BookDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        BookInfo bInfo = getIntent().getParcelableExtra("bookParcel");
        TextView title = (TextView)findViewById(R.id.book_p_title);
        TextView publisher = (TextView)findViewById(R.id.book_p_publisher);
        TextView desc = (TextView)findViewById(R.id.p_description);
        title.setText(bInfo.title);
        publisher.setText(bInfo.publisher);
        desc.setText(bInfo.description);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
