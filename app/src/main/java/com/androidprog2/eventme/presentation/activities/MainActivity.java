package com.androidprog2.eventme.presentation.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.presentation.fragments.ChatListFragment;
import com.androidprog2.eventme.presentation.fragments.CreateEventFragment;
import com.androidprog2.eventme.presentation.fragments.HomeFragment;
import com.androidprog2.eventme.presentation.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private HomeFragment mHomeFragment;
    private CreateEventFragment mCreateEventFragment;
    private ChatListFragment mChatListFragment;
    private ProfileFragment mProfileFragment;

    private BottomNavigationView mBottomNavigationView;
    private int mNavViewLastPosition;

    private FragmentTransaction mFragmentTransaction;


    private final int HOME_POSITION = 1;
    private final int ADD_POSITION = 2;
    private final int CHAT_POSITION = 3;
    private final int PROFILE_POSITION = 4;

    private final int HOME_BTN_ID = R.id.navigation_menu_home;
    private final int ADD_BTN_ID = R.id.navigation_menu_add;
    private final int CHAT_BTN_ID = R.id.navigation_menu_chat;
    private final int PROFILE_BTN_ID = R.id.navigation_menu_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        mBottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation_view);
        mNavViewLastPosition = 1;

        mHomeFragment = new HomeFragment();
        mCreateEventFragment = new CreateEventFragment();
        mChatListFragment = new ChatListFragment();
        mProfileFragment = new ProfileFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, mHomeFragment, mHomeFragment.getTag())
                .add(R.id.fragment, mCreateEventFragment, mCreateEventFragment.getTag())
                .add(R.id.fragment, mChatListFragment, mChatListFragment.getTag())
                .add(R.id.fragment, mProfileFragment, mProfileFragment.getTag())
                .hide(mCreateEventFragment)
                .hide(mChatListFragment)
                .hide(mProfileFragment)
                .commit();

        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case HOME_BTN_ID:
                    switchFragment(HOME_POSITION, mHomeFragment);
                    break;
                case ADD_BTN_ID:
                    switchFragment(ADD_POSITION, mCreateEventFragment);
                    break;
                case CHAT_BTN_ID:
                    switchFragment(CHAT_POSITION, mChatListFragment);
                    break;
                case PROFILE_BTN_ID:
                    switchFragment(PROFILE_POSITION, mProfileFragment);
                    break;
            }
            return true;
        });
    }

    private void switchFragment(int clickedPosition, Fragment fragment) {
        if (clickedPosition != mNavViewLastPosition) {
            mFragmentTransaction = getSupportFragmentManager().beginTransaction();

            if (clickedPosition > mNavViewLastPosition)
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            else
                mFragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

            mFragmentTransaction
                    .hide(mHomeFragment)
                    .hide(mCreateEventFragment)
                    .hide(mChatListFragment)
                    .hide(mProfileFragment);

            mNavViewLastPosition = clickedPosition;
            mFragmentTransaction
                    .show(fragment)
                    .commit();
        }
    }
}