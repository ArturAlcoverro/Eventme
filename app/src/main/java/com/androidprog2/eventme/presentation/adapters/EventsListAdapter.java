package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.Event;

import java.util.List;

public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.EventsListViewHolder> {
    List<Event> events;
    Context context;

    public EventsListAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsListAdapter.EventsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_event, parent, false);
        return new EventsListAdapter.EventsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsListAdapter.EventsListViewHolder holder, int position) {
        holder.bind(events.get(position), context);
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public static class EventsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Event event;
        private Context context;
        private ImageView event_image;
        private TextView name;
        private TextView location;
        private TextView date;
        private TextView category;

        public EventsListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.event_image = (ImageView) itemView.findViewById(R.id.eventImageList);
            this.name = (TextView) itemView.findViewById(R.id.eventTitleList);
            this.location = (TextView) itemView.findViewById(R.id.eventLocationList);
            this.date = (TextView) itemView.findViewById(R.id.eventDateList);
            this.category = (TextView) itemView.findViewById(R.id.eventCategoryList);
        }

        public void bind(Event _event, Context _context){
            this.event = _event;
            this.context = _context;
            String url = "http://puigmal.salle.url.edu/img/" + this.event.getImg();
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();

            imageLoader.get(url, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null){
                        event_image.setImageBitmap(response.getBitmap());
                    }
                }

                public void onErrorResponse(VolleyError error) {
                    event_image.setImageResource(R.drawable.default_event);
                }

            });

            this.name.setText(this.event.getName());
            this.location.setText(this.event.getLocalization());
            this.date.setText(this.event.getPeriod());
            this.category.setText(this.event.getType());
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}
