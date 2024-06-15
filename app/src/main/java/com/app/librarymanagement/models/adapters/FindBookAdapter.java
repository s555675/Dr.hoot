package com.app.librarymanagement.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Users.BookDetailsActivity;
import com.app.librarymanagement.models.Book;

import java.util.Collections;
import java.util.List;

public class FindBookAdapter extends RecyclerView.Adapter<FindBookAdapter.MyBook> {
    List<Book> list = Collections.emptyList();
    Context mContext;
    public FindBookAdapter(List<Book> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_item, parent, false);
        return new FindBookAdapter.MyBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBook holder, int position) {
        Book book = list.get(position);
        holder.tvBookName.setText(book.getName());
        holder.tvShortDesc.setText(book.getShortDescription());
//        holder.tvPublish.setText(book.getPublished());
        holder.tvRating.setRating(Float.parseFloat(book.getRating()));
        holder.bookCard.setOnClickListener(view->{
                Intent intent = new Intent(view.getContext(), BookDetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putString("book_id",book.getId());
                intent.putExtras(extras);
                view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyBook extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvBookName, tvPublish,tvShortDesc,tvLongDesc;
        ImageView imgBook;
        CardView bookCard;
        RatingBar tvRating;
        public MyBook(@NonNull View itemView) {
            super(itemView);
            tvBookName = itemView.findViewById(R.id.view_book_name);
            tvShortDesc = itemView.findViewById(R.id.view_book_short_desc);
            tvPublish = itemView.findViewById(R.id.book_published);
            tvRating = itemView.findViewById(R.id.book_view_rating);
            imgBook = itemView.findViewById(R.id.book_item_image);
            bookCard = itemView.findViewById(R.id.bookCard);
        }
    }
}
