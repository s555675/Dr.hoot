package com.app.librarymanagement.activities.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.SpinnerItem;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddBookActivity extends AppCompatActivity {
    EditText bookName,shortDesc,longDesc,etBookCount,etPublished;
    String stBookName, stAuthorName, stShortDesc, stLongDesc,stBookCount,stPublished;
    Button btnAddBook;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    Spinner spAuthors;

    ArrayAdapter<SpinnerItem> adapter;

    List<SpinnerItem> authorList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this,BooksActivity.class));
        });
        spAuthors = findViewById(R.id.spAuthors);
        bookName = findViewById(R.id.bookName);
        shortDesc = findViewById(R.id.shortDesc);
        longDesc = findViewById(R.id.longDesc);
        etBookCount = findViewById(R.id.etBookCount);
        btnAddBook = findViewById(R.id.btnAddBook);
        etPublished = findViewById(R.id.etPublished);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnAddBook.setOnClickListener(view->{
            validateBookDetails();
        });
        setAuthSpinner();
    }

    void validateBookDetails(){
        boolean flag=false;
        stBookName = bookName.getText().toString();
        stBookCount = etBookCount.getText().toString();
        stShortDesc = shortDesc.getText().toString();
        stLongDesc = longDesc.getText().toString();
        stPublished = etPublished.getText().toString();

        if(stBookName.isEmpty()) bookName.setError("required");
        else if(stBookCount.isEmpty()) etBookCount.setError("required");
        else if(stShortDesc.isEmpty()) shortDesc.setError("required");
        else if(stLongDesc.isEmpty()) longDesc.setError("required");
        else if(stPublished.isEmpty()) etPublished.setError("required");
        else flag = true;

        if(flag){
            int count = 0;
            count = Integer.parseInt(stBookCount);
            addBookDetails(stBookName,count,stShortDesc,stLongDesc,stPublished);
        }else{
            Toast.makeText(this, "false..!", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAuthSpinner(){
        authorList.add(new SpinnerItem("0","Select Author"));
        ArrayAdapter<SpinnerItem> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, authorList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAuthors.setAdapter(dataAdapter);

        FirebaseDatabase.getInstance().getReference("authors").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String authName = ds.child("name").getValue(String.class);
                        authorList.add(new SpinnerItem(id, authName));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                dataAdapter.notifyDataSetChanged();
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    void addBookDetails(String name,int count, String shortDesc, String longDesc, String stPublished){
        String uKey = mDatabase.getDatabase().getReference("books").push().getKey();
        SpinnerItem item = (SpinnerItem)spAuthors.getSelectedItem();
        String authId= item.getId();

        Book book = new Book(uKey,authId,name, shortDesc, longDesc, "5.0",stPublished,count,0);
        mDatabase.child("books").child(uKey).setValue(book)
                .addOnSuccessListener(command -> {
                    Toast.makeText(this,"Added!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,BooksActivity.class));
                })
                .addOnFailureListener(command ->
                        Toast.makeText(this, "failed to add book!", Toast.LENGTH_SHORT).show()
                );
    }
}
