package com.app.librarymanagement.activities.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Users.FindBooksActivity;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.BookRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class RequestedBookDetailsActivity extends AppCompatActivity {
    TextView tvUserName,book_title,tvReqDate,tvCount;
    Button btnRequestAccept,btnRequestReject,btnCanceled,btnAccepted;
    private DatabaseReference databaseReference;
    List<BookRequest> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request_view);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),RequestedBooksActivity.class));
        });
        tvUserName=findViewById(R.id.tvUserName);
        book_title=findViewById(R.id.book_title);
        tvReqDate=findViewById(R.id.tvReqDate);
        tvCount=findViewById(R.id.tvCount);
        btnRequestAccept=findViewById(R.id.btnRequestAccept);
        btnRequestReject=findViewById(R.id.btnRequestReject);
        btnCanceled=findViewById(R.id.btnCanceled);
        btnAccepted=findViewById(R.id.btnAccepted);

        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            String strId= intent.getStringExtra("req_id");
            if(!strId.isEmpty()) {
                getRequestedBookData(strId);
            }
            else {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),RequestedBooksActivity.class));
            }
        }
    }
    boolean flag = false;
    int count = 0;
    BookRequest bookRequest = new BookRequest();
    Book book = new Book();
    private void getRequestedBookData(String strId){
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

                            if(status.equals("Accepted")){
                                btnRequestReject.setVisibility(View.GONE);
                                btnRequestAccept.setVisibility(View.GONE);
                                btnCanceled.setVisibility(View.GONE);
                                btnAccepted.setVisibility(View.VISIBLE);
                            }
                            else if(status.equals("Rejected")){
                                btnRequestReject.setVisibility(View.GONE);
                                btnRequestAccept.setVisibility(View.GONE);
                                btnCanceled.setVisibility(View.VISIBLE);
                                btnAccepted.setVisibility(View.GONE);
                            }
                            else{
                                btnRequestReject.setVisibility(View.VISIBLE);
                                btnRequestAccept.setVisibility(View.VISIBLE);
                                btnCanceled.setVisibility(View.GONE);
                                btnAccepted.setVisibility(View.GONE);
                            }
                            bookRequest = new BookRequest(id, bookId, user_id, bookName, userName, requestedDate, status);

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
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    displayData(bookRequest,count);
                                }  @Override
                                public void onCancelled(DatabaseError databaseError) {}
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(!flag){
                    Toast.makeText(RequestedBookDetailsActivity.this, "invalid details!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), RequestedBooksActivity.class));
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        btnRequestAccept.setOnClickListener(view->{
            //update status "accepted"
            updateStatus(bookRequest,"Accepted");
        });
        btnRequestReject.setOnClickListener(view->{
            //update status "rejected"
            updateStatus(bookRequest,"Rejected");
        });
    }

    private void updateStatus(BookRequest updateBookReq, String status){
        updateBookReq.setStatus(status);
        FirebaseDatabase.getInstance().getReference().child("book_requests").child(updateBookReq.getId()).setValue(updateBookReq)
                .addOnSuccessListener(command -> {
                    if(status.equals("Accepted"))
                        updateBookCount(book,status);
                    else{
                        Toast.makeText(this,status+"!",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),RequestedBooksActivity.class));
                    }
                })
                .addOnFailureListener(command ->{
                        Toast.makeText(this, "failed!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),RequestedBooksActivity.class));
                });
    }

    private void displayData(BookRequest book, int count){
        tvUserName.setText(book.getUserName());
        book_title.setText(book.getBookName());
        tvCount.setText("Available: "+count);
        tvReqDate.setText(book.getRequestedDate());
    }

    private void updateBookCount(Book book, String status){
        Book updateBook = new Book(book.getId(),book.getAuth_id(),book.getName(),
                book.getShortDescription(),book.getLongDescription(),book.getRating(),book.getPublished(),(book.getCount() -1),book.getLikes());
        FirebaseDatabase.getInstance().getReference().child("books").child(book.getId()).setValue(updateBook)
                .addOnSuccessListener(command -> {
                    Toast.makeText(this,status+"!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),RequestedBooksActivity.class));
                })
                .addOnFailureListener(command ->
                        Toast.makeText(this, "failed to add book!", Toast.LENGTH_SHORT).show()
                );
    }

}
