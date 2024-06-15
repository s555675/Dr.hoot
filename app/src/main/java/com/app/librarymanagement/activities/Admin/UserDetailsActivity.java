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
import com.app.librarymanagement.models.User;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDetailsActivity extends AppCompatActivity {
    Button btnUserDetails;
    String strId;
    TextView tvUserName,tvAge,tvEmail,tvMobile,tvAddress,tvDelete,tvGender;
    private DatabaseReference databaseReference;
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        findViewById(R.id.btnBack).setOnClickListener(view->{
            startActivity(new Intent(this,MyUsersActivity.class));
        });

        tvUserName= findViewById(R.id.tvUserName);
        tvEmail= findViewById(R.id.tvEmail);
        tvAge= findViewById(R.id.tvAge);
        tvMobile= findViewById(R.id.tvMobile);
        tvAddress= findViewById(R.id.tvAddress);
        tvDelete= findViewById(R.id.tvDelete);
        tvGender= findViewById(R.id.tvGender);
        btnUserDetails= findViewById(R.id.btnUserDetails);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            strId= intent.getStringExtra("user_id");
            getUserDetails(strId);
        }
    }

    private void getUserDetails(String strId){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String name = ds.child("name").getValue(String.class);
                        String email = ds.child("email").getValue(String.class);
                        String gender = ds.child("gender").getValue(String.class);
                        int age = ds.child("age").getValue(Integer.class);
                        String tel = ds.child("tel").getValue(String.class);
                        String address = ds.child("address").getValue(String.class);
                        if(strId.equals(id)){
                            user = new User(id,name,address,tel,email,age,gender);
                            setUserDetails(user);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void setUserDetails(User user){
        tvUserName.setText(user.getName());
        tvEmail.setText(user.getEmail());
        tvMobile.setText(user.getTel());
        tvAddress.setText(user.getAddress());
        tvAge.setText(user.getAge()+"");
        tvGender.setText(user.getGender());
//        tvDelete.setOnClickListener(view->{
//            databaseReference.child(user.getId()).removeValue();
//            Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(getApplicationContext(),MyUsersActivity.class));
//        });
        btnUserDetails.setOnClickListener(view->{
            updateUser(user);
        });
    }

    public void updateUser(User user) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.activity_edit_user);
        bottomSheetDialog.show();
        EditText etName = bottomSheetDialog.findViewById(R.id.etName);
        EditText etEmail = bottomSheetDialog.findViewById(R.id.etEmail);
        EditText etPhone = bottomSheetDialog.findViewById(R.id.etPhone);
        EditText etAge = bottomSheetDialog.findViewById(R.id.etAge);
        EditText etGender = bottomSheetDialog.findViewById(R.id.etGender);
        EditText etAddress = bottomSheetDialog.findViewById(R.id.etAddress);
        Button btnUpdate = bottomSheetDialog.findViewById(R.id.btnUpdate);

        etName.setText(tvUserName.getText());
        etEmail.setText(tvEmail.getText());
        etPhone.setText(tvMobile.getText());
        etAge.setText(tvAge.getText());
        etGender.setText(tvGender.getText());
        etAddress.setText(tvAddress.getText());

        btnUpdate.setOnClickListener(view -> {
            String stName = etName.getText().toString();
            String stEmail = etEmail.getText().toString();
            String stPhone = etPhone.getText().toString();
            int stAge = Integer.parseInt(etAge.getText().toString());
            String stGender = etGender.getText().toString();
            String stAddress = etAddress.getText().toString();

            if (!stName.isEmpty() && !stEmail.isEmpty()
                    && !stPhone.isEmpty() && stAge > 0
                    && !stGender.isEmpty() && !stAddress.isEmpty()) {
                User userUpdate = new User(user.getId(),stName,stGender,stPhone,stEmail,stAge,stGender);
                FirebaseDatabase.getInstance().getReference().child("users").child(user.getId()).setValue(userUpdate)
                        .addOnSuccessListener(command -> {
                            Toast.makeText(this,"Updated!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(this,MyUsersActivity.class));
                        })
                        .addOnFailureListener(command ->
                                Toast.makeText(this, "failed to update user!", Toast.LENGTH_SHORT).show()
                        );
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(this, "invalid details!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
