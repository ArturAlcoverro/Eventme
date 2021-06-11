package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.activities.ChatActivity;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatListViewHolder> {
    public static String EXTRA_ID = "EXTRA_ID";
    public static String EXTRA_NAME = "EXTRA_NAME";
    public static String EXTRA_LAST_NAME = "EXTRA_LAST_NAME";
    public static String EXTRA_EMAIL = "EXTRA_EMAIL";
    public static String EXTRA_IMAGE = "EXTRA_IMAGE";

    List<User> users;
    Context context;

    public ChatListAdapter(List<User> users, Context context) {
        this.users = users;
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
        holder.bind(users.get(position), context);
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class ChatListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Callback<List<Message>> {

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
            itemView.setOnClickListener(this);
        }

        public void bind(User _user, Context _context){
            this.user = _user;

            CallSingelton
                    .getInstance()
                    .getMessages(this.user.getId(), this);
            this.context = _context;
            String url = "http://puigmal.salle.url.edu/img/" + this.user.getImage();
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();
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
        }

        @Override
        public void onResponse(Call call, Response response) {
            if (response.isSuccessful()) {
                if (response.code() == 200) {
                    List<Message> messages = (List<Message>) response.body();
                    if(!messages.isEmpty()){
                        this.message = messages.get(messages.size()-1);
                        this.hourMessage.setText(message.getTs());
                        this.contentMessage.setText(message.getContent());
                    }else{
                        this.hourMessage.setText("");
                        this.contentMessage.setText("");
                    }
                }
            } else {
                try {
                    Toast.makeText(context, response.errorBody().string(), Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
            this.hourMessage.setText("");
            this.contentMessage.setText("");
        }

        @Override
        public void onClick(View v) {
            System.out.println("ha clicalt al item aixi q fes algu no?");
            Intent intent = new Intent(context, ChatActivity.class);
            intent.putExtra(EXTRA_ID, this.user.getId());
            intent.putExtra(EXTRA_NAME, this.user.getName());
            intent.putExtra(EXTRA_LAST_NAME, this.user.getLast_name());
            intent.putExtra(EXTRA_EMAIL, this.user.getEmail());
            intent.putExtra(EXTRA_IMAGE, this.user.getImage());

            this.context.startActivity(intent);
        }
    }

}
