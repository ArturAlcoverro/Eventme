package com.androidprog2.eventme.presentation.activities;

import android.os.Build;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.presentation.fragments.ChatListFragment;
import com.androidprog2.eventme.presentation.fragments.CreateEventFragment;
import com.androidprog2.eventme.presentation.fragments.HomeFragment;
import com.androidprog2.eventme.presentation.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private HomeFragment mHomeFragment;
    private CreateEventFragment mCreateEventFragment;
    private ChatListFragment mChatListFragment;
    private ProfileFragment mProfileFragment;

    private FrameLayout mFrame;
    private BottomNavigationView mBottomNavigationView;
    private int mNavViewLastPosition;
    private Fragment mNavViewLastFragment;

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
        mFrame = (FrameLayout) findViewById(R.id.fragment);
        loadFragments();

        keyboardListener();
    }

    private void loadFragments() {
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

        mNavViewLastFragment = mHomeFragment;

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
                    mChatListFragment.updateData();
                    break;
                case PROFILE_BTN_ID:
                    switchFragment(PROFILE_POSITION, mProfileFragment);
                    mProfileFragment.updateData();
                    break;
            }
            return true;
        });
    }

    private void switchFragment(int clickedPosition, Fragment fragment) {
        FragmentTransaction transaction;

        if (clickedPosition != mNavViewLastPosition) {
            transaction = getSupportFragmentManager().beginTransaction();

            if (clickedPosition > mNavViewLastPosition)
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
            else
                transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

            transaction.hide(mNavViewLastFragment);

            mNavViewLastPosition = clickedPosition;
            mNavViewLastFragment = fragment;

            transaction
                    //.detach(fragment)
                    //.attach(fragment)
                    .show(fragment)
                    .commit();
        }
    }

    private void keyboardListener() {
        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                ViewGroup.MarginLayoutParams margins;
                int marginBottom = getResources().getDimensionPixelSize(R.dimen.navigation_space);
                if (isOpen) {
                    mBottomNavigationView.setVisibility(View.GONE);
                    margins = (ViewGroup.MarginLayoutParams) mFrame.getLayoutParams();
                    margins.bottomMargin = -marginBottom;
                } else {
                    mBottomNavigationView.setVisibility(View.VISIBLE);
                    margins = (ViewGroup.MarginLayoutParams) mFrame.getLayoutParams();
                    margins.bottomMargin = 0;
                }
            }
        });
    }
}