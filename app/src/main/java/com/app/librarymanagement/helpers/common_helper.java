package com.app.librarymanagement.helpers;

import static android.content.ContentValues.TAG;

import android.util.Log;

import com.app.librarymanagement.models.Author;
import com.app.librarymanagement.models.Book;
import com.app.librarymanagement.models.BookRequest;
import com.app.librarymanagement.models.MyShelfBook;
import com.app.librarymanagement.models.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class common_helper {
    public static boolean getAdminLogin(String email, String password){
        return email.equals("admin@gmail.com") && password.equals("admin123");
    }


    public static List<BookRequest> getRequestedBooksData(){
        List<BookRequest> list = new ArrayList<>();
        list.add(new BookRequest("1",
                "1",
                "1",
                "The Great Gatsby",
                "F. James",
                "05-03-2023",
                "approved")
        );
        list.add(new BookRequest("2",
                "2",
                "2",
                "One Hundred Years of Solitude",
                "Heera Khushi",
                "20-03-2023",
                "approved")
        );
        return list;
    }


    public static List<BookRequest> getOverdueBooksData() {
        List<BookRequest> list = getRequestedBooksData();
        List<BookRequest> list_overdue = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            String requestedDate = list.get(i).getRequestedDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date strDate = null;
            try {
                strDate = sdf.parse(requestedDate);
                Date due_date = addDay(strDate, 15);
                if (new Date().after(due_date)) {
                    //due true
                    list_overdue.add(list.get(i));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return list_overdue;
    }

    public static Date addDay(Date date, int i) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, i);
        return cal.getTime();
    }

    public static List<Author> getAuthorsData(DatabaseReference databaseReference) {
        List<Author> list = new ArrayList<>();
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String id = ds.child("id").getValue(String.class);
                        String authName = ds.child("name").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String gender = ds.child("gender").getValue(String.class);
                        Integer age = ds.child("age").getValue(Integer.class);
                        list.add(new Author(id,authName,rating,age,gender));
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return list;
    }

    public static Author getAuthorsDetails(String id) {
        DatabaseReference mDatabase;
        FirebaseAuth mAuth;
        mDatabase = FirebaseDatabase.getInstance().getReference();
        List<Author> list = getAuthorsData(mDatabase);
        Author author = new Author();
        for (int i = 0; i < list.size(); i++) {
            if(list.get(i).getId().equals(String.valueOf(id))) author = list.get(i);
        }
        return author;
    }

    public static List<User> getUsersData() {
        List<User> list = new ArrayList<>();
        list.add(new User("1",
                "F. James",
                "6/asd jhaiushda",
                "0123456789",
                "james@gmail.com",
                25,
                "male")
        );
        list.add(new User("2",
                "Heera Khushi",
                "1/F12 asasa",
                "1576543210",
                "heera@gmail.com",
                35,
                "female")
        );
        return list;
    }

    public static User getUserDetails(String id) {
        User user = new User();
        List<User> list = getUsersData();
            for (int i = 0; i < list.size(); i++) {
                if(list.get(i).getId().equals(id))  user = list.get(i);
            }
        return user;
    }
}
