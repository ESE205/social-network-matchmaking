package com.example.cutetogether.chatFiles;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.cutetogether.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChatFragment extends Fragment implements ChatAdapter.EventListener{

    private static final String TAG = "ChatFragment";
    private OnFragmentInteractionListener mListener;
    private boolean recyclerViewNotInitialized = true;

    //message variables
    private String chatid;
    private String senderName;
    private String recieverName;
    private ArrayList<MessageItem> mMessageItems = new ArrayList<>();
    private ChatAdapter mChatAdapter;

    //xml variables
    private EditText mEditText;
    private Button mButton;
    private RecyclerView mRecyclerView;

    //firebase variables
    private FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();

    public ChatFragment() {
        // Required empty public constructor
        String chatid = "";
    }

    public void setChatid(String chatid) {
        this.chatid = chatid;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        //assign views
        mRecyclerView = view.findViewById(R.id.reyclerview_message_list);
        mEditText = view.findViewById(R.id.edittext_chatbox);
        mButton = view.findViewById(R.id.button_chatbox_send);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage(mEditText.getText().toString());
            }
        });

        //get the chat messages and init recycler view
        getMessages();
        return view;
    }

    private void sendMessage(String message) {
        Log.d(TAG, "sendMessage: ");
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        MessageItem messageItem = new MessageItem(message, sharedPref.getString("name", ""),"dsf", sharedPref.getString("id", user.getUid()));
        Map<String, Object> data = new HashMap<>();
        int messageNumber = mMessageItems.size()+1;
        data.put(""+messageNumber, messageItem);
        mFirebaseDatabase.getReference("chat/" + chatid).updateChildren(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "onComplete: Success");
                }else{
                    Log.d(TAG, "onComplete: Failure " + task.getException());
                }
            }
        });
    }

    private void getMessages() {
        DatabaseReference ref = mFirebaseDatabase.getReference("chat/" + chatid);
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "onChildAdded: " + dataSnapshot.toString());

//                for (DataSnapshot message : dataSnapshot.getChildren()){
//                    MessageItem messageItem = message.getValue(MessageItem.class);
//                    mMessageItems.add(messageItem);
//                }

                Log.d(TAG, "onChildAdded: " + dataSnapshot.getKey());
                Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue());
                MessageItem messageItem = new MessageItem();
                messageItem = dataSnapshot.getValue(MessageItem.class);
                Log.d(TAG, "onChildAdded: " + messageItem.getName());
                mMessageItems.add(messageItem);


                initRecyclerView();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void initRecyclerView(){
        if(recyclerViewNotInitialized){
            Log.d(TAG, "initRecyclerView: started " + mMessageItems.size());
            Context context = getContext();
            mChatAdapter = new ChatAdapter(mMessageItems, context);
            mRecyclerView.setAdapter(mChatAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerViewNotInitialized = false;
        }else{
            mChatAdapter.notifyDataSetChanged();
        }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

    }
}
