package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.presentation.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    private ProfileFragment mProfileFragment;
    private int id;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        Intent intent = getIntent();
        id = intent.getIntExtra(ChatActivity.EXTRA_ID, 0);
        Bundle bundle = new Bundle();
        bundle.putInt("EXTRA_ID", id);

        mProfileFragment = new ProfileFragment();
        mProfileFragment.setArguments(bundle);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, mProfileFragment, mProfileFragment.getTag())
                .commit();
    }
}