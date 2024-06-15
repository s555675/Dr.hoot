package com.app.librarymanagement.activities.Users;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.LoginActivity;
import com.app.librarymanagement.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class DashboardUserActivity extends AppCompatActivity {
    Button findBtn,btnMyShelf,myDues,btnAuthors,btnProfile,signOutBtn;
    TextView tvUser;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        mAuth = FirebaseAuth.getInstance();
        signOutBtn = findViewById(R.id.signOutBtn);
        findBtn = findViewById(R.id.findBtn);
        btnMyShelf = findViewById(R.id.btnMyShelves);
        myDues = findViewById(R.id.myDues);
        findBtn = findViewById(R.id.findBtn);
        btnAuthors = findViewById(R.id.btnAuthors);
        btnProfile = findViewById(R.id.btnProfile);
        tvUser = findViewById(R.id.tvUser);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").child(Objects.requireNonNull(mAuth.getUid())).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                User user =  task.getResult().getValue(User.class);
                try {
                    assert user != null;
                    String uName = user.getName();
                    tvUser.setText("Welcome "+uName);
                } catch (Exception ex) {
                    ex.getMessage();
                    mAuth.signOut();
                }
            }
        });
        findBtn.setOnClickListener(view->{
            startActivity(new Intent(this, FindBooksActivity.class));
        });
        btnAuthors.setOnClickListener(view->{
            startActivity(new Intent(this, AuthorsActivity.class));
        });
        btnMyShelf.setOnClickListener(view->{
            startActivity(new Intent(this, MyShelvesActivity.class));
        });
        myDues.setOnClickListener(view->{
            startActivity(new Intent(this, MyDuesActivity.class));
        });
        btnProfile.setOnClickListener(view->{
            startActivity(new Intent(this, UserProfile.class));
        });
        signOutBtn.setOnClickListener(v -> {
            mAuth.signOut();
            Toast.makeText(this, "Good bye!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });

    }
}
