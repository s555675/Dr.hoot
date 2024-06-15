package com.app.librarymanagement.activities.Users;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.BookRequest;
import com.app.librarymanagement.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class BookDetailsActivity extends AppCompatActivity {
    Button btnSendRequest;
    TextView tvBookName,tvPublishedOn,tvShortDesc,tvLongDesc,tvCount,tvAuthor;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private String uName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this, FindBooksActivity.class));
        });
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        tvBookName = findViewById(R.id.tvBookName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedOn = findViewById(R.id.tvPublishedOn);
        tvShortDesc = findViewById(R.id.tvShortDesc);
        tvLongDesc = findViewById(R.id.tvLongDesc);
        tvCount = findViewById(R.id.tvCount);
        btnSendRequest = findViewById(R.id.btnSendRequest);

        Intent intent = getIntent();

        if (null != intent) {
            //Null Checking
            String strId= intent.getStringExtra("book_id");
            if(!strId.isEmpty()) {
                getBookDetails(strId);
                // displayData(book);
            }
            else {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), FindBooksActivity.class));
            }
        }
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(Objects.requireNonNull(mAuth.getUid())).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user =  task.getResult().getValue(User.class);
                try {
                    assert user != null;
                    uName = user.getName();
                } catch (Exception ex) {
                    ex.getMessage();
                    mAuth.signOut();
                }
            }
        });
    }

    boolean flag =false;
    String auth_name= "";

    private void getBookDetails(String strId){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        if (strId.equals(id)) {
                            String bookName = ds.child("name").getValue(String.class);
                            String auth_id = ds.child("auth_id").getValue(String.class);
                            String longDesc = ds.child("longDescription").getValue(String.class);
                            String rating = ds.child("rating").getValue(String.class);
                            String shortDesc = ds.child("shortDescription").getValue(String.class);
                            String published = ds.child("published").getValue(String.class);
                            int count = ds.child("count").getValue(Integer.class);
                            int likes = ds.child("likes").getValue(Integer.class);

                            FirebaseDatabase.getInstance().getReference("authors").child(auth_id)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            try {
                                                flag = true;
                                                String auth_id = snapshot.child("id").getValue(String.class);
                                                auth_name = snapshot.child("name").getValue(String.class);
                                                Book book = new Book(id, auth_id, bookName, shortDesc, longDesc,
                                                        rating, published, count, likes);
                                                displayData(book);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
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

    private void displayData(Book book){
        tvBookName.setText(book.getName());
        tvPublishedOn.setText(book.getPublished());
        tvShortDesc.setText(book.getShortDescription());
        tvLongDesc.setText(book.getLongDescription());
        tvAuthor.setText(auth_name);
        tvCount.setText("Available: "+book.getCount());

        if(book.getCount() == 0)   btnSendRequest.setVisibility(View.GONE);

        btnSendRequest.setOnClickListener(view->{
            sendBookRequest(book);
        });
    }

    private void sendBookRequest(Book book){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        String uKey = mDatabase.getDatabase().getReference("book_requests").push().getKey();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        BookRequest request = new BookRequest(uKey, book.getId(), mAuth.getUid(), book.getName(),
                uName, date, "Pending");

        mDatabase.child("book_requests").child(uKey).setValue(request)
                       .addOnSuccessListener(command -> {
                           Toast.makeText(this,"Request sent!",Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(this,FindBooksActivity.class));
                       })
                       .addOnFailureListener(command ->
                               Toast.makeText(this, "failed to send request!", Toast.LENGTH_SHORT).show()
                       );
    }

}
