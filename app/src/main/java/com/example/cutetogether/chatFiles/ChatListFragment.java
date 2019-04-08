package com.example.cutetogether.chatFiles;

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

import com.example.cutetogether.R;
import com.example.cutetogether.friendFiles.FriendListAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChatListFragment extends Fragment implements ChatListAdapter.EventListener{

    private OnFragmentInteractionListener mListener;

    //variables
    private RecyclerView mRecyclerView;
    private ChatListAdapter mChatListAdapter;
    private ArrayList<String> chatNames = new ArrayList<>();
    private ArrayList<String> chatIds = new ArrayList<>();
    private ArrayList<String> chatUserIds = new ArrayList<>();
    private ArrayList<ChatItem> mChatItems = new ArrayList<>();

    //firebase variables
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();


    private static final String TAG = "ChatListFragment";

    public ChatListFragment() {
        // Required empty public constructor
    }

    public void setListener(OnFragmentInteractionListener listener) {
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the view layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        //assign the views
        mRecyclerView = view.findViewById(R.id.frag_chat_list_recycle);

        //call the get chat method
        getChatList();
        return view;



    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        Context context = getContext();
        mChatListAdapter = new ChatListAdapter(mChatItems, context, this);
        mRecyclerView.setAdapter(mChatListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void getChatList(){
        //get friend doc of user from firestore
        db.collection("chat").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: get friends sucess");
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Log.d(TAG, "Friend Document Found");
                        document.get("m",ChatItem.class);
                        //get data and key values
                        Map<String, Object> data = document.getData();
                        Set<String> keys = data.keySet();
                        //assign data and key values to lists
                        for(String key : keys){
                            ChatItem chatItem = document.get(key, ChatItem.class);
                            mChatItems.add(chatItem);
                        }
                        Log.d(TAG, "onComplete: " + mChatItems.get(0).getChatid() + " " + mChatItems.get(0).getName());
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

    @Override
    public void chatClicked(String chatid) {
        mListener.chatClicked(chatid);
    }

    public interface OnFragmentInteractionListener {
        public void chatClicked(String chatid);
    }
}
