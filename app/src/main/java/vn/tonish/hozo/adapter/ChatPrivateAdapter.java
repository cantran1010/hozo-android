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
import vn.tonish.hozo.utils.PreferUtils;
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
    private final Context context;
    private final List<Message> messages = new ArrayList<>();
    private DatabaseReference messageCloudEndPoint, messageGroupCloudEndPoint;
    private ChildEventListener groupListener, messageListener;
    private final int taskID;
    private final int posterID;


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

    public ChatPrivateAdapter(final Context context, int taskID, int posterID, List<Assigner> assigners) {
        this.context = context;
        this.assigners = assigners;
        this.taskID = taskID;
        this.posterID = posterID;
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
            if (assigner.getId() == taskID && assigner.getFullName().equalsIgnoreCase(context.getString(R.string.group_chat)))
                holder.imgAvatar.setImageResource(R.mipmap.app_icon);
            else
                Utils.displayImageAvatar(context, holder.imgAvatar, assigner.getAvatar());
            holder.tvTitle.setText(assigner.getFullName());
            for (int i = 0; i < assigners.size(); i++) messages.add(new Message());
            if (assigner.getId() == taskID && assigner.getFullName().equalsIgnoreCase(context.getString(R.string.group_chat))) {
                groupListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildAdded , message : " + message.toString());
                        updateUI(position, message, holder.tvLastMsg, holder.imgDot, holder.tvTimeAgo);
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
                messageGroupCloudEndPoint = FirebaseDatabase.getInstance().getReference().child("task-messages").child(String.valueOf(taskID));
                messageGroupCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(groupListener);
            } else {
                messageListener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        Message message = dataSnapshot.getValue(Message.class);
                        message.setId(dataSnapshot.getKey());
                        LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildAdded , message : " + message.toString());
                        updateUI(position, message, holder.tvLastMsg, holder.imgDot, holder.tvTimeAgo);
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
                int id;
                if (posterID == UserManager.getMyUser().getId())
                    id = posterID;
                else id = UserManager.getMyUser().getId();
                messageCloudEndPoint = FirebaseDatabase.getInstance().getReference().child("private-messages").child(String.valueOf(taskID)).child(sortID(id, assigner.getId()));
                messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(messageListener);
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
        final CircleImageView imgAvatar;
        final ImageView imgDot;
        final TextViewHozo tvTitle;
        final TextViewHozo tvTimeAgo;
        final TextViewHozo tvLastMsg;

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

    private void updateUI(int pos, Message message, TextViewHozo tvSms, ImageView imgerDot, TextViewHozo timeAgo) {
        if (message.getType() == 1)
            tvSms.setText(context.getString(R.string.send_img));
        else
            tvSms.setText(message.getMessage());
        messages.set(pos, message);
        Map<String, Boolean> map = messages.get(pos).getReads();
        if (map != null)
            LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint map : " + map.toString());
        if (map != null && map.containsKey(String.valueOf(UserManager.getMyUser().getId()))) {
            imgerDot.setVisibility(View.GONE);
            assigners.get(pos).setRead(false);
        } else {
            imgerDot.setVisibility(View.VISIBLE);
            assigners.get(pos).setRead(true);
        }
        timeAgo.setVisibility(View.VISIBLE);
        timeAgo.setText(DateTimeUtils.getTimeAgo(message.getCreated_atLong(true), context));

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
        PreferUtils.setNewPushChatCount(context, getCountRoomUnRead());
        context.sendBroadcast(intentNewMsg);

    }

    public void killListener() {
        if (messageCloudEndPoint != null && messageListener != null)
            messageCloudEndPoint.removeEventListener(messageListener);
        if (messageGroupCloudEndPoint != null && groupListener != null)
            messageGroupCloudEndPoint.removeEventListener(groupListener);

    }

}