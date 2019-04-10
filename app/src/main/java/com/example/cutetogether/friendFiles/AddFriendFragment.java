package com.example.cutetogether.friendFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cutetogether.Network.GetDataService;
import com.example.cutetogether.Network.RetrofitClientInstance;
import com.example.cutetogether.Network.User;
import com.example.cutetogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.common.base.Splitter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddFriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddFriendFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    RecyclerView mAddFriendList;
    RecyclerView mAcceptFriendList;
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
        Log.d(TAG, "onCreateView: started");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_friend, container, false);
        view.setNestedScrollingEnabled(true);

        mAddFriendList = view.findViewById(R.id.frag_friend_add_rv1);
        mAcceptFriendList = view.findViewById(R.id.frag_friend_accept_rv1);

        getAcceptFriendList();
        getAddFriendList();

//        db.collection("users").document(user.getUid()).collection("friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    for(DocumentSnapshot document : task.getResult().getDocuments()){
//                        friends.add(document.getString("name"));
//                    }
//
//                    Log.d(TAG, "onComplete: get friends ");
//                } else{
//                    Log.d(TAG, "Error getting friends");
//                }
//            }
//        });
//
//        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if(task.isSuccessful()){
//                    Log.d(TAG, "getting user list " + task.getResult().getDocuments().toString());
//                    for(DocumentSnapshot document : task.getResult().getDocuments()){
//                        if(!friends.contains(document.getId())){
//                            nonfriends.add(document.getString("name"));
//                            nfid.add(document.getId());
//                        }
//
//                    }
//
//                    Log.d(TAG, "onComplete: get friends " + nonfriends.toString());
//                    initRecyclerView();
//                } else{
//                    Log.d(TAG, "Error getting friends");
//                }
//            }
//        });


        return view;

    }

    private void getAddFriendList(){
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "getting user list " + task.getResult().getDocuments().toString());
                    ArrayList<String> friends = new ArrayList<String>();
                    ArrayList<String> ids = new ArrayList<String>();
                    for(DocumentSnapshot document : task.getResult().getDocuments()){
                        if(!friends.contains(document.getId())){
                            friends.add(document.getString("name"));
                            ids.add(document.getId());
                        }
                    }
                    initAddRecyclerView(friends, ids);
                } else{
                    Log.d(TAG, "Error getting friends");
                }
            }
        });
    }

    private void getAcceptFriendList(){

//        db.collection("friendrequests").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                ArrayList<String> friends = new ArrayList<String>();
//                ArrayList<String> ids = new ArrayList<String>();
//                if(task.isSuccessful()){
//                    Log.d(TAG, "onComplete: getaccept friend task successful");
//                    DocumentSnapshot document = task.getResult();
//                    if(document.exists()){
//                        Log.d(TAG, "Friend Requests Document Found");
//                        //get data and key values
//                        Map<String, Object> data = document.getData();
//                        Set<String> keys = data.keySet();
//                        //parse data
//                        Log.d(TAG, "friendrequests data: " + data.toString());
//                        for(String key : keys){
//                            String a = data.get(key).toString();
//                            Map<String, String> value = Splitter.on(",").withKeyValueSeparator("=").split(a.substring(1, a.length()-1));
//                            Log.d(TAG, "data: " + value.get("role"));
//
//                            if(value.get("role").equals("rec")){
//                                Log.d(TAG, "got friend rec " + value.keySet().toString());
//                                Log.d(TAG, "onComplete: " + value.toString());
//                                Log.d(TAG, "onComplete: " + value.get("  name"));
//                                Log.d(TAG, "onComplete: " + value.get(" name"));
//                                Log.d(TAG, "onComplete: " + value.get("name"));
//                                friends.add(value.get(" name"));
//                                ids.add(key);
//                            }
//                        }
//                        //start recycler view
//                        initAcceptRecyclerView(friends, ids);
//                    }else{
//                        Log.d(TAG, "onComplete: ");
//                    }
//                }else{
//                    Log.d(TAG, "Could not get user friendrequest document. " + task.getException());
//                }
//            }
//        });
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        User user1 = new User(sharedPreferences.getString("name", ""), user.getUid());
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(getContext()).create(GetDataService.class);
        Call<ArrayList<User>> call = service.getFriendRequest(user1);

        call.enqueue(new Callback<ArrayList<User>>() {
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
                Log.d(TAG, "onResponse: neo response " + response.body());
                ArrayList<String> names = new ArrayList<String>();
                ArrayList<String> ids = new ArrayList<String>();
                for(User i : response.body()){
                    names.add(i.getName());
                    ids.add(i.getId());
                }
                initAcceptRecyclerView(names, ids);
            }

            @Override
            public void onFailure(Call<ArrayList<User>> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
            }
        });
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
        //AddFriendListAdapter adapter2 = new AddFriendListAdapter(nonfriends, img_urls, nfid, context);
        //mAddFriendList.setAdapter(adapter2);
        //mAddFriendList.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initAddRecyclerView(ArrayList<String> names, ArrayList<String> ids){
        Log.d(TAG, "initAddRecyclerView: started");
        Context context = getContext();
        AddFriendListAdapter adapter = new AddFriendListAdapter(names, ids, context);
        mAddFriendList.setAdapter(adapter);
        mAddFriendList.setLayoutManager(new LinearLayoutManager(context));

    }

    private void initAcceptRecyclerView(ArrayList<String> names, ArrayList<String> ids){
        Log.d(TAG, "initAcceptRecyclerView: started, sending data: " + names.toString());
        Context context = getContext();
        AcceptFriendListAdapter adapter = new AcceptFriendListAdapter(names, ids, context);
        mAcceptFriendList.setAdapter(adapter);
        mAcceptFriendList.setLayoutManager(new LinearLayoutManager(context));
    }
}
