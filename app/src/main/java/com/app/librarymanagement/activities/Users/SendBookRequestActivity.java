package com.app.librarymanagement.activities.Users;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.app.librarymanagement.R;

public class SendBookRequestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_request);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            this.finish();
        });
    }
}
