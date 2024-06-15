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
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Admin.UserDetailsActivity;
import com.app.librarymanagement.models.SpinnerItem;
import com.app.librarymanagement.models.User;

import java.util.Collections;
import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.SpinnerItemClass> {
    List<SpinnerItem> list = Collections.emptyList();
    Context mContext;
    public SpinnerAdapter(List<SpinnerItem> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public SpinnerItemClass onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item, parent, false);
        return new SpinnerItemClass(v);
    }

    @Override
    public void onBindViewHolder(@NonNull SpinnerItemClass holder, int position) {
        SpinnerItem item = list.get(position);
        holder.textViewTitle.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class SpinnerItemClass extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        Button callBtn;
        TextView textViewTitle;
        public SpinnerItemClass(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.user_view_title);
        }
    }
}
