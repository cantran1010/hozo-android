package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import vn.tonish.hozo.R;
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

    public ChatRoomAdapter(Context context, List<TaskResponse> tasks) {
        this.tasks = tasks;
        this.context = context;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        TaskResponse taskResponse = tasks.get(position);

        Utils.displayImageAvatar(context, holder.imgAvatar, taskResponse.getPoster().getAvatar());
        holder.tvTitle.setText(taskResponse.getTitle());
        holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), context));

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Message message = dataSnapshot.getValue(Message.class);
                message.setId(dataSnapshot.getKey());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message : " + message.toString());

                holder.tvLastMsg.setText(message.getMessage());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                LogUtils.d(TAG, "messageCloudEndPoint onChildChanged , message : " + message.toString());
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

        DatabaseReference messageCloudEndPoint = myRef.child("task-messages").child(String.valueOf(taskResponse.getId()));

        messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(childEventListener);
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }


    public class MyViewHolder extends BaseHolder {
        CircleImageView imgAvatar;
        TextViewHozo tvTitle, tvTimeAgo, tvLastMsg;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvTimeAgo = itemView.findViewById(R.id.tv_time_ago);
            tvLastMsg = itemView.findViewById(R.id.tv_last_msg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chatRoomListener != null) chatRoomListener.onClick(getAdapterPosition());
                }
            });

        }
    }

}