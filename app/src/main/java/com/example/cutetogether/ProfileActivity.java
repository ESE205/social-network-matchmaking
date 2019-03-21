package com.example.cutetogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    TextView mName;
    TextView mAge;
    TextView mCity;
    TextView mAbout;
    TextView mGender;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser user = mAuth.getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = findViewById(R.id.profile_textview_name);
        mAge = findViewById(R.id.profile_tv_user_age);
        mCity = findViewById(R.id.profile_tv_user_city);
        mAbout = findViewById(R.id.profile_tv_user_about);
        mGender = findViewById(R.id.profile_tv_user_gender);

        String uid = user.getUid();


        CollectionReference users = db.collection("users");
        DocumentReference mUser = users.document(uid);

    }
}
