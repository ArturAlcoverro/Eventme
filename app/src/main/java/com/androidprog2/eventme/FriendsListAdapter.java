package com.androidprog2.eventme;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.androidprog2.eventme.business.User;

import java.util.List;

public class FriendsListAdapter extends RecyclerView.Adapter<FriendsListAdapter.FriendsListViewHolder> {

    List<User> users;
    Context context;

    public FriendsListAdapter(List<User> users, Context context) {
        this.users = users;
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
        holder.bind(users.get(position));
    }

    @Override
    public int getItemCount() {
        return this.users.size();
    }

    public static class FriendsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private User user;
        private ImageView user_image;
        private TextView nickname;
        private TextView full_name;

        public FriendsListViewHolder(@NonNull View itemView) {
            super(itemView);
            this.user_image = (ImageView) itemView.findViewById(R.id.imageProfile);
            this.nickname = (TextView) itemView.findViewById(R.id.nickname);
            this.full_name = (TextView) itemView.findViewById(R.id.full_name);
        }

        public void bind(User _user){
            this.user = _user;
            //this.user_image.setImageBitmap());
            this.nickname.setText(user.getNickname());
            this.full_name.setText(user.getFull_name());
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }
    }
}