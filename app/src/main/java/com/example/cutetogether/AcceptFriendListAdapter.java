package com.example.cutetogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class AcceptFriendListAdapter extends RecyclerView.Adapter<AcceptFriendListAdapter.Viewholder>{

    private static final String TAG = "AcceptFriendListAdapter";

    private ArrayList<String> mFriendNames = new ArrayList<>();
    private ArrayList<String> mFriendIDs = new ArrayList<>();
    private ArrayList<String> mFriendID;
    private Context mContext;

    public AcceptFriendListAdapter(ArrayList<String> friendNames, ArrayList<String> friendIDs, Context context) {
        mFriendNames = friendNames;
        mFriendIDs = friendIDs;
        mContext = context;


    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_accept_item, viewGroup, false);
        AcceptFriendListAdapter.Viewholder holder = new AcceptFriendListAdapter.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference img = mStorageReference.child("img/image.jpg");

        Glide.with(mContext)
                .load(img)
                .apply(new RequestOptions().override(300,300))
                .into(viewholder.mImage);

        viewholder.mFriendName.setText(mFriendNames.get(i));

        // TODO: 3/18/19 add on clicklister functionality
        viewholder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mFriendNames.get(i));

                //Toast.makeText(mContext, "Yea" + mFriendNames.get(i), Toast.LENGTH_SHORT).show();
            }
        });

        viewholder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String senderName = sharedPref.getString("name", null);
                if(senderName != null){
                    //sendFriendRequest(mFriendNames.get(i),mFriendIDs.get(i),user.getUid(),senderName);
                }else{
                    Log.d(TAG, "Friend add failed. Name is not shared in shared preference");
                    Toast.makeText(mContext, "Add Friend Failed", Toast.LENGTH_SHORT).show();
                }

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
        ImageButton mAdd;
        ImageButton mDeny;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.friend_accept_item_image);
            mFriendName = itemView.findViewById(R.id.friend_accept_item_name);
            parentLayout = itemView.findViewById(R.id.friend_accept_item_layout);
            mAdd = itemView.findViewById(R.id.friend_accept_item_btn);
            mDeny = itemView.findViewById(R.id.friend_reject_item_btn);

        }
    }
}