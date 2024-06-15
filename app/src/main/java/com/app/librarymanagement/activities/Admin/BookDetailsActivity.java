package com.app.librarymanagement.activities.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.app.librarymanagement.R;
import com.app.librarymanagement.models.Book;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookDetailsActivity extends AppCompatActivity {
    Button btnBookUpdate;
    TextView tvDelete,tvBookName,tvPublishedOn,tvShortDesc,tvLongDesc,tvCount,tvAuthor;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this,RequestedBooksActivity.class));
        });
        tvBookName = findViewById(R.id.tvBookName);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedOn = findViewById(R.id.tvPublishedOn);
        btnBookUpdate = findViewById(R.id.btnBookUpdate);
        tvShortDesc = findViewById(R.id.tvShortDesc);
        tvLongDesc = findViewById(R.id.tvLongDesc);
        tvCount = findViewById(R.id.tvCount);
        tvDelete = findViewById(R.id.tvDelete);
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            String strId= intent.getStringExtra("book_id");
            if(!strId.isEmpty()) {
               getBookDetails(strId);
            }
            else {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),BooksActivity.class));
            }
        }
    }

    private void getBookDetails(String strId){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        if(strId.equals(id)) {
                            flag = true;
                            String authName = ds.child("name").getValue(String.class);
                            String auth_id = ds.child("auth_id").getValue(String.class);
                            String longDesc = ds.child("longDescription").getValue(String.class);
                            String rating = ds.child("rating").getValue(String.class);
                            String shortDesc = ds.child("shortDescription").getValue(String.class);
                            String published = ds.child("published").getValue(String.class);
                            int count = ds.child("count").getValue(Integer.class);
                            int likes = ds.child("likes").getValue(Integer.class);

                            Book book = new Book(id, auth_id, authName, shortDesc, longDesc, rating, published, count, likes);
                            displayData(book);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "invalid book!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),BooksActivity.class));
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void displayData(Book book){
        tvBookName.setText(book.getName());
        tvAuthor.setText(book.getAuth_id());
        tvPublishedOn.setText(book.getPublished());
        tvShortDesc.setText(book.getShortDescription());
        tvLongDesc.setText(book.getLongDescription());
        tvCount.setText(book.getCount()+"");
        btnBookUpdate.setOnClickListener(view->{
            updateBook(book);
        });
        tvDelete.setOnClickListener(view->{
            databaseReference.child(book.getId()).removeValue();
            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(),BooksActivity.class));
        });
    }

    public void updateBook(Book book) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.activity_edit_book);
        bottomSheetDialog.show();
        EditText bookName = bottomSheetDialog.findViewById(R.id.bookName);
        EditText etShortDesc = bottomSheetDialog.findViewById(R.id.etShortDesc);
        EditText etLongDesc = bottomSheetDialog.findViewById(R.id.etLongDesc);
        EditText etQuantity = bottomSheetDialog.findViewById(R.id.etQuantity);
        EditText etPublished = bottomSheetDialog.findViewById(R.id.etPublished);
        Button btnUpdateCust = bottomSheetDialog.findViewById(R.id.btnUpdateCust);

        bookName.setText(tvBookName.getText());
        etShortDesc.setText(tvShortDesc.getText());
        etLongDesc.setText(tvLongDesc.getText());
        etQuantity.setText(tvCount.getText());
        etPublished.setText(tvPublishedOn.getText());

        btnUpdateCust.setOnClickListener(view -> {
            String stBookName = bookName.getText().toString();
            String stShortDesc = etShortDesc.getText().toString();
            String sLongDesc = etLongDesc.getText().toString();
            String stPublishedOn = tvPublishedOn.getText().toString();
            int stQuantity = Integer.parseInt(etQuantity.getText().toString());

            if (!stBookName.isEmpty() && !stShortDesc.isEmpty() &&
                    !sLongDesc.isEmpty() && stQuantity > 0) {
                Book bookUpdate = new Book(book.getId(), book.getAuth_id(), stBookName, stShortDesc,
                        sLongDesc, book.getRating(), stPublishedOn, stQuantity, book.getLikes());
                databaseReference = FirebaseDatabase.getInstance().getReference("authors");
                FirebaseDatabase.getInstance().getReference().child("books").child(book.getId()).setValue(bookUpdate)
                        .addOnSuccessListener(command -> {
                            Toast.makeText(this,"Updated!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this,BooksActivity.class));
                        })
                        .addOnFailureListener(command ->
                                Toast.makeText(this, "failed to add book!", Toast.LENGTH_SHORT).show()
                        );
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(this, "invalid details!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
