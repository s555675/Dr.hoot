package com.app.librarymanagement.models.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.app.librarymanagement.activities.Admin.AuthorDetailsActivity;
import com.app.librarymanagement.models.Author;

import java.util.Collections;
import java.util.List;

public class AuthorsViewAdapter extends RecyclerView.Adapter<AuthorsViewAdapter.MyAuthor> {
    List<Author> list = Collections.emptyList();
    Context mContext;
    public AuthorsViewAdapter(List<Author> list, Context context){
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
            view.getContext().startActivity(new Intent(view.getContext(), AuthorDetailsActivity.class));
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
