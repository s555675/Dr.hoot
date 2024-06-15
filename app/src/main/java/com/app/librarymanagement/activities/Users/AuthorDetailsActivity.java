package com.app.librarymanagement.activities.Users;

import static com.app.librarymanagement.helpers.common_helper.getAuthorsDetails;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Author;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.adapters.FindBookAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AuthorDetailsActivity extends AppCompatActivity {
    FindBookAdapter adapter;
    RecyclerView recyclerView;
    String strId;
    TextView tvAuthName,tvAuthorAge,tvRating,tvNoRecords;
    private DatabaseReference databaseReference,databaseReference1;
    List<Book> list = new ArrayList<>();
    Author author = new Author();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_details);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            this.finish();
        });
        Intent intent = getIntent();
        if (null != intent)
            strId= intent.getStringExtra("author_id");

        tvAuthName = findViewById(R.id.tvAuthName);
        tvAuthorAge = findViewById(R.id.tvAuthorAge);
        tvRating = findViewById(R.id.tvRating);
        tvNoRecords = findViewById(R.id.tvNoRecords);
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        Author auth_details = getAuthorsDetails(strId);
        recyclerView = findViewById(R.id.ListMyBooks);
        adapter = new FindBookAdapter(list, getApplicationContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tvAuthName.setText(auth_details.getName());
        tvAuthorAge.setText("Age: "+auth_details.getAge());

        databaseReference = FirebaseDatabase.getInstance().getReference("authors");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("books");
        getAuthDetails();
    }

    private void getAuthDetails(){
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
                        if(strId.equals(id)){
                            author = new Author(id,authName,rating,age,gender);
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @SuppressLint("NotifyDataSetChanged")
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        try {
                                            String id = ds.child("id").getValue(String.class);
                                            String auth_id = ds.child("auth_id").getValue(String.class);
                                            String name = ds.child("name").getValue(String.class);
                                            String longDesc = ds.child("longDescription").getValue(String.class);
                                            String rating = ds.child("rating").getValue(String.class);
                                            String published = ds.child("published").getValue(String.class);
                                            String shortDesc = ds.child("shortDescription").getValue(String.class);
                                            int count = ds.child("count").getValue(Integer.class);
                                            int likes = ds.child("likes").getValue(Integer.class);
                                            if(strId.equals(auth_id)){
                                                list.add(new Book(id, auth_id, name, shortDesc, longDesc, rating, published, count, likes));
                                            }
                                            adapter.notifyDataSetChanged();
                                            setDetails();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }  @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setDetails(){
        tvAuthName.setText(author.getName());
        tvRating.setText("Rating: "+author.getRating());
        tvAuthorAge.setText("Age: "+author.getAge() +"Y");
        tvAuthorAge.setText("Age: "+author.getAge() +"Y");
        if(list.size() == 0){
            tvNoRecords.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }

}
