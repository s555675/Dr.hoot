package com.app.librarymanagement.activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Author;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddAuthorActivity extends AppCompatActivity {
    EditText authorName,etRating,etAuthAge,authGender;
    String stAuthorName,stRating,stAuthAge,stAuthGender;
    Button btnAddBook;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_author);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this,DashboardAdminActivity.class));
        });
        findViewById(R.id.btnAddBook).setOnClickListener(view->{
            startActivity(new Intent(this,AuthorsActivity.class));
        });

        btnAddBook = findViewById(R.id.btnAddBook);
        authorName = findViewById(R.id.authorName);
        etRating = findViewById(R.id.etRating);
        etAuthAge = findViewById(R.id.etAuthAge);
        authGender = findViewById(R.id.authGender);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        btnAddBook.setOnClickListener(view->{
            validateAuthDetails();
        });
    }

    void validateAuthDetails(){
        boolean flag=false;
        stAuthorName = authorName.getText().toString();
        stRating = etRating.getText().toString();
        stAuthAge = etAuthAge.getText().toString();
        stAuthGender = authGender.getText().toString();

        if(stAuthorName.isEmpty()) authorName.setError("required");
        else if(stRating.isEmpty()) etRating.setError("required");
        else if(stAuthAge.isEmpty()) etAuthAge.setError("required");
        else flag = true;

        if(flag){
            addAuthDetails(stAuthorName,stRating,stAuthAge);
        }else{
            Toast.makeText(this, "false..!", Toast.LENGTH_SHORT).show();
        }
    }

    void addAuthDetails(String authName, String rating, String age){
        String uKey = mDatabase.getDatabase().getReference("authors").push().getKey();
        Author author = new Author(uKey,authName,rating,Integer.parseInt(age), stAuthGender);
        mDatabase.child("authors").child(uKey).setValue(author)
                .addOnSuccessListener(command -> {
                    Toast.makeText(this,"Added!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,AuthorsActivity.class));
                })
                .addOnFailureListener(command ->
                        Toast.makeText(this, "failed to add auth!", Toast.LENGTH_SHORT).show()
                );
    }
}
