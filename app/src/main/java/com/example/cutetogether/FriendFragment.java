package com.example.cutetogether;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FriendFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class FriendFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FriendTabAdapter mFriendTabAdapter;

    private static final String TAG = "FriendFragment";

    public FriendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: Started");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView: Started");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend, container, false);

        //initialzie variables
        mTabLayout = view.findViewById(R.id.frag_friend_tablayout);
        mViewPager = view.findViewById(R.id.frag_friend_vp);

        //adjust height of viewpager based on the navigation bar size
        //assume navbar will be about 56dp if info not set in shared preferences
        ViewGroup.LayoutParams params = mViewPager.getLayoutParams();
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        int navHeight = sharedPref.getInt("navHeight", 56);
        params.height=mViewPager.getHeight()-navHeight;
        mViewPager.setLayoutParams(params);

        //add fragments to tab adapter
        mFriendTabAdapter = new FriendTabAdapter(getFragmentManager());
        mFriendTabAdapter.addFragment(new FriendListFragment(), "Friends");
        mFriendTabAdapter.addFragment(new AddFriendFragment(), "Manage");

        //set up view pager with adapter
        mViewPager.setAdapter(mFriendTabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

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
}
