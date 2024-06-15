package com.app.librarymanagement.activities.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.User;
import com.app.librarymanagement.models.adapters.UsersAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyUsersActivity extends AppCompatActivity {
    UsersAdapter adapter;
    RecyclerView recyclerView;
    private DatabaseReference databaseReference;
    List<User> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        setUpRecyclerView();
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this,DashboardAdminActivity.class));
        });
    }

    public void setUpRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.ListMyUsers);
        adapter = new UsersAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getUsersData();
    }

    private void getUsersData(){
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String name = ds.child("name").getValue(String.class);
                        String email = ds.child("email").getValue(String.class);
                        String gender = ds.child("gender").getValue(String.class);
                        int age = ds.child("age").getValue(Integer.class);
                        String tel = ds.child("tel").getValue(String.class);
                        String address = ds.child("address").getValue(String.class);
                        list.add(new User(id, name, address,tel,email, age, gender));
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
