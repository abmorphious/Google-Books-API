package daabsoft.com.googlebooks;

import android.os.Parcel;
import android.os.Parcelable;

public class BookInfo implements Parcelable {
    String title;
    String publisher;
    String description;
    public BookInfo(String title, String publisher, String description) {
        this.title = title;
        this.publisher = publisher;
        this.description = description;
    }

    protected BookInfo(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        description = in.readString();
    }

    public static final Creator<BookInfo> CREATOR = new Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel in) {
            return new BookInfo(in);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(publisher);
        parcel.writeString(description);
    }
}
