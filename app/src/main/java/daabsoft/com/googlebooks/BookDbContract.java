package daabsoft.com.googlebooks;

import android.provider.BaseColumns;

public class BookDbContract {
    public static final String TABLE_NAME = "book_table";

    class BookEntry implements BaseColumns{
        public static final String BOOK_TITLE = "book_title";
        public static final String BOOK_PUBLISHED_DATE = "published_date";
        public static final String BOOK_DESCRIPTION = "book_description";

        public static final String CREATE_BOOK_TABLE =
                "CREATE TABLE "+TABLE_NAME + " ("+BOOK_TITLE
                        +" TEXT PRIMARY KEY"
                        +","+ BOOK_PUBLISHED_DATE
                        + " TEXT"+", "+BOOK_DESCRIPTION
                        +" TEXT"+")";
    }
}
