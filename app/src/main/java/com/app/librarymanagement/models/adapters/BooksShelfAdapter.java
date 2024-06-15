package com.app.librarymanagement.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Users.BookShelfDetailsActivity;

import java.util.Collections;
import java.util.List;

public class BooksShelfAdapter extends RecyclerView.Adapter<BooksShelfAdapter.MyShelfBook> {
    List<com.app.librarymanagement.models.MyShelfBook> list = Collections.emptyList();
    Context mContext;
    public BooksShelfAdapter(List<com.app.librarymanagement.models.MyShelfBook> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyShelfBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_request, parent, false);
        return new BooksShelfAdapter.MyShelfBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShelfBook holder, int position) {
       com.app.librarymanagement.models.MyShelfBook request = list.get(position);
        holder.requested_book_name.setText(request.getBookName());
        holder.request_user_name.setText(request.getUserName());
        holder.text_request_date.setText(request.getRequestedDate());
        holder.requestedBookItem.setOnClickListener(view->{
            Intent intent = new Intent(view.getContext(), BookShelfDetailsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("shelf_id",request.getId());
            intent.putExtras(extras);
            view.getContext().startActivity(intent);
        });

        if(request.getStatus().equals("Rejected")){
            holder.user_item_image.setColorFilter(ContextCompat.getColor(mContext, R.color.red), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else if(request.getStatus().equals("Accepted")){
            holder.user_item_image.setColorFilter(ContextCompat.getColor(mContext, R.color.green), android.graphics.PorterDuff.Mode.MULTIPLY);
        }else{
            holder.user_item_image.setColorFilter(ContextCompat.getColor(mContext, R.color.primary), android.graphics.PorterDuff.Mode.MULTIPLY);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyShelfBook extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView requested_book_name,request_user_name,text_request_date;
        CardView requestedBookItem;
        ImageView user_item_image;
        public MyShelfBook(@NonNull View itemView) {
            super(itemView);
            text_request_date = itemView.findViewById(R.id.text_request_date);
            request_user_name = itemView.findViewById(R.id.requested_user_name);
            requested_book_name = itemView.findViewById(R.id.requested_book_name);
            requestedBookItem = itemView.findViewById(R.id.requestedBookItem);
            user_item_image = itemView.findViewById(R.id.user_item_image);
        }
    }
}
