package com.androidprog2.eventme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.androidprog2.eventme.business.Event;

import java.util.List;

public class timelineAdapter extends RecyclerView.Adapter<timelineAdapter.timelineViewHolder> {
    List<Event> events;
    Context context;

    public timelineAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public timelineAdapter.timelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline_item, parent, false);
        return new timelineAdapter.timelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull timelineAdapter.timelineViewHolder holder, int position) {
        holder.bind(events.get(position));
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public static class timelineViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Event event;
        private TextView name;
        private TextView location;
        private TextView day;
        private TextView month;
        private TextView category;

        public timelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = (TextView) itemView.findViewById(R.id.timeline_item_event_name);
            this.location = (TextView) itemView.findViewById(R.id.timeline_item_event_location);
            this.day = (TextView) itemView.findViewById(R.id.timeline_item_event_day);
            this.month = (TextView) itemView.findViewById(R.id.timeline_item_event_month);
            this.category = (TextView) itemView.findViewById(R.id.timeline_item_event_category);
        }

        public void bind(Event _event){
            this.event = _event;
            this.name.setText(this.event.getName());
            this.location.setText(this.event.getLocalization());
            this.day.setText(this.event.getDay());
            this.month.setText(this.event.getMonth());
            this.category.setText(this.event.getType());
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}
