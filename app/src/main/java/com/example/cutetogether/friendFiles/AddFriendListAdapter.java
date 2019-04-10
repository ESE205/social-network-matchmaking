package com.example.cutetogether.friendFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.cutetogether.Network.GetDataService;
import com.example.cutetogether.Network.RetrofitClientInstance;
import com.example.cutetogether.Network.User;
import com.example.cutetogether.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddFriendListAdapter extends RecyclerView.Adapter<AddFriendListAdapter.Viewholder>{

    private static final String TAG = "AddFriendListAdapter";

    private ArrayList<String> mFriendNames = new ArrayList<>();
    private ArrayList<String> mFriendIDs = new ArrayList<>();
    private Context mContext;


    public AddFriendListAdapter(ArrayList<String> friendNames, ArrayList<String> friendIDs, Context context) {
        mFriendNames = friendNames;
        mFriendIDs = friendIDs;
        mContext = context;

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
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String senderName = sharedPref.getString("name", null);
                Log.d(TAG, "Shared Preference Name: " + senderName);
                if(senderName != null){
                    sendFriendRequest(mFriendNames.get(i),mFriendIDs.get(i),user.getUid(),senderName);
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

    private void sendFriendRequest(String name, String id, String senderId, String senderName){
        Log.d(TAG, "onClick: clicked on add friend button");

//        //firebase var
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        //create friend request send object
//        Map<String, Object> friendinfo = new HashMap<>();
//        Map<String, Object> nestedData = new HashMap<>();
//        nestedData.put("name", name);
//        nestedData.put("status", "pending");
//        nestedData.put("role", "sender");
//        friendinfo.put(id, nestedData);
//
//        //create friend request req object
//        Map<String, Object> reqfriendinfo = new HashMap<>();
//        Map<String, Object> reqnestedData = new HashMap<>();
//        reqnestedData.put("name", senderName);
//        reqnestedData.put("status", "pending");
//        reqnestedData.put("role", "rec");
//        reqfriendinfo.put(senderId, reqnestedData);
//
//        //insert friend request object for sender
//        db.collection("friendrequests").document(senderId)
//                .set(friendinfo, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: Document sucessfully written");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: Error writing document");
//                    }
//                });
//
//        //insert friend request object for reciever
//        db.collection("friendrequests").document(id)
//                .set(reqfriendinfo, SetOptions.merge())
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: Document sucessfully written");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d(TAG, "onFailure: Error writing document");
//                    }
//                });
        User user = new User(senderName, senderId);
        User user2 = new User(name, id);
        ArrayList<User> data = new ArrayList<>();
        data.add(user);
        data.add(user2);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<String> call = service.addFriend(data);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
            }
        });


    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView mImage;
        TextView mFriendName;
        RelativeLayout parentLayout;
        ImageButton mAdd;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.friend_add_item_image);
            mFriendName = itemView.findViewById(R.id.friend_add_item_name);
            parentLayout = itemView.findViewById(R.id.friend_add_item_layout);
            mAdd = itemView.findViewById(R.id.friend_add_item_btn);

        }
    }
}
