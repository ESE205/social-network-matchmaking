package com.example.cutetogether;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class friendActivity extends AppCompatActivity {

    EditText mSearch;
    RecyclerView mFriendList;
    RecyclerView mAddFriendList;
    private static final String TAG = "friendActivity";
    ArrayList<String> friends = new ArrayList<String>();
    ArrayList<String> img_urls = new ArrayList<String>();
    ArrayList<String> nonfriends = new ArrayList<String>();
    ArrayList<String> nfid = new ArrayList<String>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Log.d(TAG, "onCreate: started");

        mSearch = findViewById(R.id.edittext_friend);
        mFriendList = findViewById(R.id.recycler_view_friend);
        mAddFriendList = findViewById(R.id.friend_rec_add);

        db.collection("users").document(user.getUid()).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(DocumentSnapshot document : task.getResult().getDocuments()){
                        friends.add(document.getString("name"));
                    }

                    Log.d(TAG, "onComplete: get friends ");
                } else{
                    Log.d(TAG, "Error getting friends");
                }
            }
        });

        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "getting user list " + task.getResult().getDocuments().toString());
                    for(DocumentSnapshot document : task.getResult().getDocuments()){
                        if(!friends.contains(document.getId())){
                            nonfriends.add(document.getString("name"));
                            nfid.add(document.getId());
                        }

                    }

                    Log.d(TAG, "onComplete: get friends " + nonfriends.toString());
                    initRecyclerView();
                } else{
                    Log.d(TAG, "Error getting friends");
                }
            }
        });

        //initRecyclerView();



    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        //FriendListAdapter adapter = new FriendListAdapter(friends, img_urls, this);
        //AddFriendListAdapter adapter2 = new AddFriendListAdapter(nonfriends, img_urls, nfid, this);
        //mFriendList.setAdapter(adapter);
        //mAddFriendList.setAdapter(adapter2);
        mAddFriendList.setLayoutManager(new LinearLayoutManager(this));
        mFriendList.setLayoutManager(new LinearLayoutManager(this));


    }
}
