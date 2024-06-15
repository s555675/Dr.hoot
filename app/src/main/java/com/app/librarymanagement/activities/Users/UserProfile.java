package com.app.librarymanagement.activities.Users;

import static com.app.librarymanagement.helpers.common_helper.getUserDetails;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.librarymanagement.R;
import com.app.librarymanagement.helpers.BaseActivity;
import com.app.librarymanagement.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class UserProfile extends BaseActivity {
    TextView tvName,tvJob,tvEmail,tvMobile,tvAddress,tvAge,tvGender;
    Button btnHome;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this, DashboardUserActivity.class));
        });
        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvMobile = findViewById(R.id.tvMobile);
        tvAddress = findViewById(R.id.tvAddress);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);
        btnHome = findViewById(R.id.btnHome);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            String strId= "1";
            if(!strId.isEmpty()){
                mDatabase.child("users").child(Objects.requireNonNull(mAuth.getUid())).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user =  task.getResult().getValue(User.class);
                        try {
                            assert user != null;
                            showDetails(user);
                        } catch (Exception ex) {
                            ex.getMessage();
                            mAuth.signOut();
                        }
                    }  else{
                        Toast.makeText(this, "error!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
                    }
                });
            }
            else {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
            }
        }
        btnHome.setOnClickListener(view->{
            startActivity(new Intent(this, DashboardUserActivity.class));
        });
    }

    private void showDetails(User user){
        tvName.setText(user.getName());
        tvAddress.setText(user.getAddress());
        tvEmail.setText(user.getEmail());
        tvMobile.setText(user.getTel());
        tvAge.setText("Age: "+user.getAge());
        tvGender.setText("Gender: "+user.getGender());
    }
}
