package com.androidprog2.eventme.presentation.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.recyclerview.widget.RecyclerView;

import com.androidprog2.eventme.R;
import com.androidprog2.eventme.business.Message;
import com.androidprog2.eventme.business.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.messageViewHolder> {
    private static final int VIEW_MESSAGE_RECEIVED = 0;
    private static final int VIEW_MESSAGE_SENT = 1;

    private List<Message> messages;
    private Context context;
    private User user;

    public MessageListAdapter(List<Message> messages, User user, Context context) {
        this.messages = messages;
        this.context = context;
        this.user = user;
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getUser_id_send() == this.user.getId()) {
            return VIEW_MESSAGE_RECEIVED;
        } else {
            return VIEW_MESSAGE_SENT;
        }
    }

    @NonNull
    @Override
    public MessageListAdapter.messageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_MESSAGE_RECEIVED) {
            View viewLeft = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_xat_left, parent, false);

            return new MessageListAdapter.messageViewHolder(viewLeft);
        } else {
            View viewRight = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_xat_right, parent, false);

            return new MessageListAdapter.messageViewHolder(viewRight);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListAdapter.messageViewHolder holder, int position) {
        holder.bind(messages.get(position), user, context);
    }

    @Override
    public int getItemCount() {
        return this.messages.size();
    }

    @UiThread
    public void addItem(Message message) {
        this.messages.add(message);
        notifyItemInserted(getItemCount() - 1);
    }

    public static class messageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, RequestListener<Drawable> {

        private Message mMessage;
        private User mUser;
        private TextView mContent;
        private TextView mTime;
        private ImageView mImg;
        private Calendar mCalendar;


        public messageViewHolder(@NonNull View itemView) {
            super(itemView);
            mContent = itemView.findViewById(R.id.xat_item_message);
            mTime = itemView.findViewById(R.id.xat_message_time);
            mImg = itemView.findViewById(R.id.xat_item_img);
            mCalendar = new GregorianCalendar();
        }

        public void bind(Message _message, User _user, Context context) {
            this.mMessage = _message;
            this.mUser = _user;
            String content = this.mMessage.getContent();
            mContent.setText(content);
            mCalendar.setTime(new Date(mMessage.getTimestamp().getTime()));

            printDate();

            if (content.startsWith("http://imgur.com/")) {
                mContent.setVisibility(View.GONE);
                mImg.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(content)
                        .listener(this)
                        .into(mImg);
            }
        }

        private void printDate() {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());

            String dateFormat = "HH:mm";

            if (calendar.get(Calendar.YEAR) != mCalendar.get(Calendar.YEAR)) {
                dateFormat = "HH:mm, dd-MMM-YYYY";
            } else if (calendar.get(Calendar.DAY_OF_YEAR) != mCalendar.get(Calendar.DAY_OF_YEAR)){
                dateFormat = "HH:mm, dd MMM";
            }
            SimpleDateFormat formater = new SimpleDateFormat(dateFormat);
            mTime.setText(formater.format(new Date(mMessage.getTimestamp().getTime())));
        }

        @Override
        public void onClick(View v) {
            //Open activity detail
        }


        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
            mContent.setVisibility(View.VISIBLE);
            return false;
        }

        @Override
        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
            return false;
        }
    }
}
