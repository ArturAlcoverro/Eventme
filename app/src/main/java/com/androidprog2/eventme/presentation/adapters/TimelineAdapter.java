package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Event;
import com.google.android.material.card.MaterialCardView;

import java.util.Date;
import java.util.List;

public class TimelineAdapter extends RecyclerView.Adapter<TimelineAdapter.timelineViewHolder> {
    List<Event> events;
    Context context;

    public TimelineAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public TimelineAdapter.timelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        return new TimelineAdapter.timelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimelineAdapter.timelineViewHolder holder, int position) {
        holder.bind(events, position);
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public static class timelineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Event event;
        private TextView name;
        private TextView location;
        private TextView day;
        private TextView month;
        private TextView category;

        private View topLine;
        private View bottomLine;

        private MaterialCardView circleCardView;

        public timelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.timeline_item_event_name);
            this.location = (TextView) itemView.findViewById(R.id.timeline_item_event_location);
            this.day = (TextView) itemView.findViewById(R.id.timeline_item_event_day);
            this.month = (TextView) itemView.findViewById(R.id.timeline_item_event_month);
            this.category = (TextView) itemView.findViewById(R.id.timeline_item_event_category);
            this.circleCardView = (MaterialCardView) itemView.findViewById(R.id.circle_card_view);
            this.topLine = (View) itemView.findViewById(R.id.timeline_line_top);
            this.bottomLine = (View) itemView.findViewById(R.id.timeline_line_bottom);
        }

        public void bind(List<Event> _events, int position) {
            this.event = _events.get(position);
            this.name.setText(this.event.getName());
            this.location.setText(this.event.getLocation());
            this.category.setText(this.event.getType());

            String period = this.event.getPeriod();
            if(!period.equals("")) {
                this.day.setText(period.split(" ")[1]);
                this.month.setText(period.split(" ")[0]);
            }
            Log.d("POSITION", "---"+ position + "---");

            if (position == 0) {
                this.topLine.setVisibility(View.INVISIBLE);
            }else{
                this.topLine.setVisibility(View.VISIBLE);
            }
            if (position == _events.size()-1) {
                this.bottomLine.setVisibility(View.INVISIBLE);
            }
            else{
                this.bottomLine.setVisibility(View.VISIBLE);
            }

            if (this.event.getStartDate().compareTo(new Date()) >= 0) {

                circleCardView.setCardBackgroundColor(
                        ResourcesCompat.getColor(
                                itemView.getResources(),
                                R.color.background_color_lighter,
                                null));
                topLine.setBackgroundColor(
                        ResourcesCompat.getColor(
                                itemView.getResources(),
                                R.color.background_color_lighter_opaque,
                                null));
                bottomLine.setBackgroundColor(
                        ResourcesCompat.getColor(
                                itemView.getResources(),
                                R.color.background_color_lighter_opaque,
                                null));
            }
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}
