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
import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;

import java.util.List;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    List<User> users;
    List<Message> messages;
    Context context;

    public ChatListAdapter(List<User> users, Context context) {
        this.users = users;
        //this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ChatListAdapter.ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatListAdapter.ChatListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListAdapter.ChatListViewHolder holder, int position) {
        //holder.bind(users.get(position), messages.get(position));
        holder.bind(users.get(position), context);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private User user;
        private Message message;
        private Context context;
        private ImageView image;
        private TextView full_name;
        private TextView hourMessage;
        private TextView contentMessage;

        public ChatListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.imageProfileChat);
            this.full_name = (TextView) itemView.findViewById(R.id.nameChat);
            this.hourMessage = (TextView) itemView.findViewById(R.id.hourChat);
            this.contentMessage = (TextView) itemView.findViewById(R.id.lastMessage);
        }

        public void bind(User _user, Context _context){
            this.user = _user;
            //this.message = _message;
            this.context = _context;
            String url = "http://puigmal.salle.url.edu/img/" + this.user.getImage();
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
            System.out.println(imageLoader);
            System.out.println(url);
            imageLoader.get(url, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null){
                        image.setImageBitmap(response.getBitmap());
                    }
                }

                public void onErrorResponse(VolleyError error) {
                    image.setImageResource(R.drawable.avatar_profile);
                }

            });

            this.full_name.setText(user.getFull_name());
            //this.hourMessage.setText(message.getTs());
            //this.contentMessage.setText(message.getContent());
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}
