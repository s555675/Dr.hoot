package com.app.librarymanagement.models.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Admin.AuthorDetailsActivity;
import com.app.librarymanagement.activities.Admin.UserDetailsActivity;
import com.app.librarymanagement.models.User;

import java.util.Collections;
import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.MyUsers> {
    List<User> list = Collections.emptyList();
    Context mContext;
    public UsersAdapter(List<User> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyUsers onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new MyUsers(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyUsers holder, int position) {
        User user = list.get(position);
        holder.textViewTitle.setText(user.getName());
        holder.textViewTelephone.setText("Tel : "+user.getTel());
        holder.parent_layout.setOnClickListener(view->{
            Intent intent = new Intent(view.getContext(), UserDetailsActivity.class);
            Bundle extras = new Bundle();
            extras.putString("user_id",user.getId());
            intent.putExtras(extras);
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyUsers extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        Button callBtn;
        TextView textViewTitle;
        TextView textViewTelephone;
        ImageView imageViewUser;
        RelativeLayout parent_layout;
        public MyUsers(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.user_view_title);
            textViewTelephone = itemView.findViewById(R.id.text_view_telephone);
            imageViewUser = itemView.findViewById(R.id.user_item_image);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
