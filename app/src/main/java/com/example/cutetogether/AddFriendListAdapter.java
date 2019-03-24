package com.example.cutetogether;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddFriendListAdapter extends RecyclerView.Adapter<AddFriendListAdapter.Viewholder>{

    private static final String TAG = "AddFriendListAdapter";

    private ArrayList<String> mFriendNames = new ArrayList<>();
    private ArrayList<String> mImages = new ArrayList<>();
    private ArrayList<String> mFriendID;
    private Context mContext;

    public AddFriendListAdapter(ArrayList<String> friendNames, ArrayList<String> images, ArrayList<String> FriendID, Context context) {
        mFriendNames = friendNames;
        mImages = images;
        mContext = context;
        mFriendID = FriendID;

    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_friend_add_item, viewGroup, false);
        AddFriendListAdapter.Viewholder holder = new AddFriendListAdapter.Viewholder(view);
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
                Log.d(TAG, "onClick: clicked on add friend button");
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                final Map<String, Object> friendinfo = new HashMap<>();

                friendinfo.put("name", mFriendNames.get(i));
                db.collection("users").document(user.getUid()).collection("friends").document(mFriendID.get(i))
                        .set(friendinfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: Document sucessfully written");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: Error writing document");
                            }
                        });
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
        Button mAdd;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.friend_add_item_image);
            mFriendName = itemView.findViewById(R.id.friend_add_item_name);
            parentLayout = itemView.findViewById(R.id.friend_add_item_layout);
            mAdd = itemView.findViewById(R.id.friend_add_item_btn);

        }
    }
}
