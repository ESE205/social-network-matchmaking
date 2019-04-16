package com.example.cutetogether.matchFiles;

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
import com.example.cutetogether.Network.MatchObject;
import com.example.cutetogether.Network.RetrofitClientInstance;
import com.example.cutetogether.Network.User;
import com.example.cutetogether.Network.endorseMatchObject;
import com.example.cutetogether.R;
import com.example.cutetogether.chatFiles.ChatItem;
import com.example.cutetogether.friendFiles.AddFriendListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchListAdapter extends RecyclerView.Adapter<MatchListAdapter.Viewholder> {

    private static final String TAG = "MatchListAdapter";
    private ArrayList<String> mMatchNames = new ArrayList<>();
    private ArrayList<String> mMatchIds = new ArrayList<>();
    private ArrayList<String> mStatus = new ArrayList<>();
    private ArrayList<String> mUserStatus = new ArrayList<>();
    private Context mContext;
    private EventListener mListener;
    private String username;
    private String userid;

    public MatchListAdapter (ArrayList<String> namesList, ArrayList<String> idList, ArrayList<String> status, ArrayList<String> userStatus, Context context, EventListener listener){
        mMatchNames = namesList;
        mMatchIds = idList;
        mContext = context;
        mListener = listener;
        mStatus = status;
        mUserStatus = userStatus;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_match_view_accept_item, viewGroup, false);
        MatchListAdapter.Viewholder holder = new MatchListAdapter.Viewholder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, final int i) {
        Log.d(TAG, "onBindViewHolder: called");
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String senderName = sharedPref.getString("name", null);



        username = senderName;
        userid = user.getUid();

        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference img = mStorageReference.child("img/image.jpg");

        Glide.with(mContext)
                .load(img)
                .apply(new RequestOptions().override(300,300))
                .into(viewholder.mImage);

        viewholder.mName.setText(mMatchNames.get(i));

        // TODO: 3/18/19 add on clicklister functionality
        viewholder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on: " + mMatchNames.get(i));
            }
        });


        viewholder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUserStatus.get(i).equals("yes")){
                    try{
                        Toast.makeText(mContext, "Already added", Toast.LENGTH_SHORT).show();
                    }catch(Exception e){
                        Toast.makeText(mContext, "Something went wrong", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "onClick: " + e.toString());
                    }
                }else if(mStatus.get(i).equals("yes")){
                    completeAcceptMatch(username, userid, mMatchNames.get(i), mMatchIds.get(i));
                }else{
                    acceptMatch(username, userid, mMatchNames.get(i), mMatchIds.get(i));
                }
            }
        });

        viewholder.mDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                denyMatch(username, userid, mMatchNames.get(i), mMatchIds.get(i));
            }
        });

    }


    private void acceptMatch(String name1, String id1, String name2, String id2){
        Log.d(TAG, "acceptMatch: ");
        endorseMatchObject data = new endorseMatchObject(name1, id1, name2, id2, "", "");
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<String> call = service.acceptMatch(data);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
                //Toast.makeText(mContext, "Couldn't get list of matches. You aint got no friends or something is wrong with server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void denyMatch(String name1, String id1, String name2, String id2){
        Log.d(TAG, "denyMatch: ");
        endorseMatchObject data = new endorseMatchObject(name1, id1, name2, id2, "", "");
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<String> call = service.denyMatch(data);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
                //Toast.makeText(mContext, "Couldn't get list of matches. You aint got no friends or something is wrong with server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void completeAcceptMatch(String name1, String id1, String name2, String id2){
        Log.d(TAG, "completeAcceptMatch: ");

        FirebaseFirestore mFirebaseStore = FirebaseFirestore.getInstance();
        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();

        endorseMatchObject data = new endorseMatchObject(name1, id1, name2, id2, "", "");
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<String> call = service.completeAcceptMatch(data);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
                //Toast.makeText(mContext, "Couldn't get list of matches. You aint got no friends or something is wrong with server", Toast.LENGTH_SHORT).show();
            }
        });

        //add firebase chat info
        int chatid = (int) (Math.random()*1000000000);
        ChatItem c1 = new ChatItem(name1, ""+chatid, id1);
        ChatItem c2 = new ChatItem(name2, ""+chatid, id2);
        HashMap<String, Object> data1 = new HashMap<>();
        data1.put(id1, c1);
        HashMap<String, Object> data2 = new HashMap<>();
        data2.put(id2, c2);

        mFirebaseStore.collection("chat").document(id1).set(data2, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: added chat info 1/2");
                }else{
                    Log.d(TAG, "onComplete: " + task.getException().toString());
                }
            }
        });

        mFirebaseStore.collection("chat").document(id2).set(data1, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: added chat info 2/2");
                }else{
                    Log.d(TAG, "onComplete: " + task.getException().toString());
                }
            }
        });



    }

    @Override
    public int getItemCount() {
        return mMatchNames.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder{
        ImageView mImage;
        TextView mName;
        RelativeLayout parentLayout;
        ImageButton mAdd;
        ImageButton mDeny;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            mImage = itemView.findViewById(R.id.match_accept_item_image);
            mName = itemView.findViewById(R.id.match_accept_item_name);
            parentLayout = itemView.findViewById(R.id.match_accept_item_layout);
            mAdd = itemView.findViewById(R.id.match_accept_item_btn);
            mDeny = itemView.findViewById(R.id.match_reject_item_btn);

        }
    }

    public interface EventListener {
        public void matchClicked(String id, String name);
    }
}
