package com.androidprog2.eventme.presentation.fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.adapters.EventsCarrouselAdapter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ViewPager2.PageTransformer, View.OnClickListener {


    private ViewPager2 mViewPager;
    private LinearLayout mNoEventsLayout;
    private ArrayList<Event> mEvents;
    private ArrayList<String> mCategories;

    private EventsCarrouselAdapter mCarrouselAdapter;
    private ArrayList<Chip> mChips;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mCategories = new ArrayList<>();
        mCategories.add("music");
        mCategories.add("art");
        mCategories.add("cultural");
        mCategories.add("sport");
        mCategories.add("science");
        mCategories.add("technology");
        mCategories.add("food");
        mCategories.add("fashion");
        mCategories.add("politics");
        mCategories.add("education");
        mCategories.add("travel");
        mCategories.add("games");

        mViewPager = view.findViewById(R.id.home_view_pager);
        mNoEventsLayout = view.findViewById(R.id.home_no_events_layout);

        loadChips(view);

        mViewPager.setPageTransformer(this);
        mViewPager.addItemDecoration(new HorizontalMarginItemDecoration(55));
        mViewPager.setOffscreenPageLimit(1);

        loadEvents();

        return view;
    }

    private void loadEvents() {
        CallSingelton.getInstance().getEvents(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                for (Chip chip : mChips) {
                    chip.setEnabled(true);
                }

                //                ArrayList<Event> events = new ArrayList<>();
                mEvents = (ArrayList<Event>) response.body();

//                for(Event event: _events){
//                    if(event.getStartDate() != null)
//                        if(event.getStartDate().c)
//                }

                isEvents(mEvents.size() > 0);
                mCarrouselAdapter = new EventsCarrouselAdapter(mEvents, getContext());
                mViewPager.setAdapter(mCarrouselAdapter);

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.d("ERR", "------GET EVENTS------");
            }
        });
    }

    private void loadChips(View view) {
        mChips = new ArrayList<>();
        Chip allChip = view.findViewById(R.id.home_chip_all);
        allChip.setChecked(true);
        mChips.add(allChip);
        mChips.add(view.findViewById(R.id.home_chip_music));
        mChips.add(view.findViewById(R.id.home_chip_cultural));
        mChips.add(view.findViewById(R.id.home_chip_sport));
        mChips.add(view.findViewById(R.id.home_chip_education));
        mChips.add(view.findViewById(R.id.home_chip_travel));
        mChips.add(view.findViewById(R.id.home_chip_games));
        mChips.add(view.findViewById(R.id.home_chip_technology));
        mChips.add(view.findViewById(R.id.home_chip_science));
        mChips.add(view.findViewById(R.id.home_chip_art));
        mChips.add(view.findViewById(R.id.home_chip_food));
        mChips.add(view.findViewById(R.id.home_chip_fashion));
        mChips.add(view.findViewById(R.id.home_chip_politics));
        mChips.add(view.findViewById(R.id.home_chip_others));

        for (Chip chip : mChips) {
            chip.setEnabled(false);
            chip.setOnClickListener(this);
        }
    }

    private void applyFilter(String type) {
        Log.d("TAG", "---" + type + "---");
        ArrayList<Event> events = new ArrayList<>();
        for (Event event : mEvents)
            if (event.getType().toLowerCase().equals(type))
                events.add(event);

        isEvents(events.size() > 0);
        mCarrouselAdapter.updateData(events);
        mViewPager.setCurrentItem(0, false);
    }

    private void otherFilter() {
        ArrayList<Event> events = new ArrayList<>();
        boolean isOther = true;
        for (Event event : mEvents) {
            for (String category : mCategories) {
                if (event.getType().toLowerCase().equals(category))
                    isOther = false;
            }
            if(isOther) events.add(event);
            isOther = true;
        }

        isEvents(events.size() > 0);
        mCarrouselAdapter.updateData(events);
        mViewPager.setCurrentItem(0, false);
    }

    private void clearFilter() {
        isEvents(mEvents.size() > 0);
        mCarrouselAdapter.updateData(mEvents);
        mViewPager.setCurrentItem(0, false);
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        int val = 100;
        view.setTranslationX(-val * position);
        view.setScaleY(1 - (0.15f * Math.abs(position)));
    }

    @Override
    public void onClick(View v) {
        for (Chip chip : mChips) {
            chip.setChecked(false);
        }
        Chip chip = (Chip) v;
        chip.setChecked(true);
        String type = (String) chip.getTag();

        if (type.equals("all"))
            clearFilter();
        else if (type.equals("others"))
            otherFilter();
        else
            applyFilter((String) chip.getTag());

    }

    public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration {
        private final int horizontalMarginInPx;

        public HorizontalMarginItemDecoration(int horizontalMarginInPx) {
            this.horizontalMarginInPx = horizontalMarginInPx;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                                   RecyclerView.State state) {
            outRect.right = horizontalMarginInPx;
            outRect.left = horizontalMarginInPx;
        }
    }

    private void isEvents(boolean is) {
        if (is) {
            mViewPager.setVisibility(View.VISIBLE);
            mNoEventsLayout.setVisibility(View.GONE);
        } else {
            mViewPager.setVisibility(View.GONE);
            mNoEventsLayout.setVisibility(View.VISIBLE);
        }
    }
}

