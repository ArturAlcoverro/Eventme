package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.User;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {

    List<User> users;
    List<User> requests;
    List<User> allUsers;
    int requestSize;
    Context context;

    public FriendsListAdapter(List<User> users, List<User> requests, Context context) {
        this.users = users;
        for (User user: users) {
            if(user.getImage() == null){
                users.remove(user);
            }
        }
        this.requests = requests;
        requestSize = this.requests.size();
        this.allUsers = Stream.of(this.requests, this.users)
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
        this.context = context;
    }

    @NonNull
    @Override
    public FriendsListAdapter.FriendsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new FriendsListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsListAdapter.FriendsListViewHolder holder, int position) {
        if (position < requestSize){
            holder.bind(allUsers.get(position), true, context);
        }else {
            holder.bind(allUsers.get(position), false, context);
        }
    }

    @Override
    public int getItemCount() {
        return this.allUsers.size();
    }

    public static class FriendsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private User user;
        private boolean isRequest;
        private Context context;
        private ImageView user_image;
        private TextView nickname;
        private TextView full_name;
        private LinearLayout linearLayout;
        private MaterialButton confirmBtn;
        private MaterialButton deleteBtn;

        public FriendsListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_image = (ImageView) itemView.findViewById(R.id.imageProfile);
            this.nickname = (TextView) itemView.findViewById(R.id.nickname);
            this.full_name = (TextView) itemView.findViewById(R.id.full_name);
            this.linearLayout = itemView.findViewById(R.id.linearButtonsFriend);
            this.confirmBtn = itemView.findViewById(R.id.confirmFriend);
            this.deleteBtn = itemView.findViewById(R.id.declineFriend);
        }

        public void bind(User _user, boolean isRequest, Context _context){
            this.user = _user;
            this.isRequest = isRequest;
            this.context = _context;

            String url;
            if(this.user.getImage().startsWith("http")){
                url = this.user.getImage();
            }else{
                url = "http://puigmal.salle.url.edu/img/" + this.user.getImage();
            }
            ImageLoader imageLoader = VolleySingleton.getInstance(context).getImageLoader();

            imageLoader.get(url, new ImageLoader.ImageListener() {

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null){
                        user_image.setImageBitmap(response.getBitmap());
                    }
                }

                public void onErrorResponse(VolleyError error) {
                    user_image.setImageResource(R.drawable.avatar_profile);
                }

            });

            this.nickname.setText(user.getName());
            this.full_name.setText(user.getFull_name());

            if(isRequest){
                linearLayout.setVisibility(View.VISIBLE);
                confirmBtn.setOnClickListener(v -> {
                    acceptFriendShip();
                });
                deleteBtn.setOnClickListener(v -> {
                    declineFriendShip();
                });
            }else{
                linearLayout.setVisibility(View.GONE);
            }
        }

        private void acceptFriendShip() {
            System.out.println("la vol acceptar");
        }

        private void declineFriendShip() {
            System.out.println("la vol declinar");
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}
