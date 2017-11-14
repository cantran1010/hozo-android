package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/21/2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {

    private static final String TAG = ChatRoomAdapter.class.getSimpleName();
    private final List<TaskResponse> tasks;
    private Context context;
    private DatabaseReference myRef;
    private List<Message> messages = new ArrayList<>();

    public interface ChatRoomListener {
        public void onClick(int position);
    }

    private ChatRoomListener chatRoomListener;

    public ChatRoomListener getChatRoomListener() {
        return chatRoomListener;
    }

    public void setChatRoomListener(ChatRoomListener chatRoomListener) {
        this.chatRoomListener = chatRoomListener;
    }

    public ChatRoomAdapter(final Context context, List<TaskResponse> tasks) {
        this.tasks = tasks;
        this.context = context;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();

        for (int i = 0; i < tasks.size(); i++) messages.add(new Message());

        for (int i = 0; i < tasks.size(); i++) {
            final int finalI = i;
            ChildEventListener childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    Message message = dataSnapshot.getValue(Message.class);
                    message.setId(dataSnapshot.getKey());
                    LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message : " + message.toString());

                    messages.set(finalI, message);

                    Intent intentNewMsg = new Intent();
                    intentNewMsg.setAction(Constants.BROAD_CAST_PUSH_COUNT);
                    intentNewMsg.putExtra(Constants.COUNT_NEW_MSG_EXTRA, getCountRoomUnRead());
                    context.sendBroadcast(intentNewMsg);

                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                    Message message = dataSnapshot.getValue(Message.class);
//                    LogUtils.d(TAG, "messageCloudEndPoint onChildChanged , message : " + message.toString());
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

            DatabaseReference messageCloudEndPoint = myRef.child("task-messages").child(String.valueOf(tasks.get(i).getId()));

            messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(childEventListener);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        TaskResponse taskResponse = tasks.get(position);

        Utils.displayImageAvatar(context, holder.imgAvatar, taskResponse.getPoster().getAvatar());
        holder.tvTitle.setText(taskResponse.getTitle());
//        holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), context));
        holder.tvDate.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));

        final DatabaseReference messageCloudEndPoint = myRef.child("task-messages").child(String.valueOf(taskResponse.getId()));

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (holder.getAdapterPosition() < 0) return;

                Message message = dataSnapshot.getValue(Message.class);
                message.setId(dataSnapshot.getKey());
                LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildAdded , message : " + message.toString());
                LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildAdded , holder.getAdapterPosition() : " + holder.getAdapterPosition());

                holder.tvLastMsg.setText(message.getMessage());
                messages.set(holder.getAdapterPosition(), message);

                Map<String, Boolean> map = messages.get(holder.getAdapterPosition()).getReads();

                if (map != null)
                    LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint map : " + map.toString());

                if (map != null && map.containsKey(String.valueOf(UserManager.getMyUser().getId()))) {
                    holder.tvLastMsg.setTextColor(ContextCompat.getColor(context, R.color.setting_text));
//                    holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.tv_black));

                    holder.tvLastMsg.setTypeface(holder.tvLastMsg.getTypeface(), Typeface.NORMAL);
                    holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_message_read, 0, 0, 0);

                } else {
                    holder.tvLastMsg.setTextColor(ContextCompat.getColor(context, R.color.hozo_bg));
//                    holder.tvTitle.setTextColor(ContextCompat.getColor(context, R.color.hozo_bg));

                    holder.tvLastMsg.setTypeface(holder.tvLastMsg.getTypeface(), Typeface.BOLD);
                    holder.tvTitle.setCompoundDrawablesWithIntrinsicBounds(R.drawable.img_message_unread, 0, 0, 0);
                }

                holder.tvTimeAgo.setVisibility(View.VISIBLE);
                holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(message.getCreated_atLong(true), context));

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Message message = dataSnapshot.getValue(Message.class);
//                LogUtils.d(TAG, "onBindViewHolder messageCloudEndPoint onChildChanged , message : " + message.toString());
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

        messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(childEventListener);

    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class MyViewHolder extends BaseHolder {
        CircleImageView imgAvatar;
        TextViewHozo tvTitle, tvTimeAgo, tvLastMsg, tvDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);
            tvDate = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (chatRoomListener != null)
                            chatRoomListener.onClick(getAdapterPosition());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        }
    }

    private int getCountRoomUnRead() {
        int result = 0;

        for (int i = 0; i < messages.size(); i++) {

            LogUtils.d(TAG, "getCountRoomUnRead messsage " + i + " : " + messages.get(i).toString());

            if ((messages.get(i).getReads() == null && messages.get(i).getId() != null) || (messages.get(i).getReads() != null && !messages.get(i).getReads().containsKey(String.valueOf(UserManager.getMyUser().getId()))))
                result++;
        }

        return result;
    }

}