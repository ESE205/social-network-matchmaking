package com.example.cutetogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firestore.v1.Document;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PairingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PairingFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView mPair1;
    private ImageView mPair2;
    private TextView mName1;
    private TextView mName2;
    private ImageButton mConfirm;
    private ImageButton mDeny;

    private ArrayList<String> matchNames1 = new ArrayList<>();
    private ArrayList<String> mathchIds1 = new ArrayList<>();
    private  ArrayList<String> matchNames2 = new ArrayList<>();
    private ArrayList<String> matchIds2 = new ArrayList<>();

    private String userid;
    private String username;

    private String match1;
    private String match2;
    private String matchId1;
    private String matchId2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    private static final String TAG = "PairingFragment";
    private Context mContext;


    public PairingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: Started");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pairing, container, false);

        //initialize layout variables
        mPair1 = view.findViewById(R.id.frag_pairing_pair1);
        mPair2 = view.findViewById(R.id.frag_pairing_pair2);
        mName1 = view.findViewById(R.id.frag_pairing_tv1);
        mName2 = view.findViewById(R.id.frag_pairing_tv2);
        mConfirm = view.findViewById(R.id.frag_pairing_btn_confirm);
        mDeny = view.findViewById(R.id.frag_pairing_btn_deny);

        //set user variables
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPref.getString("name", null);
        userid = user.getUid();

        //set context
        mContext = getActivity().getApplicationContext();

        //get match list, load the page, and set the listeners
        getMatchList(username, userid);

        //add button listeners
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmMatch(match1, match2, matchId1, matchId2);
                if(matchNames1.isEmpty()){
                    getMatchList(username,userid);
                }else{
                    reloadPage(matchNames1.remove(0), matchNames2.remove(0),mathchIds1.remove(0),matchIds2.remove(0));
                }
            }
        });

        mDeny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(matchNames1.isEmpty()){
                    getMatchList(username,userid);
                }else{
                    reloadPage(matchNames1.remove(0), matchNames2.remove(0),mathchIds1.remove(0),matchIds2.remove(0));
                }
            }
        });

        return view;
    }

    //get match list based on username and id
    //Edits the string array list
    //reloads the page
    //temporarily use firebase
    private void getMatchList(String name, String id){
        Log.d(TAG, "getMatchList: stared");
        db.collection("friends").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            Map friendList = documentSnapshot.getData();
                            Set<String> keys = friendList.keySet();
                            for(String key : keys){

                                matchNames1.add((String) friendList.get(key));
                                matchNames2.add((String) friendList.get(key));
                                mathchIds1.add(key);
                                matchIds2.add(key);
                            }

                            reloadPage(matchNames1.remove(0), matchNames2.remove(0),mathchIds1.remove(0),matchIds2.remove(0));


                        }else{
                            Log.d(TAG, "Couldn't get list of matches. You aint got no friends");
                            Toast.makeText(mContext, "Couldn't get list of matches. You aint got no friends", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Couldn't get list of matches");
                        Toast.makeText(mContext, "Couldn't get list of matches", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    //reload the page
    //load the images
    // set the match variables
    private void reloadPage(String name1, String name2, String id1, String id2){
        Log.d(TAG, "reloadPage: started");
        StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();
        StorageReference img1 = mStorageReference.child("img/image.jpg");
        StorageReference img2 = mStorageReference.child("img/image.jpg");
        Glide.with(mContext)
                .load(img1)
                .apply(new RequestOptions().override(300,300))
                .into(mPair1);
        Glide.with(mContext)
                .load(img2)
                .apply(new RequestOptions().override(300,300))
                .into(mPair2);

        match1=name1;
        match2=name2;
        matchId1=id1;
        matchId2=id2;

        mName1.setText(name1);
        mName2.setText(name2);

    }

    //send confirm match data to database
    private void confirmMatch(String name1, String name2, String id1, String id2){
        //firebase var
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        //create match info for both
        Map<String, Object> info1 = new HashMap<>();
        Map<String, Object> nestedData1 = new HashMap<>();
        nestedData1.put("name", name1);
        nestedData1.put("status", "pending");
        info1.put(id1, nestedData1);

        //second match data
        Map<String, Object> info2 = new HashMap<>();
        Map<String, Object> nestedData2 = new HashMap<>();
        nestedData2.put("name", name2);
        nestedData2.put("status", "pending");
        info2.put(id2, nestedData2);

        //insert friend request object for sender
        db.collection("match").document(id2)
                .set(info1, SetOptions.merge())
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

        //insert friend request object for reciever
        db.collection("match").document(id1)
                .set(info2, SetOptions.merge())
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

    //send deny match data to database
    // TODO: 3/26/19 actually create this but not for firebase
    private void denyMatch(String name1, String name2, String id1, String id2){

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
