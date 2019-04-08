package com.example.cutetogether;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.cutetogether.chatFiles.ChatFragment;
import com.example.cutetogether.chatFiles.ChatListFragment;
import com.example.cutetogether.friendFiles.AddFriendFragment;
import com.example.cutetogether.friendFiles.FriendFragment;
import com.example.cutetogether.matchFiles.MatchFragment;
import com.example.cutetogether.matchFiles.MatchListFragment;
import com.example.cutetogether.matchFiles.PairingFragment;
import com.example.cutetogether.profileFiles.ProfileFragment;


//https://www.simplifiedcoding.net/bottom-navigation-android-example/

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        ChatFragment.OnFragmentInteractionListener, MatchFragment.OnFragmentInteractionListener,
        FriendFragment.OnFragmentInteractionListener, AddFriendFragment.OnFragmentInteractionListener,
        PairingFragment.OnFragmentInteractionListener, MatchListFragment.OnFragmentInteractionListener,
        ChatListFragment.OnFragmentInteractionListener {

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout mFrameContainer;

    //callbacks
    ChatListFragment.OnFragmentInteractionListener mOnChatListFragmentInteractionListener;

    //fragments
    ProfileFragment profileFragment;
    ChatFragment chatFragment;
    MatchFragment matchFragment;
    FriendFragment friendFragment;
    ChatListFragment ChatListFragment;
    ChatFragment ChatFragment;
    Fragment latest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.main_activity_bottom_nav);
        mFrameContainer = findViewById(R.id.main_activity_frame);

        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("navHeight", mBottomNavigationView.getHeight());
        editor.commit();

        //mBottomNavigationView.setOnNavigationItemReselectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        profileFragment = new ProfileFragment();
        chatFragment = new ChatFragment();
        matchFragment = new MatchFragment();
        friendFragment = new FriendFragment();
        ChatListFragment = new ChatListFragment();
        ChatFragment = new ChatFragment();

        //add listeners to fragments
        ChatListFragment.setListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_activity_frame, profileFragment)
                .commit();

        latest = profileFragment;

    }

    private void loadFragment(Fragment fragment, Fragment current){
        latest = fragment;
        if(fragment != null && fragment.isAdded()){
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(current)
                    .show(fragment)
                    .commit();
        }else{
            getSupportFragmentManager()
                    .beginTransaction()
                    .hide(current)
                    .add(R.id.main_activity_frame, fragment)
                    .commit();
        }
    }

    private void replaceFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_activity_frame, fragment)
                    .commit();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Fragment fragment = null;

        switch (menuItem.getItemId()){
            case R.id.navigation_chat:
                fragment = ChatListFragment;
                break;

            case R.id.navigation_friend:
                fragment = friendFragment;
                break;

            case R.id.navigation_match:
                fragment = matchFragment;
                break;

            case R.id.navigation_profile:
                fragment = profileFragment;
                break;
        }

        loadFragment(fragment, latest);
        return true;
    }

    @Override
    public void chatClicked(String chatid) {
        ChatFragment fragment = new ChatFragment();
        fragment.setChatid(chatid);
        loadFragment(fragment,latest);
    }
}
