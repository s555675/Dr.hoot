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
import com.app.librarymanagement.activities.Admin.BooksActivity;
import com.app.librarymanagement.activities.Admin.RequestedBookDetailsActivity;
import com.app.librarymanagement.activities.Admin.RequestedBooksActivity;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.BookRequest;
import com.app.librarymanagement.models.MyShelfBook;
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

public class BookShelfDetailsActivity extends AppCompatActivity {
    Button  btnSendRequest;
    TextView tvBookName,tvPublishedOn,tvReturnedDate,tvShortDesc,tvLongDesc,tvCount,tvAuthor;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    private String uName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_shelf_details);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this, MyShelvesActivity.class));
        });
        tvBookName = findViewById(R.id.tvBookName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedOn = findViewById(R.id.tvPublishedOn);
        tvReturnedDate = findViewById(R.id.tvReturnedDate);
        tvShortDesc = findViewById(R.id.tvShortDesc);
        tvLongDesc = findViewById(R.id.tvLongDesc);
        tvCount = findViewById(R.id.tvCount);
        btnSendRequest = findViewById(R.id.btnSendRequest);

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

        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            String strId= intent.getStringExtra("shelf_id");
            if(!strId.isEmpty())
                getBookDetails(strId);
            else {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), BooksActivity.class));
            }
        }
        btnSendRequest.setOnClickListener(view->{
            Toast.makeText(this, "Request Sent!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),MyShelvesActivity.class));
        });
    }

    boolean flag =false;
    String auth_name= "";
    int count = 0;
    MyShelfBook bookRequest = new MyShelfBook();
    Book book = new Book();

    private void getBookDetails(String strId){
        databaseReference = FirebaseDatabase.getInstance().getReference("book_requests");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String status = ds.child("status").getValue(String.class);
                        if (strId.equals(id)) {
                            flag = true;
                            String bookId = ds.child("book_id").getValue(String.class);
                            String requestedDate = ds.child("requestedDate").getValue(String.class);

                            String userName = ds.child("userName").getValue(String.class);
                            String user_id = ds.child("user_id").getValue(String.class);
                            String bookName = ds.child("bookName").getValue(String.class);

                            bookRequest = new MyShelfBook(id, bookId, user_id, bookName, userName, requestedDate,requestedDate, status);

                            //GET available book count from books collection
                            FirebaseDatabase.getInstance().getReference("books")
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @SuppressLint("NotifyDataSetChanged")
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                try {
                                                    String id = ds.child("id").getValue(String.class);
                                                    if (bookId.equals(id)) {
                                                        count = ds.child("count").getValue(Integer.class);
                                                        String bookName = ds.child("name").getValue(String.class);
                                                        String auth_id = ds.child("auth_id").getValue(String.class);
                                                        String longDesc = ds.child("longDescription").getValue(String.class);
                                                        String rating = ds.child("rating").getValue(String.class);
                                                        String shortDesc = ds.child("shortDescription").getValue(String.class);
                                                        String published = ds.child("published").getValue(String.class);
                                                        int likes = ds.child("likes").getValue(Integer.class);

                                                        book = new Book(id, auth_id, bookName, shortDesc, longDesc,
                                                                rating, published, count, likes);
                                                        FirebaseDatabase.getInstance().getReference("authors").child(book.getAuth_id())
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                        try {
                                                                            flag = true;
                                                                            String auth_id = snapshot.child("id").getValue(String.class);
                                                                            auth_name = snapshot.child("name").getValue(String.class);
                                                                            displayData(bookRequest,book,auth_name);
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(!flag){
                    Toast.makeText(getApplicationContext(), "invalid details!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), RequestedBooksActivity.class));
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void displayData(MyShelfBook myShelfBook, Book book, String auth_name){
        tvBookName.setText(myShelfBook.getBookName());
        tvPublishedOn.setText(myShelfBook.getRequestedDate());
        tvReturnedDate.setText(myShelfBook.getRequestedDate());
        tvShortDesc.setText(book.getShortDescription());
        tvLongDesc.setText(book.getLongDescription());
        tvAuthor.setText(auth_name);
        tvCount.setText("Available: "+book.getCount());

        if(!book.toString().equals("Accepted")){
            tvReturnedDate.setText("-");
        }
        if(count == 0)   btnSendRequest.setVisibility(View.GONE);

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
