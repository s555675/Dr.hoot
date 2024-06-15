package com.app.librarymanagement.models.adapters;

import static com.app.librarymanagement.helpers.common_helper.addDay;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Admin.RequestedBookDetailsActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class OverDueBooksAdapter extends RecyclerView.Adapter<OverDueBooksAdapter.BookRequest> {
    List<com.app.librarymanagement.models.BookRequest> list = Collections.emptyList();
    Context mContext;
    public OverDueBooksAdapter(List<com.app.librarymanagement.models.BookRequest> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public BookRequest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.overdue_book_item, parent, false);
        return new OverDueBooksAdapter.BookRequest(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull BookRequest holder, int position) {
        com.app.librarymanagement.models.BookRequest book = list.get(position);
        holder.requested_book_name.setText(book.getBookName());
        holder.request_user_name.setText(book.getUserName());
        holder.tvCollectDate.setText("On date: "+book.getRequestedDate());
        holder.tvDueDate.setText("Due On: "+book.getRequestedDate());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date strDate = null;
        try {
            strDate = sdf.parse(book.getRequestedDate());
            Date dueDate = addDay(strDate,15);
            long time_difference = new Date().getTime() - dueDate.getTime();
            long days_difference = (time_difference / (1000*60*60*24)) % 365;
            if(days_difference > 0){
                holder.tvDueDate.setText("Overdue: "+ days_difference+" Days");
                holder.tvDueDate.setVisibility(View.VISIBLE);
            }
            else{
                holder.tvDueDate.setVisibility(View.GONE);
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class BookRequest extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView requested_book_name, request_user_name, tvCollectDate, tvDueDate;
        public BookRequest(@NonNull View itemView) {
            super(itemView);
            tvCollectDate = itemView.findViewById(R.id.tvCollectDate);
            tvDueDate = itemView.findViewById(R.id.tvDueDate);
            request_user_name = itemView.findViewById(R.id.request_user_name);
            requested_book_name = itemView.findViewById(R.id.requested_book_name);
        }
    }
}
