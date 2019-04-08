package com.example.cutetogether.friendFiles;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cutetogether.R;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

//https://github.com/mitchtabian/Recyclerview/blob/master/RecyclerView/app/src/main/java/codingwithmitch/com/recyclerview/RecyclerViewAdapter.java

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.Viewholder>{
    private static final String TAG = "FriendListAdapter";

    private ArrayList<String> mFriendNames = new ArrayList<>();
    private ArrayList<String> mFriendId = new ArrayList<>();
    private Context mContext;
    private EventListener listener;

    public FriendListAdapter(ArrayList<String> friendNames, ArrayList<String> friendId, Context context, EventListener listener) {
        mFriendNames = friendNames;
        mFriendId = friendId;
        mContext = context;
        this.listener = listener;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_item, viewGroup, false);
        Viewholder holder = new Viewholder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {


        Log.d(TAG, "onBindViewHolder: called");

        //get profile images
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference img = mStorageReference.child("img/image.jpg");

        //load profile images
        Glide.with(mContext)
                .load(img)
                .apply(new RequestOptions().override(300,300))
                .into(viewholder.mImage);
        //set friend name text
        viewholder.mFriendName.setText(mFriendNames.get(i));

        //send friend clicked to fragment
        viewholder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mFriendNames.get(i));
                listener.friendClicked(mFriendId.get(i), mFriendNames.get(i));
            }
        });

    }

    @Override
    public int getItemCount() {
        return mFriendNames.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView mImage;
        TextView mFriendName;
        RelativeLayout parentLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.friend_item_image);
            mFriendName = itemView.findViewById(R.id.friend_item_name);
            parentLayout = itemView.findViewById(R.id.friend_item_layout);
        }
    }

    //interface for communicating with fragment
    public interface EventListener{
        public void friendClicked(String id, String name);
    }
}
