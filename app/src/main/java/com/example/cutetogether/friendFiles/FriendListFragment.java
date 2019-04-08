package com.example.cutetogether.friendFiles;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cutetogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class FriendListFragment extends Fragment implements FriendListAdapter.EventListener{

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "FriendListFragment";

    //variables
    private RecyclerView mRecyclerView;
    private FriendListAdapter mFriendListAdapter;
    private ArrayList<String> friendNames = new ArrayList<>();
    private ArrayList<String> friendIds = new ArrayList<>();

    //firebase variables
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    public FriendListFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);

        //assign views
        mRecyclerView = view.findViewById(R.id.frag_friend_list_recycle);

        //call method to get friends. Recycler view initializes inside this method
        getFriendList();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void friendClicked(String id, String name) {
        Toast.makeText(getContext(), name + " clicked", Toast.LENGTH_SHORT).show();
    }


    public interface OnFragmentInteractionListener {
        //public void displayProfile(String id);
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        Context context = getContext();
        mFriendListAdapter = new FriendListAdapter(friendNames, friendIds, context, this);
        mRecyclerView.setAdapter(mFriendListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getFriendList(){
        //get friend doc of user from firestore
        db.collection("friends").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: get friends sucess");
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d(TAG, "Friend Document Found");
                        //get data and key values
                        Map<String, Object> data = document.getData();
                        Set<String> keys = data.keySet();
                        //assign data and key values to lists
                        for(String key : keys){
                            friendNames.add(data.get(key).toString());
                            friendIds.add(key);
                        }
                        //start recycler view
                        initRecyclerView();
                    }else{
                        Log.d(TAG, "No Friend Document Found");
                    }
                } else{
                    Log.d(TAG, "onComplete: get friends failed. " + task.getException());
                }
            }
        });
    }
}
