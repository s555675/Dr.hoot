package com.app.librarymanagement.activities;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Users.DashboardUserActivity;
import com.app.librarymanagement.helpers.BaseActivity;
import com.app.librarymanagement.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateAccountActivity extends BaseActivity {
    Button btnLogin;
    TextView tvCreateAccount;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    EditText etEmail,etPassword,etConfPass,etAge,etName,etMobile,etAddress;
    RadioButton rbMale,rbFemale,rbOther;
    RadioGroup rgGender;
    String stPassword,stEmail,stConfPass,stName,stAddress,stMobile,stAge,stGender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        btnLogin = findViewById(R.id.LoginBtn);
        tvCreateAccount = findViewById(R.id.CreateAccount);
        rgGender = findViewById(R.id.rgGender);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        rbOther = findViewById(R.id.rbOther);
        etAge = findViewById(R.id.etAge);
        etAddress = findViewById(R.id.etAddress);
        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etUsername);
        etMobile = findViewById(R.id.etMobile);
        etPassword = findViewById(R.id.etPassword);
        etConfPass = findViewById(R.id.etConfPassword);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnLogin.setOnClickListener(this::login);
        tvCreateAccount.setOnClickListener(this::signUp);
    }

    private void login(View view) {
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }

    private void signUp(View view) {
        Log.d(TAG, "signUp");
        if (!validateForm()) {
            return;
        }
        showProgressDialog();

        stEmail = etEmail.getText().toString().trim();
        stPassword = etPassword.getText().toString().trim();
        stConfPass = etConfPass.getText().toString().trim();
        stName = etName.getText().toString().trim();
        stAddress = etAddress.getText().toString().trim();
        stAge = etAge.getText().toString().trim();
        stMobile = etMobile.getText().toString().trim();
        int selectedId = rgGender.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        rbOther =  findViewById(selectedId);
        stGender = rbOther.getText().toString();

        mAuth.createUserWithEmailAndPassword(stEmail, stPassword)
                .addOnCompleteListener(task -> {
                    Log.d(TAG, "createUser:onComplete:" + task.isSuccessful());
                    hideProgressDialog();

                    if (task.isSuccessful()) {
                        onAuthSuccess(task.getResult().getUser());
                    } else {
                        Snackbar.make(view, "error: " + task.getException().getMessage(), Snackbar.LENGTH_SHORT)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view1) {
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.red))
                                .show();
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        // Write new user
        writeNewUser(user.getUid(), user.getEmail(),stName,stMobile,Integer.parseInt(stAge),stAddress,stGender);
        // Go to MainActivity
        Toast.makeText(this,"User account created!",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private boolean validateForm() {
        boolean result = true;
        if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Required");
            result = false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()) {
            etEmail.setError("Enter valid email!");
            result = false;
        }
        else{
            etEmail.setError(null);
        }
        if (TextUtils.isEmpty(etName.getText().toString())) {
            etName.setError("Required");
            result = false;
        } else {
            etName.setError(null);
        }

        if (TextUtils.isEmpty(etPassword.getText().toString())) {
            etPassword.setError("Required");
            result = false;
        } else {
            etPassword.setError(null);
        }

        if (TextUtils.isEmpty(etConfPass.getText().toString())) {
            etConfPass.setError("Required");
            result = false;
        } else if(!etConfPass.getText().toString().equals(etPassword.getText().toString())){
            result = false;
            etConfPass.setError("Password Mismatch!!");
        }else {
            etConfPass.setError(null);
        }
        return result;
    }

    // [START basic_write]
    private void writeNewUser(String userId,String email, String name, String mobile, int age, String address, String gender) {
        User user = new User(userId,name, address, mobile, email,age, gender);
        mDatabase.child("users").child(userId).setValue(user);
    }
}