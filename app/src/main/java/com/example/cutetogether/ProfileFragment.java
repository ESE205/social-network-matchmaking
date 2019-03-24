package com.example.cutetogether;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
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



public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG, "onCreateView: started");

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mName = view.findViewById(R.id.profile_textview_name);
        mAge = view.findViewById(R.id.profile_tv_user_age);
        mCity = view.findViewById(R.id.profile_tv_user_city);
        mAbout = view.findViewById(R.id.profile_tv_user_about);
        mGender = view.findViewById(R.id.profile_tv_user_gender);
        mImageView = view.findViewById(R.id.profile_im);

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


                    Glide.with(getActivity().getApplicationContext())
                            .load(img)
                            .apply(new RequestOptions().override(300,300))
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

        return view;
    }
}
