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
import com.google.firebase.firestore.FieldValue;
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

public class AcceptFriendListAdapter extends RecyclerView.Adapter<AcceptFriendListAdapter.Viewholder>{

    private static final String TAG = "AcceptFriendListAdapter";

    private ArrayList<String> mFriendNames = new ArrayList<>();
    private ArrayList<String> mFriendIDs = new ArrayList<>();
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        final String userName = sharedPref.getString("name", null);

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
                if(userName != null){
                    acceptFriendRequest(mFriendNames.get(i),mFriendIDs.get(i),user.getUid(),userName);
                    Toast.makeText(mContext, "Accepted " + mFriendNames.get(i), Toast.LENGTH_SHORT).show();
                    mFriendNames.remove(i);
                    mFriendIDs.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, mFriendNames.size());
                }else{
                    Log.d(TAG, "Friend add failed. Name is not shared in shared preference");
                    Toast.makeText(mContext, "Add Friend Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewholder.mDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName != null){
                    denyFriendRequest(mFriendNames.get(i),mFriendIDs.get(i),user.getUid(),userName);
                    Toast.makeText(mContext, "Denied " + mFriendNames.get(i), Toast.LENGTH_SHORT).show();
                    mFriendNames.remove(i);
                    mFriendIDs.remove(i);
                    notifyItemRemoved(i);
                    notifyItemRangeChanged(i, mFriendNames.size());
                }else{
                    Log.d(TAG, "Friend deny failed. Name is not shared in shared preference");
                    Toast.makeText(mContext, "deny Friend Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void acceptFriendRequest(String name, String id, String acceptID, String acceptName){

        //firebase var
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //create two friend objects
        Map<String, Object> friendinfo = new HashMap<>();
        friendinfo.put(acceptID,acceptName);

        Map<String, Object> friendinfo2 = new HashMap<>();
        friendinfo2.put(id,name);

        db.collection("friends").document(id)
                .set(friendinfo, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                        //Toast.makeText(mContext, "Friend Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                        //Toast.makeText(mContext, "Friend Not Added", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("friends").document(acceptID)
                .set(friendinfo2, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: ");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: ");
                    }
                });

        User user = new User(acceptName, acceptID);
        User user2 = new User(name, id);
        ArrayList<User> data = new ArrayList<>();
        data.add(user);
        data.add(user2);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<String> call = service.acceptFriend(data);

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

    private void denyFriendRequest(String name, String id, String denyId, String denyName){

        User user = new User(denyName, denyId);
        User user2 = new User(name, id);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        ArrayList<User> data = new ArrayList<>();
        data.add(user);
        data.add(user2);
        Call<String> call = service.denyFriend(data);

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
            mImage = itemView.findViewById(R.id.match_accept_item_image);
            mFriendName = itemView.findViewById(R.id.match_accept_item_name);
            parentLayout = itemView.findViewById(R.id.friend_accept_item_layout);
            mAdd = itemView.findViewById(R.id.match_accept_item_btn);
            mDeny = itemView.findViewById(R.id.friend_reject_item_btn);

        }
    }
}
