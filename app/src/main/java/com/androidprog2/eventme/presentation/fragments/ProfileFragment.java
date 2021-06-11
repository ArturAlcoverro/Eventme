package com.androidprog2.eventme.presentation.fragments;

import android.content.Intent;
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
import com.androidprog2.eventme.presentation.adapters.TimelineAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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

    private ImageButton editProfileBtn;
    private ImageButton backArrow_btn;
    private LinearLayout linearLayout;
    private ImageView profileImage;
    private TextView profileName;
    private User user;
    private RecyclerView recyclerView;
    private TimelineAdapter adapter;

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
        View view =  inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage = view.findViewById(R.id.imageProfile);
        profileName = view.findViewById(R.id.textView_Name);
        editProfileBtn = view.findViewById(R.id.editProfileBtn);
        backArrow_btn = view.findViewById(R.id.arrowLeft);
        linearLayout = view.findViewById(R.id.linearButtons);
        recyclerView = view.findViewById(R.id.recyclerViewTimeline);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        JSONObject jsonObject = CallSingelton.getPayload();
        String image = null;
        try {
            int id = (int) jsonObject.get("id");
            String name = (String) jsonObject.get("name");
            String last_name = (String) jsonObject.get("last_name");
            image = (String) jsonObject.get("image");
            String email = (String) jsonObject.get("email");
            this.user = new User(id, name, last_name, email);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        profileName.setText(this.user.getFull_name());
        setImage(image);

        loadTimeline();

        Intent intent = new Intent(getContext(), EditProfileActivity.class);
        backArrow_btn.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);

        editProfileBtn.setOnClickListener(v -> {
            startActivity(intent);
        });
        return view;
    }

    private void loadTimeline() {
        CallSingelton
                .getInstance()
                .getUserAssistances(239, new Callback<List<Event>>() {
                    @Override
                    public void onResponse(Call<List<Event>> call, Response<List<Event>> response) {
                        if (response.isSuccessful()) {
                            if (response.code() == 200) {
                                List<Event> assistances = response.body();
                                if(!assistances.isEmpty()){
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
        String url = "http://puigmal.salle.url.edu/img/" + image;
        ImageLoader imageLoader = VolleySingleton.getInstance(getContext()).getImageLoader();
        imageLoader.get(url, new ImageLoader.ImageListener() {

            @Override
            public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                if (response.getBitmap() != null){
                    profileImage.setImageBitmap(response.getBitmap());
                }
            }

            public void onErrorResponse(VolleyError error) {
                profileImage.setImageResource(R.drawable.avatar_profile);
            }
        });
    }
}