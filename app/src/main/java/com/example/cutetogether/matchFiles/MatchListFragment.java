package com.example.cutetogether.matchFiles;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cutetogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class MatchListFragment extends Fragment implements MatchListAdapter.EventListener{

    private OnFragmentInteractionListener mListener;
    private static final String TAG = "MatchListFragment";

    //variables
    private RecyclerView mRecyclerView;
    private MatchListAdapter mMatchListAdapter;
    private ArrayList<String> matchNames = new ArrayList<>();
    private ArrayList<String> matchIds = new ArrayList<>();

    //firebase variables
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user = mAuth.getCurrentUser();
    private StorageReference mStorageReference = FirebaseStorage.getInstance().getReference();

    private Context mContext;

    public MatchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: started");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_view, container, false);

        //initialize view and context
        mRecyclerView = view.findViewById(R.id.frag_match_list_recycle);
        mContext= getActivity().getApplicationContext();

        //start recycler
        initRecyclerView();

        return view;
    }

    // TODO: 3/27/19 add neo4j matchlist
    private void getMatchList(){

    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        Context context = getContext();
        mMatchListAdapter = new MatchListAdapter(matchNames, matchIds, mContext, this);
        mRecyclerView.setAdapter(mMatchListAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
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

    // TODO: 3/27/19 add confirm match function
    @Override
    public void matchClicked(String id, String name) {

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