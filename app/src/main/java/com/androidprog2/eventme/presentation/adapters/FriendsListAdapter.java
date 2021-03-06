package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidprog2.eventme.R;
import com.androidprog2.eventme.VolleySingleton;
import com.androidprog2.eventme.business.User;
import com.androidprog2.eventme.persistance.API.CallSingelton;
import com.androidprog2.eventme.presentation.activities.ProfileActivity;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {

    List<User> users;
    List<User> requests;
    List<User> allUsers;
    Context context;

    public FriendsListAdapter(List<User> users, List<User> requests, Context context) {
        this.users = users;
        for (int i = 0; i < users.size(); i++) {
            if(users.get(i).getImage() == null){
                users.remove(i);
            }else {
                users.get(i).setFriend(true);
            }
        }
        for (int i = 0; i < requests.size(); i++) {
            if(requests.get(i).getImage() == null){
                requests.remove(i);
            }else {
                requests.get(i).setFriend(false);
            }
        }
        this.requests = requests;
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
        if (!allUsers.get(position).isFriend()){
            holder.bind(allUsers.get(position), true, context);
        }else {
            holder.bind(allUsers.get(position), false, context);
        }

        holder.deleteBtn.setOnClickListener(v -> {
            CallSingelton
                    .getInstance()
                    .declineFriendShip(allUsers.get(position).getId(), new Callback<Response<Void>>() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                if (response.code() == 200) {
                                    System.out.println("Decline FriendShip");
                                    allUsers.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, allUsers.size());
                                    /*if(!(position < requestSize)){
                                        holder.linearLayout.setVisibility(View.GONE);
                                    }*/
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
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return this.allUsers.size();
    }

    public static class FriendsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public static String EXTRA_ID = "EXTRA_ID";

        private User user;
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
            itemView.setOnClickListener(this);
        }

        public void bind(User _user, boolean isRequest, Context _context){
            this.user = _user;
            this.context = _context;

            String url = "";
            if(this.user.getImage() != null) {
                if (this.user.getImage().startsWith("http")) {
                    url = this.user.getImage();
                } else {
                    url = "http://puigmal.salle.url.edu/img/" + this.user.getImage();
                }
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
            }else{
                linearLayout.setVisibility(View.GONE);
            }
        }

        private void acceptFriendShip() {
            CallSingelton
                    .getInstance()
                    .acceptFriendShip(this.user.getId(), new Callback<Response<Void>>() {
                        @Override
                        public void onResponse(Call call, Response response) {
                            if (response.isSuccessful()) {
                                System.out.println(response.code());
                                if (response.code() == 200) {
                                    System.out.println("Accepted FriendShip");
                                    linearLayout.setVisibility(View.GONE);
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
                        }
                    });
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {
            //Open activity detail
            if(this.user.getId() != CallSingelton.getUserId()) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(EXTRA_ID, this.user.getId());
                context.startActivity(intent);
            }
        }
    }
}
