package com.app.librarymanagement.activities.Users;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.MyShelfBook;
import com.app.librarymanagement.models.adapters.BooksShelfAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyShelvesActivity extends AppCompatActivity {
    BooksShelfAdapter adapter;
    RecyclerView recyclerView;
    TextView tvMsg;
    List<MyShelfBook> list = new ArrayList<>();
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shelves);
        setUpRecyclerView();
        findViewById(R.id.btnBack).setOnClickListener(view->{
            this.finish();
        });
    }
    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.ListMyBooks);
        tvMsg = findViewById(R.id.tvMsg);
        adapter = new BooksShelfAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
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

                        if(mAuth.getUid().equals(user_id)){
                            list.add(new MyShelfBook(id, bookId, user_id, bookName, userName,
                                    requestedDate, requestedDate,status));
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
