package com.example.cutetogether.matchFiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.cutetogether.Network.GetDataService;
import com.example.cutetogether.Network.MatchObject;
import com.example.cutetogether.Network.RetrofitClientInstance;
import com.example.cutetogether.Network.User;
import com.example.cutetogether.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
    private ArrayList<String> matchStatuses1 = new ArrayList<>();
    private ArrayList<String> matchStatuses2 = new ArrayList<>();
    private String username;
    private String userid;

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
        //set user variables
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = sharedPref.getString("name", null);
        userid = user.getUid();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match_view, container, false);

        //initialize view and context
        mRecyclerView = view.findViewById(R.id.frag_match_list_recycle);
        mContext= getActivity().getApplicationContext();

        //get matchlist and start recycler
        getMatchList(username, userid);

        return view;
    }

    // TODO: 3/27/19 add neo4j matchlist
    private void getMatchList(String name, String id){
        Log.d(TAG, "getMatchList: started");
        User user = new User(name, id);
        GetDataService service = RetrofitClientInstance.getRetrofitInstance(mContext).create(GetDataService.class);
        Call<ArrayList<MatchObject>> call = service.getMatchList(user);

        call.enqueue(new Callback<ArrayList<MatchObject>>() {
            @Override
            public void onResponse(Call<ArrayList<MatchObject>> call, Response<ArrayList<MatchObject>> response) {
                Log.d(TAG, "onResponse: neo response successful" + response.toString());
                for(MatchObject i : response.body()){
                    Log.d(TAG, "onResponse: " + i.getStatus1() + " " + i.getStatus2());
                    matchNames.add(i.getName2());
                    matchIds.add(i.getUserid2());
                    matchStatuses1.add(i.getStatus1());
                    matchStatuses2.add(i.getStatus2());
                }
                initRecyclerView();
            }

            @Override
            public void onFailure(Call<ArrayList<MatchObject>> call, Throwable t) {
                Log.d(TAG, "Adding to Neo4j failed: " + t.toString());
                Toast.makeText(mContext, "Couldn't get list of matches. You aint got no friends or something is wrong with server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: started");
        Context context = getContext();
        mMatchListAdapter = new MatchListAdapter(matchNames, matchIds, matchStatuses2, matchStatuses1, mContext, this);
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
