package com.example.cutetogether;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenu;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;


//https://www.simplifiedcoding.net/bottom-navigation-android-example/

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener,
        ChatFragment.OnFragmentInteractionListener, MatchFragment.OnFragmentInteractionListener,
        FriendFragment.OnFragmentInteractionListener, AddFriendFragment.OnFragmentInteractionListener {

    private BottomNavigationView mBottomNavigationView;
    private FrameLayout mFrameContainer;

    ProfileFragment profileFragment;
    ChatFragment chatFragment;
    MatchFragment matchFragment;
    FriendFragment friendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomNavigationView = findViewById(R.id.main_activity_bottom_nav);
        mFrameContainer = findViewById(R.id.main_activity_frame);

        //mBottomNavigationView.setOnNavigationItemReselectedListener(this);
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);

        profileFragment = new ProfileFragment();
        chatFragment = new ChatFragment();
        matchFragment = new MatchFragment();
        friendFragment = new FriendFragment();

        loadFragment(profileFragment);

    }

    private void loadFragment(Fragment fragment){
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
                fragment = chatFragment;
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

        loadFragment(fragment);
        return true;
    }
}
