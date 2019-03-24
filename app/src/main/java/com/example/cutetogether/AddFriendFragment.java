package com.example.cutetogether;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddFriendFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView mAddFriendList;
    ArrayList<String> friends = new ArrayList<String>();
    ArrayList<String> img_urls = new ArrayList<String>();
    ArrayList<String> nonfriends = new ArrayList<String>();
    ArrayList<String> nfid = new ArrayList<String>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    private static final String TAG = "AddFriendFragment";

    public AddFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);


        mAddFriendList = view.findViewById(R.id.frag_friend_add_rv1);

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


        return view;

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {

    }

    private void initRecyclerView() {
        Log.d(TAG, "initRecyclerView: started");

        Context context = getContext();
        AddFriendListAdapter adapter2 = new AddFriendListAdapter(nonfriends, img_urls, nfid, context);
        mAddFriendList.setAdapter(adapter2);
        mAddFriendList.setLayoutManager(new LinearLayoutManager(context));


    }
}
