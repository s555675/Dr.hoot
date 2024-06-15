package com.app.librarymanagement.activities.Users;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Author;
import com.app.librarymanagement.models.adapters.AuthorsUserAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthorsActivity extends AppCompatActivity {
    AuthorsUserAdapter adapter;
    RecyclerView recyclerView;
    List<Author> list = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_authors);
        setUpRecyclerView();
        findViewById(R.id.btnBack).setOnClickListener(view->{
            this.finish();
        });
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.ListMyAuthors);
        adapter = new AuthorsUserAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("authors");
        getAuthors();
    }

    public void getAuthors(){
        list.clear();
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String authName = ds.child("name").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String gender = ds.child("gender").getValue(String.class);
                        Integer age = ds.child("age").getValue(Integer.class);
                        list.add(new Author(id,authName,rating,age,gender));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                adapter.notifyDataSetChanged();
            }  @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
