package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.Utils.sortID;

/**
 * Created by cantran on 4/21/2017.
 */

public class ChatPrivateAdapter extends RecyclerView.Adapter<ChatPrivateAdapter.MyViewHolder> {

    private static final String TAG = ChatPrivateAdapter.class.getSimpleName();
    private final List<Assigner> assigners;
    private Context context;
    private List<Message> messages = new ArrayList<>();


    public interface ChatPrivateListener {
        void onClick(int position);
    }

    private ChatPrivateListener chatPrivateListener;

    public ChatPrivateListener getChatPrivateListener() {
        return chatPrivateListener;
    }

    public void setChatPrivateListener(ChatPrivateListener chatPrivateListener) {
        this.chatPrivateListener = chatPrivateListener;
    }

    public ChatPrivateAdapter(final Context context, List<Assigner> assigners) {
        LogUtils.d(TAG, "taskiD: " + assigners.get(0).getId());
        this.context = context;
        this.assigners = assigners;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_private_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {
            Assigner assigner = assigners.get(position);
            Utils.displayImageAvatar(context, holder.imgAvatar, assigner.getAvatar());
            holder.tvTitle.setText(assigner.getFullName());
            DatabaseReference myRef;
            myRef = FirebaseDatabase.getInstance().getReference();
            for (int i = 0; i < assigners.size(); i++) messages.add(new Message());
            if (position == 0) {
                final DatabaseReference messageCloudEndPoint1 = myRef.child("task-messages").child(String.valueOf(assigner.getId()));
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildAdded , message : " + message.toString());
                        LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildAdded , holder.getAdapterPosition() : " + holder.getAdapterPosition());
                        if (message.getType() == 1)
                            holder.tvLastMsg.setText(context.getString(R.string.send_img));
                        else
                            holder.tvLastMsg.setText(message.getMessage());
                        messages.set(0, message);
                        Map<String, Boolean> map = messages.get(0).getReads();
                        if (map != null)
                            LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint map : " + map.toString());
                        if (map != null && map.containsKey(String.valueOf(UserManager.getMyUser().getId()))) {
                            holder.imgDot.setVisibility(View.GONE);
                            assigners.get(0).setRead(false);
                        } else {
                            holder.imgDot.setVisibility(View.VISIBLE);
                            assigners.get(0).setRead(true);
                        }
                        holder.tvTimeAgo.setVisibility(View.VISIBLE);
                        holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(message.getCreated_atLong(true), context));
                        sendBroasdCast();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        LogUtils.d(TAG, "onChildChanged");
                        notifyDataSetChanged();
                        sendBroasdCast();
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                messageCloudEndPoint1.orderByKey().limitToLast(1).addChildEventListener(childEventListener);
            } else {
                ChildEventListener childEventListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        LogUtils.d(TAG, "messageCloudEndPoint onChildAdded last, message : " + message.toString());
                        if (message.getType() == 1)
                            holder.tvLastMsg.setText(context.getString(R.string.send_img));
                        else
                            holder.tvLastMsg.setText(message.getMessage());
                        messages.set(position, message);
                        Map<String, Boolean> map = messages.get(position).getReads();
                        if (map != null)
                            LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint map : " + map.toString());
                        if (map != null && map.containsKey(String.valueOf(UserManager.getMyUser().getId()))) {
                            holder.imgDot.setVisibility(View.GONE);
                            assigners.get(position).setRead(false);
                        } else {
                            holder.imgDot.setVisibility(View.VISIBLE);
                            assigners.get(position).setRead(true);
                        }
                        holder.tvTimeAgo.setVisibility(View.VISIBLE);
                        holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(message.getCreated_atLong(true), context));
                        sendBroasdCast();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        LogUtils.d(TAG, "onChildChanged" + position);
                        notifyDataSetChanged();
                        sendBroasdCast();

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };
                DatabaseReference messageCloudEndPoint = myRef.child("private-messages").child(String.valueOf(assigners.get(0).getId())).child(sortID(assigners.get(position).getId()));
                messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(childEventListener);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return assigners.size();
    }


    public class MyViewHolder extends BaseHolder {
        CircleImageView imgAvatar;
        ImageView imgDot;
        TextViewHozo tvTitle, tvTimeAgo, tvLastMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgDot = itemView.findViewById(R.id.dot);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (chatPrivateListener != null)
                            chatPrivateListener.onClick(getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private int getCountRoomUnRead() {
        int count = 0;
        for (Assigner assigner : assigners
                ) {
            if (assigner.isRead()) count++;
        }
        return count;
    }


    private void sendBroasdCast() {
        Intent intentNewMsg = new Intent();
        intentNewMsg.setAction(Constants.BROAD_CAST_PUSH_CHAT_COUNT);
        intentNewMsg.putExtra(Constants.COUNT_NEW_CHAT_EXTRA, getCountRoomUnRead());
        context.sendBroadcast(intentNewMsg);

    }

}