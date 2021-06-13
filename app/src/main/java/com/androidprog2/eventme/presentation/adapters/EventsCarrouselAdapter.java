package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.util.Log;
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
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventsCarrouselAdapter extends RecyclerView.Adapter<EventsCarrouselAdapter.EventsCarrouselViewHolder> {

    private List<Event> events;
    private Context context;

    public EventsCarrouselAdapter(List<Event> events, Context context) {
        this.events = events;
        this.context = context;
    }

    @NonNull
    @Override
    public EventsCarrouselViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_carrousel_event, parent, false);
        return new EventsCarrouselViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsCarrouselViewHolder holder, int position) {
        holder.bind(events.get(position), context);
    }

    @Override
    public int getItemCount() {
        return this.events.size();
    }

    public void updateData(List<Event> events) {
        this.events = events;
        notifyDataSetChanged();
    }

    public static class EventsCarrouselViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Event event;
        private TextView event_days;
        private TextView event_name;
        private TextView event_participants;
        private ImageView event_image;

        public EventsCarrouselViewHolder(@NonNull View itemView) {
            super(itemView);
            this.event_days = (TextView) itemView.findViewById(R.id.home_carrousel_event_days);
            this.event_name = (TextView) itemView.findViewById(R.id.home_carrousel_event_name);
            this.event_participants = (TextView) itemView.findViewById(R.id.home_carrousel_event_participations);
            this.event_image = (ImageView) itemView.findViewById(R.id.home_carrousel_event_img);
        }

        private void setImage(String image, Context context) {
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            Log.d("IMAGE:", "---" + image + "---");
            imageLoader.get(image, new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null){
                        event_image.setImageBitmap(response.getBitmap());
                    }
                }

                public void onErrorResponse(VolleyError error) {
                    event_image.setImageResource(R.drawable.avatar_profile);
                }
            });
        }

        public void bind(Event _event, Context context) {
            this.event = _event;
            this.event_days.setText(event.getPeriod());
            this.event_name.setText(event.getNameAndLocation());

            String imageUrl = "";
            if (event.getImage() != null)
                if (event.getImage().toLowerCase().startsWith("http:") || event.getImage().toLowerCase().startsWith("https:"))
                    imageUrl = event.getImage();
                else
                    imageUrl = "http://puigmal.salle.url.edu/img/" + event.getImage();

            if (!imageUrl.equals("")){
                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.test_event_img)
                        .error(R.drawable.test_event_img);
                Glide.with(context)
                        .applyDefaultRequestOptions(options)
                        .load(imageUrl)
                        .into(this.event_image);
//                setImage(imageUrl, context);
            }



            CallSingelton.getInstance().getEventAssistances(event.getId(), new Callback() {
                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        ArrayList<User> users = (ArrayList<User>) response.body();
                        event_participants.setText(users.size() + " / " + event.getNumParticipants() + " will assist");
                    } catch (Exception e) {
                        event_participants.setText(event.getNumParticipants() + " maximum seats");
                    }
                }

                @Override
                public void onFailure(Call call, Throwable t) {
                    event_participants.setText(event.getNumParticipants() + " maximum seats");
                }
            });

        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}
