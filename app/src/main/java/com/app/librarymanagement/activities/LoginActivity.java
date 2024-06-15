package com.app.librarymanagement.activities;

import static android.content.ContentValues.TAG;
import static com.app.librarymanagement.helpers.common_helper.getAdminLogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.librarymanagement.R;
import com.app.librarymanagement.activities.Admin.DashboardAdminActivity;
import com.app.librarymanagement.activities.Users.DashboardUserActivity;
import com.app.librarymanagement.helpers.BaseActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class LoginActivity extends BaseActivity {
    Button btnLogin;
    TextView tvCreateAccount;
    EditText etEmail, etPassword;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        btnLogin = findViewById(R.id.LoginBtn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvCreateAccount = findViewById(R.id.CreateAccount);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            if (!validateForm()) {
                return;
            }
            if (getAdminLogin(etEmail.getText().toString().trim(),etPassword.getText().toString().trim())) {
                startActivity(new Intent(getApplicationContext(), DashboardAdminActivity.class));
            }else{
                signIn(etEmail.getText().toString().trim(), etPassword.getText().toString().trim(), view);
            }
        });
        mAuthListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null) {
                // User is signed in
                startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
                Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
            }
        };
        tvCreateAccount.setOnClickListener(view->
            startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class))
        );
    }

    private void signIn(String email, String password,View view) {
        Log.d(TAG, "signIn:" + email);
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail", task.getException());
                        Snackbar.make(view, "Invalid Credentials!!", Snackbar.LENGTH_SHORT)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view1) {
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.red))
                                .show();
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(),DashboardUserActivity.class));
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
        // [END sign_in_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;
        String email = etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            etEmail.setError("Required.");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        String password = etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            etPassword.setError("Required.");
            valid = false;
        } else {
            etPassword.setError(null);
        }
        return valid;
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]
    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}