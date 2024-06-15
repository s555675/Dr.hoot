package com.app.librarymanagement.activities.Users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.adapters.FindBookAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FindBooksActivity extends AppCompatActivity {
    FindBookAdapter adapter;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    List<Book> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_books);
        setUpRecyclerView();
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),DashboardUserActivity.class));
        });
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.ListMyBooks);
        adapter = new FindBookAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        getBooks();
    }

    private void getBooks(){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String bookName = ds.child("name").getValue(String.class);
                        String auth_id = ds.child("auth_id").getValue(String.class);
                        String longDesc = ds.child("longDescription").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String shortDesc = ds.child("shortDescription").getValue(String.class);
                        String published = ds.child("published").getValue(String.class);
                        int count = ds.child("count").getValue(Integer.class);
                        int likes = ds.child("likes").getValue(Integer.class);

                        list.add(new Book(id, auth_id, bookName, shortDesc, longDesc, rating, published, count,likes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
