package com.example.cutetogether;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileActivity extends AppCompatActivity {

    //https://stackoverflow.com/questions/43826927/firebase-storage-and-android-images
    //https://firebase.google.com/docs/firestore/query-data/get-data
    //https://github.com/firebase/FirebaseUI-Android/blob/master/storage/README.md

    private static final String TAG = "ProfileActivity";

    private TextView mName;
    private TextView mAge;
    private TextView mCity;
    private TextView mAbout;
    private TextView mGender;
    private ImageView mImageView;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mName = findViewById(R.id.profile_textview_name);
        mAge = findViewById(R.id.profile_tv_user_age);
        mCity = findViewById(R.id.profile_tv_user_city);
        mAbout = findViewById(R.id.profile_tv_user_about);
        mGender = findViewById(R.id.profile_tv_user_gender);
        mImageView = findViewById(R.id.profile_im);

        final StorageReference img = mStorageReference.child("img/image.jpg");

        String uid = user.getUid();


        CollectionReference users = db.collection("users");
        DocumentReference mUser = users.document(uid);

        mUser.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    DocumentSnapshot document = task.getResult();

                    //if sucessful set the fields
                    mName.setText(document.getString("name"));
                    mAge.setText(document.getString("age"));
                    mCity.setText(document.getString("city"));
                    mGender.setText(document.getString("gender"));
                    mAbout.setText(document.getString("about"));


                    Glide.with(getApplicationContext())
                            .load(img)
                            .into(mImageView);




                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }
}
