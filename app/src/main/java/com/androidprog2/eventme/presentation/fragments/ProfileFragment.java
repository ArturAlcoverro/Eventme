package com.androidprog2.eventme.presentation.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.activities.EditProfileActivity;
import com.androidprog2.eventme.presentation.activities.UserEventsActivity;
import com.androidprog2.eventme.presentation.activities.UserFriendsActivity;
import com.androidprog2.eventme.presentation.adapters.TimelineAdapter;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";

    private ImageButton editProfileBtn;
    private ImageButton chatProfileBtn;
    private MaterialButton sendMessageBtn;
    private MaterialButton requestFriendBtn;
    private ImageButton backArrow_btn;
    private LinearLayout linearLayout;
    private ImageView profileImage;
    private TextView profileName;
    private RecyclerView recyclerView;
    private TimelineAdapter adapter;
    private TextView createdNumber;
    private TextView assistanceNumber;
    private TextView friendsNumber;
    private LinearLayout friendsLinear;
    private LinearLayout createdLinear;
    private LinearLayout assistanceLinear;

    private int id;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage = view.findViewById(R.id.imageProfile);
        profileName = view.findViewById(R.id.textView_Name);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        chatProfileBtn = view.findViewById(R.id.chatProfileBtn);
        backArrow_btn = view.findViewById(R.id.arrowLeft);
        linearLayout = view.findViewById(R.id.linearButtons);
        recyclerView = view.findViewById(R.id.recyclerViewTimeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        createdNumber = view.findViewById(R.id.createdStatistics);
        assistanceNumber = view.findViewById(R.id.assistedStatistics);
        friendsNumber = view.findViewById(R.id.friendsStatistics);
        sendMessageBtn = view.findViewById(R.id.sendMessage);
        requestFriendBtn = view.findViewById(R.id.requestFriend);
        friendsLinear = view.findViewById(R.id.friendsLinear);
        createdLinear = view.findViewById(R.id.createdLinear);
        assistanceLinear = view.findViewById(R.id.assistanceLinear);

        updateData();

        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        backArrow_btn.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        chatProfileBtn.setVisibility(View.GONE);

        editProfileBtn.setOnClickListener(v -> { startActivity(intent); });
        chatProfileBtn.setOnClickListener(v -> { startChatActivity(); });
        sendMessageBtn.setOnClickListener(v -> { startChatActivity(); });
        requestFriendBtn.setOnClickListener(v -> { requestFriendShip(); });
        friendsLinear.setOnClickListener(v -> { startFriendActivity(); });
        createdLinear.setOnClickListener(v -> { startCreatedActivity(); });
        assistanceLinear.setOnClickListener(v -> { startAssistanceActivity(); });
        return view;
    }

    private void startAssistanceActivity() {
        Intent intent = new Intent(getContext(), UserEventsActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_NAME, profileName.getText().toString());
        intent.putExtra(EXTRA_TYPE, "EXTRA_ASSISTANCE");

        startActivity(intent);
    }

    private void startCreatedActivity() {
        Intent intent = new Intent(getContext(), UserEventsActivity.class);
        intent.putExtra(EXTRA_ID, id);
        intent.putExtra(EXTRA_NAME, profileName.getText().toString());
        intent.putExtra(EXTRA_TYPE, "EXTRA_CREATED");

        startActivity(intent);
    }

    private void startFriendActivity() {
        Intent intent = new Intent(getContext(), UserFriendsActivity.class);
        intent.putExtra(EXTRA_ID, id);

        startActivity(intent);
    }

    private void requestFriendShip() {

    }

    private void startChatActivity() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(){
        id = CallSingelton.getUserId();
        setProfileInformation(id);
        setStatistics(id);
        loadTimeline();
    }

    private void setStatistics(int id) {
        getCreatedStatistics(id);
        getFriendsStatistics(id);
    }

    private void getFriendsStatistics(int id) {
        CallSingelton
                .getInstance()
                .getUserFriends(id, new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<User> friends = (List<User>) response.body();
                                friendsNumber.setText(String.valueOf(friends.size()));
                            }
                        } else {
                            try {
                                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getCreatedStatistics(int id) {
        CallSingelton
                .getInstance()
                .getUserEventsCreated(id, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> created = response.body();
                                createdNumber.setText(String.valueOf(created.size()));
                                System.out.println(created.size() + " vaja vaja pero que passa");
                            }
                        } else {
                            try {
                                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setProfileInformation(int id) {
        CallSingelton
                .getInstance()
                .getProfileUser(id, new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<User> user = (List<User>) response.body();
                                profileName.setText(user.get(0).getFull_name());
                                setImage(user.get(0).getImage());
                            }
                        } else {
                            try {
                                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {
                        System.out.println("entro en el failure q guai");
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void loadTimeline() {
        CallSingelton
                .getInstance()
                .getUserAssistances(CallSingelton.getUserId(), new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> assistances = response.body();

                                if (!assistances.isEmpty()) {
                                    assistances.removeIf(event -> (event.getStartDate() == null));
                                    Collections.sort(assistances, (e1, e2) -> e2.getStartDate().compareTo(e1.getStartDate()));
                                    adapter = new TimelineAdapter(assistances, getContext());
                                    recyclerView.setAdapter(adapter);
                                }
                            }
                        } else {
                            try {
                                Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Event>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void setImage(String image) {
        String url;
        if(image.startsWith("http")){
            url = image;
        }else{
            url = "http://puigmal.salle.url.edu/img/" + image;
        }
        ImageLoader imageLoader = VolleySingleton.getInstance(getContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null) {
                    profileImage.setImageBitmap(response.getBitmap());
                }
            }

            public void onErrorResponse(VolleyError error) {
                profileImage.setImageResource(R.drawable.avatar_profile);
            }
        });
    }

    private void deleteToken(){

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CallSingelton.TOKEN, MODE_PRIVATE);
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}