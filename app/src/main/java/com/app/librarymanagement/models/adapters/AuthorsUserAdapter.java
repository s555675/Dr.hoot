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
import com.app.librarymanagement.activities.Users.AuthorDetailsActivity;
import com.app.librarymanagement.models.Author;

import java.util.Collections;
import java.util.List;

public class AuthorsUserAdapter extends RecyclerView.Adapter<AuthorsUserAdapter.MyAuthor> {
    List<Author> list = Collections.emptyList();
    Context mContext;
    public AuthorsUserAdapter(List<Author> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyAuthor onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.author_item, parent, false);
        return new MyAuthor(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAuthor holder, int position) {
        Author author = list.get(position);
        holder.tvAuthName.setText(author.getName());
        holder.tvAge.setText("Years : " + author.getAge() + " old");
        holder.tvRating.setRating(Float.parseFloat(author.getRating()));
        holder.authorItemCard.setOnClickListener(view->{
            Intent intent = new Intent(view.getContext(), AuthorDetailsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("author_id",author.getId());
            intent.putExtras(extras);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyAuthor extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvAuthName, tvAge;
        ImageView imageViewAuth;
        CardView authorItemCard;
        RatingBar tvRating;
        public MyAuthor(@NonNull View itemView) {
            super(itemView);
            tvAuthName = itemView.findViewById(R.id.view_auth_name);
            tvAge = itemView.findViewById(R.id.auth_view_age);
            tvRating = itemView.findViewById(R.id.auth_view_rating);
            authorItemCard = itemView.findViewById(R.id.authorItemCard);
            imageViewAuth = itemView.findViewById(R.id.auth_item_image);
        }
    }
}
