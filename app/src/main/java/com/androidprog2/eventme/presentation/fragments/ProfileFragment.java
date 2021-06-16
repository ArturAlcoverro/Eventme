package com.androidprog2.eventme.presentation.fragments;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.androidprog2.eventme.presentation.activities.ChatActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    public static final String EXTRA_ID = "EXTRA_ID";
    public static final String ACTIVITY = "ACTIVITY";
    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String EXTRA_TYPE = "EXTRA_TYPE";
    private static final String EXTRA_LAST_NAME = "EXTRA_LAST_NAME";
    private static final String EXTRA_EMAIL = "EXTRA_EMAIL";
    private static final String EXTRA_IMAGE = "EXTRA_IMAGE";

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
    private TextView pendingRequests;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ID = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int id;
    private boolean isActivity;
    //private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param id Parameter 1.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(int id) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        System.out.println(id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            id = getArguments().getInt(ARG_ID);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        id = this.getArguments().getInt("EXTRA_ID");
        isActivity = this.getArguments().getBoolean(ACTIVITY, false);

        Log.d("--ACTIVITY--", isActivity? "---TRUE---":"---FALSE---");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage = view.findViewById(R.id.imageProfile);
        profileName = view.findViewById(R.id.textView_Name);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        chatProfileBtn = view.findViewById(R.id.chatProfileBtn);
        pendingRequests = view.findViewById(R.id.pendingRequests);
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

        if (id == -1) {
            backArrow_btn.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
            chatProfileBtn.setVisibility(View.GONE);
            updateData();
        } else {
            updateData(id);
            checkisFriendOrNot();
        }

        if (isActivity) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)recyclerView.getLayoutParams();
            params.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            recyclerView.setLayoutParams(params);
        }

        editProfileBtn.setOnClickListener(v -> {
            startUpdateActivity();
        });
        chatProfileBtn.setOnClickListener(v -> {
            startChatActivity();
        });
        sendMessageBtn.setOnClickListener(v -> {
            startChatActivity();
        });
        requestFriendBtn.setOnClickListener(v -> {
            requestFriendShip();
        });
        friendsLinear.setOnClickListener(v -> {
            startFriendActivity();
        });
        createdLinear.setOnClickListener(v -> {
            startCreatedActivity();
        });
        assistanceLinear.setOnClickListener(v -> {
            startAssistanceActivity();
        });
        return view;
    }

    private void startUpdateActivity() {
        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        intent.putExtra(EXTRA_ID, id);

        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkisFriendOrNot() {
        CallSingelton
                .getInstance()
                .getUserFriends(CallSingelton.getUserId(), new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<User> friends = (List<User>) response.body();
                                boolean isFriend = false;
                                for (User friend : friends) {
                                    if (friend.getId() == id) {
                                        isFriend = true;
                                    }
                                }
                                if (isFriend) {
                                    chatProfileBtn.setVisibility(View.VISIBLE);
                                    editProfileBtn.setVisibility(View.GONE);
                                    linearLayout.setVisibility(View.GONE);
                                } else {
                                    editProfileBtn.setVisibility(View.GONE);
                                    chatProfileBtn.setVisibility(View.GONE);
                                }
                                backArrow_btn.setOnClickListener(v -> {
                                    getActivity().finish();
                                });
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
        CallSingelton
                .getInstance()
                .requestFriendShip(id, new Callback<Response<Void>>() {
                    @Override
                    public void onResponse(Call<Response<Void>> call, Response<Response<Void>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 201) {
                                System.out.println("Requested FriendShip");
                                requestFriendBtn.setText(R.string.pending_friendship);
                            }
                        } else {
                            if (response.code() == 400) {
                                requestFriendBtn.setText(R.string.pending_friendship);
                            } else {
                                try {
                                    Toast.makeText(getContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Response<Void>> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void startChatActivity() {
        CallSingelton
                .getInstance()
                .getProfileUser(id, new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<User> user = (List<User>) response.body();
                                Intent intent = new Intent(getContext(), ChatActivity.class);
                                intent.putExtra(EXTRA_ID, user.get(0).getId());
                                intent.putExtra(EXTRA_NAME, user.get(0).getName());
                                intent.putExtra(EXTRA_LAST_NAME, user.get(0).getLast_name());
                                intent.putExtra(EXTRA_EMAIL, user.get(0).getEmail());
                                intent.putExtra(EXTRA_IMAGE, user.get(0).getImage());

                                getContext().startActivity(intent);
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData() {
        id = CallSingelton.getUserId();
        setProfileInformation(id);
        setStatistics(id);
        loadTimeline(id);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateData(int id) {
        setProfileInformation(id);
        setStatistics(id);
        loadTimeline(id);
    }

    private void setStatistics(int id) {
        getCreatedStatistics(id);
        getFriendsStatistics(id);
    }

    private void getFriendsStatistics(int id) {
        CallSingelton
                .getInstance()
                .getUserFriends(id, new Callback<List<User>>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<User> friends = (List<User>) response.body();
                                friendsNumber.setText(String.valueOf(friends.size()));
                                if (id == CallSingelton.getUserId()) {
                                    CallSingelton
                                            .getInstance()
                                            .getUserFriendsRequests(new Callback<List<User>>() {
                                                @Override
                                                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                                                    if (response.isSuccessful()) {
                                                        if (response.code() == 200) {
                                                            List<User> requests = response.body();
                                                            if (!requests.isEmpty()) {
                                                                pendingRequests.setText(String.valueOf(requests.size()));
                                                                pendingRequests.setVisibility(View.VISIBLE);
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
                                                public void onFailure(Call<List<User>> call, Throwable t) {
                                                    Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });
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
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadTimeline(int id) {
        CallSingelton
                .getInstance()
                .getUserAssistances(id, new Callback<List<Event>>() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> assistances = response.body();
                                String assistance = assistances.size() + "";
                                assistanceNumber.setText(assistance);
                                if (!assistances.isEmpty()) {
                                    assistances.removeIf(event -> (event.getStartDate() == null));
                                    Collections.sort(assistances, (e1, e2) -> e2.getStartDate().compareTo(e1.getStartDate()));
                                    adapter = new TimelineAdapter(assistances, getContext());
                                    recyclerView.setAdapter(adapter);
                                }

                                if (id != CallSingelton.getUserId()) {
                                    int paddingDp = -70;
                                    float density = getContext().getResources().getDisplayMetrics().density;
                                    int paddingPixel = (int) (paddingDp * density);
                                    recyclerView.setPadding(0, 0, 0, paddingPixel);
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
        String url = "";
        if (image != null) {
            if (image.startsWith("http")) {
                url = image;
            } else {
                url = "http://puigmal.salle.url.edu/img/" + image;
            }
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
}