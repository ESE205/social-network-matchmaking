package com.example.cutetogether.chatFiles;

import android.app.ActionBar;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.example.cutetogether.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChatFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    //message variables
    private String chatid;
    private String senderName;
    private String recieverName;
    private ArrayList<MessageItem> mMessageItems;

    //xml variables
    private EditText mEditText;
    private Button mButton;
    private RecyclerView mRecyclerView;

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

    }

    private void getMessages() {

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
