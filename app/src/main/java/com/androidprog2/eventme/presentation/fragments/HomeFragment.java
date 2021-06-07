package com.androidprog2.eventme.presentation.fragments;

import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Event;
import com.androidprog2.eventme.presentation.adapters.EventsCarrouselAdapter;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements ViewPager2.PageTransformer {


//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    private String mParam1;
//    private String mParam2;

    private ViewPager2 mViewPager;

    public HomeFragment() {
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mViewPager = (ViewPager2) view.findViewById(R.id.home_view_pager);
        mViewPager.setPageTransformer(this);
        mViewPager.addItemDecoration(new HorizontalMarginItemDecoration(55));
        mViewPager.setOffscreenPageLimit(1);

        ArrayList<Event> events = new ArrayList<>();

        events.add(new Event(1, "Name string exisde", 1, "Plaça Catalunya", new Date(), new Date(), new Date(), 20, "imagen"));
        events.add(new Event(2, "Name string", 1, "Plaça Catalunya", new Date(), new Date(), new Date(), 20, "imagen"));
        events.add(new Event(3, "Name string", 1, "Plaça Catalunya", new Date(), new Date(), new Date(), 20, "imagen"));
        events.add(new Event(4, "Name string", 1, "Plaça Catalunya", new Date(), new Date(), new Date(), 20, "imagen"));
        events.add(new Event(5, "Name string", 1, "Plaça Catalunya", new Date(), new Date(), new Date(), 20, "imagen"));
        events.add(new Event(6, "Name string", 1, "Plaça Catalunya", new Date(), new Date(), new Date(), 20, "imagen"));

        EventsCarrouselAdapter adapter = new EventsCarrouselAdapter(events, getContext());

        mViewPager.setAdapter(adapter);

        return view;
    }

    @Override
    public void transformPage(@NonNull View view, float position) {
        int val = 100;
        view.setTranslationX(-val * position);
        view.setScaleY(1 - (0.15f * Math.abs(position)));
    }

    public class HorizontalMarginItemDecoration extends RecyclerView.ItemDecoration{
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
}

