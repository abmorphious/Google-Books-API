package daabsoft.com.googlebooks;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    Context mcontext;
    LayoutInflater layoutInflater;
    ArrayList<BookInfo> rBooks;

    MyAdapter(Context context, ArrayList<BookInfo> resultBooks)
    {
        mcontext = context;
        layoutInflater = LayoutInflater.from(mcontext);
        rBooks = resultBooks;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate View
        View view = layoutInflater.inflate(R.layout.book_list, parent, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //Set data to the views
        BookInfo bInfo = rBooks.get(position);
        holder.bookTitle.setText(bInfo.title.toString());
        holder.bookPublisher.setText(bInfo.publisher.toString());
    }

    @Override
    public int getItemCount() {
        return rBooks.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView bookTitle;
        TextView bookPublisher;
        public MyViewHolder(View view) {
            super(view);
            bookTitle = (TextView)view.findViewById(R.id.book_title);
            bookPublisher = (TextView)view.findViewById(R.id.books_result);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            BookInfo selectedBook = rBooks.get(position);
            Intent bookDetailIntent = new Intent(view.getContext(), BookDetail.class);
            bookDetailIntent.putExtra("bookParcel", selectedBook);
            view.getContext().startActivity(bookDetailIntent);
        }
    }
}