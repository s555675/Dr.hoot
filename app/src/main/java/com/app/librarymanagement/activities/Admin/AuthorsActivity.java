package com.app.librarymanagement.activities.Admin;

import static android.content.ContentValues.TAG;
import static com.app.librarymanagement.helpers.common_helper.getAuthorsData;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Author;
import com.app.librarymanagement.models.adapters.AuthorsAdapter;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthorsActivity extends AppCompatActivity {
    AuthorsAdapter adapter;
    RecyclerView recyclerView;
    TextView tvMsg;
    List<Author> list = new ArrayList<>();
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_authors);

        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this,DashboardAdminActivity.class));
        });
        findViewById(R.id.addAuthor).setOnClickListener(view->{
            startActivity(new Intent(this, AddAuthorActivity.class));
        });

        FirebaseApp.initializeApp(this);
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        recyclerView = findViewById(R.id.ListMyAuthors);
        tvMsg = findViewById(R.id.tvMsg);
        adapter = new AuthorsAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference = FirebaseDatabase.getInstance().getReference("authors");
        getAuthors();
    }

    public void getAuthors(){
        list.clear();
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        list.add(new Author(id, authName, rating, age, gender));
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
