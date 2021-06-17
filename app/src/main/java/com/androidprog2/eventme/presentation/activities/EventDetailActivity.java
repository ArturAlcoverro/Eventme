package com.androidprog2.eventme.presentation.activities;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.adapters.ChatListAdapter;
import com.androidprog2.eventme.presentation.adapters.EventsCarrouselAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private int mEventId;

    private TextView mEventName;
    private TextView mCreatorName;
    private TextView mEventCategory;
    private TextView mEventParticipants;
    private TextView mEventStarDate;
    private TextView mEventEndDate;
    private TextView mEventLocation;
    private TextView mEventDescription;

    private LinearLayout mLocationLayout;
    private LinearLayout mMapLayout;
    private LinearLayout mDateLayout;
    private LinearLayout mProgressBarLayout;

    private View mMapDivider;
    private View mDateDivider;


    private CardView mMapCard;

    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private ImageView mEventImage;

    private ImageButton mBackBtn;

    private Button mHelpBtn;
    private Button mAttendBtn;
    private Button mOutBtn;
    private Button mFinishedBtn;

    private Event mEvent;
    private LatLng mCoords;
    private boolean isFinished = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        mEventName = findViewById(R.id.event_detail_event_name);
        mCreatorName = findViewById(R.id.event_detail_user_creator);
        mEventCategory = findViewById(R.id.event_detail_event_category);
        mEventParticipants = findViewById(R.id.event_detail_event_assistances);
        mEventStarDate = findViewById(R.id.event_detail_event_startDate);
        mEventEndDate = findViewById(R.id.event_detail_event_endDate);
        mEventLocation = findViewById(R.id.event_detail_event_location);
        mEventDescription = findViewById(R.id.event_detail_event_description);
        mEventImage = findViewById(R.id.event_detail_event_image);
        mHelpBtn = findViewById(R.id.event_detail_ask_btn);
        mAttendBtn = findViewById(R.id.event_detail_join_btn);
        mOutBtn = findViewById(R.id.event_detail_out_btn);
        mFinishedBtn = findViewById(R.id.event_finished_btn);
        mBackBtn = findViewById(R.id.event_detail_back_btn);
        mMapLayout = findViewById(R.id.event_detail_map_layout);
        mMapCard = findViewById(R.id.event_detail_map_card);
        mLocationLayout = findViewById(R.id.event_detail_location_layout);
        mDateLayout = findViewById(R.id.event_detail_date_layout);
        mMapDivider = findViewById(R.id.event_detail_description_divider);
        mDateDivider = findViewById(R.id.event_detail_date_layout_divider);
        mProgressBarLayout = findViewById(R.id.event_detail_progress_bar_layout);

        mBackBtn.setOnClickListener(v -> {
            finish();
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.event_detail_map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        mEventId = intent.getIntExtra(EventsCarrouselAdapter.EVENT_ID, 0);

        loadEvent();

    }

    private void loadEvent() {
        CallSingelton.getInstance().getEvent(mEventId, new Callback<ArrayList<Event>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ArrayList<Event>> call, Response<ArrayList<Event>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        List<Event> events = response.body();
                        if (events.get(0) != null) {
                            mEvent = events.get(0);
                            printEvent(events.get(0));
                        }
                    }
                } else {
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void printEvent(Event event) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy · hh:mm a");
        String msg = "";

        mEventName.setText(event.getName());
        printOwnerName(event.getOwnerId());
        if (event.getOwnerId() == CallSingelton.getUserId())
            mHelpBtn.setVisibility(View.GONE);
        mEventCategory.setText(event.getType());
        printParticipants(event.getNumParticipants());

        if (event.getStartDate() != null && event.getEndDate() != null) {
            msg = getString(R.string.event_detail_starts) + " " + formatter.format(event.getStartDate());
            mEventStarDate.setText(msg);
            msg = getString(R.string.event_detail_ends) + " " + formatter.format(event.getEndDate());
            mEventEndDate.setText(msg);
            setCalendar();
        } else {
            mDateLayout.setVisibility(View.GONE);
            mDateDivider.setVisibility(View.GONE);
        }

        mEventLocation.setText(event.getLocation());
        mEventDescription.setText(event.getDescription());

        setLocation();

        String imageUrl = "";
        if (event.getImage() != null)
            if (event.getImage().toLowerCase().startsWith("http:") || event.getImage().toLowerCase().startsWith("https:"))
                imageUrl = event.getImage();
            else
                imageUrl = "http://puigmal.salle.url.edu/img/" + event.getImage();

        if (!imageUrl.equals("")) {
            RequestOptions options = new RequestOptions()
                    .placeholder(R.drawable.test_event_img)
                    .error(R.drawable.test_event_img);
            Glide.with(getApplicationContext())
                    .applyDefaultRequestOptions(options)
                    .load(imageUrl)
                    .into(mEventImage);
//                setImage(imageUrl, context);
        }

        if (event.getStartDate() == null || event.getStartDate().before(new Date())) {
            if (event.getEndDate() == null || event.getEndDate().before(new Date()))
                setFinished(true);
            else
                setFinished(false);
        }

    }

    private void setCalendar() {
        mDateLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.TITLE, mEvent.getName());
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, mEvent.getStartDate().getTime());
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, mEvent.getEndDate().getTime());
            intent.putExtra(CalendarContract.Events.EVENT_LOCATION, mEvent.getLocation());
            intent.putExtra(CalendarContract.Events.DESCRIPTION, mEvent.getDescription());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }

    private void printParticipants(int max) {
        CallSingelton.getInstance().getEventAssistances(mEventId, new Callback<ArrayList<User>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<ArrayList<User>> call, Response<ArrayList<User>> response) {
                String msg = "";
                ArrayList<User> users = new ArrayList<>();
                try {
                    users = response.body();
                    msg = users.size() + " / " + max + " " + getString(R.string.event_detail_participants);
                } catch (Exception e) {
                    msg = max + " " + getString(R.string.event_detail_max_participants);
                }
                mEventParticipants.setText(msg);
                for (User user : users) {
                    if (user.getId() == CallSingelton.getUserId())
                        setBtnOut();
                }
                loadAssistanceButtons();
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                String msg = "";
                msg = max + " " + getString(R.string.event_detail_max_participants);
                mEventParticipants.setText(msg);
                loadAssistanceButtons();
            }
        });
    }

    private void printOwnerName(int id) {
        CallSingelton.getInstance().getProfileUser(id, new Callback<List<User>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if (response.isSuccessful()) {
                    if (response.code() == 200) {
                        String msg = "";
                        List<User> users = response.body();
                        msg = getString(R.string.event_detail_by) + " " + users.get(0).getFull_name();
                        mCreatorName.setText(msg);
                        if (users.get(0).getId() != CallSingelton.getUserId())
                            loadOwnerButtons(users.get(0));
                    }
                } else {
                    try {
                        Toast.makeText(getApplicationContext(), response.errorBody().string(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOwnerButtons(User user) {

        mCreatorName.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra(ChatActivity.EXTRA_ID, user.getId());
            startActivity(intent);
        });

        mHelpBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra(ChatListAdapter.EXTRA_ID, user.getId());
            intent.putExtra(ChatListAdapter.EXTRA_NAME, user.getName());
            intent.putExtra(ChatListAdapter.EXTRA_LAST_NAME, user.getLast_name());
            intent.putExtra(ChatListAdapter.EXTRA_EMAIL, user.getEmail());
            intent.putExtra(ChatListAdapter.EXTRA_IMAGE, user.getImage());

            startActivity(intent);
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        setLocation();
    }

    private void setLocation() {
        if (mMap != null && mEvent != null) {
            Geocoder geocoder = new Geocoder(getApplicationContext());
            List<Address> addresses;
            try {
                addresses = geocoder.getFromLocationName(mEvent.getLocation(), 10);
                if (addresses.isEmpty()) {
                    mMapLayout.setVisibility(View.GONE);
                    mMapDivider.setVisibility(View.GONE);
                } else {
                    mCoords = new LatLng(
                            addresses.get(0).getLatitude(),
                            addresses.get(0).getLongitude()
                    );

                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mCoords, 16.0f));
                    mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(getApplicationContext(), R.raw.map_style));
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions()
                            .position(mCoords)
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_pin)));
                    loadMapButtons();
                }
            } catch (IOException e) {
                e.printStackTrace();
                mMapLayout.setVisibility(View.GONE);
                mMapDivider.setVisibility(View.GONE);
            }
        }
    }

    private void loadMapButtons() {
        String label = mEvent.getName();
        String uriBegin = "geo:" + mCoords.latitude + "," + mCoords.longitude;
        String query = mCoords.latitude + "," + mCoords.longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery;
        Uri uri = Uri.parse(uriString);

        mMap.setOnMapClickListener(arg0 -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });

        mLocationLayout.setOnClickListener(v -> {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }

    private void loadAssistanceButtons() {
        mAttendBtn.setOnClickListener(v -> {
            setLoading();
            CallSingelton.getInstance().createAssistance(mEvent.getId(), new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        setBtnOut();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        mOutBtn.setOnClickListener(v -> {
            setLoading();
            CallSingelton.getInstance().deleteAssistance(mEvent.getId(), new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful() && response.code() == 200) {
                        setBtnAttend();
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void setLoading() {
        if (!isFinished) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mOutBtn.setVisibility(View.GONE);
            mAttendBtn.setVisibility(View.GONE);
        }
    }

    private void setBtnAttend() {
        if (!isFinished) {
            mProgressBarLayout.setVisibility(View.GONE);
            mOutBtn.setVisibility(View.GONE);
            mAttendBtn.setVisibility(View.VISIBLE);
        }
    }

    private void setBtnOut() {
        if (!isFinished) {
            mProgressBarLayout.setVisibility(View.GONE);
            mOutBtn.setVisibility(View.VISIBLE);
            mAttendBtn.setVisibility(View.GONE);
        }
    }

    private void setFinished(boolean finished) {
        if (!finished)
            mFinishedBtn.setText(getString(R.string.event_detail_started_event));
        isFinished = true;
        mFinishedBtn.setVisibility(View.VISIBLE);
        mFinishedBtn.setEnabled(false);
        mProgressBarLayout.setVisibility(View.GONE);
        mOutBtn.setVisibility(View.GONE);
        mAttendBtn.setVisibility(View.GONE);
        mHelpBtn.setVisibility(View.GONE);

    }
}