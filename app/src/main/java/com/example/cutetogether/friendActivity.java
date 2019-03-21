package com.example.cutetogether;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

public class friendActivity extends AppCompatActivity {

    EditText mSearch;
    RecyclerView mFriendList;
    private static final String TAG = "friendActivity";
    ArrayList<String> friends;
    ArrayList<String> img_urls;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        Log.d(TAG, "onCreate: started");

        mSearch = findViewById(R.id.edittext_friend);
        mFriendList = findViewById(R.id.recycler_view_friend);

        initRecyclerView();

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        FriendListAdapter adapter = new FriendListAdapter(friends, img_urls, this);


    }
}
