package com.app.librarymanagement.activities.Users;

import static com.app.librarymanagement.helpers.common_helper.addDay;
import static com.app.librarymanagement.helpers.common_helper.getOverdueBooksData;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.BookRequest;
import com.app.librarymanagement.models.adapters.BooksAdapter;
import com.app.librarymanagement.models.adapters.OverDueBooksAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyDuesActivity extends AppCompatActivity {
    OverDueBooksAdapter adapter;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    List<BookRequest> list = new ArrayList<>();
    TextView tvMsg;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dues);
        setUpRecyclerView();
        findViewById(R.id.btnBack).setOnClickListener(view->{
            this.finish();
        });
    }
    public void setUpRecyclerView(){
        tvMsg = findViewById(R.id.tvMsg);
        recyclerView = findViewById(R.id.ListMyBooks);
        adapter = new OverDueBooksAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        getBooks();
    }

    private void getBooks(){
        databaseReference = FirebaseDatabase.getInstance().getReference("book_requests");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String bookId = ds.child("book_id").getValue(String.class);
                        String requestedDate = ds.child("requestedDate").getValue(String.class);
                        String status = ds.child("status").getValue(String.class);
                        String userName = ds.child("userName").getValue(String.class);
                        String user_id = ds.child("user_id").getValue(String.class);

                        String bookName = ds.child("bookName").getValue(String.class);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                        Date strDate = null;
                        try {
                            strDate = sdf.parse(requestedDate);
                            Date dueDate = addDay(strDate,15);
                            long time_difference = new Date().getTime() - dueDate.getTime();
                            long days_difference = (time_difference / (1000*60*60*24)) % 365;
                            if(days_difference > 0 && user_id.equals(mAuth.getUid())){
                                list.add(new BookRequest(id, bookId, user_id, bookName, userName, requestedDate, status));
                            }
                        }catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
                if(list.size() == 0){
                    recyclerView.setVisibility(View.GONE);
                    tvMsg.setVisibility(View.VISIBLE);
                }else{
                    recyclerView.setVisibility(View.VISIBLE);
                    tvMsg.setVisibility(View.GONE);
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
