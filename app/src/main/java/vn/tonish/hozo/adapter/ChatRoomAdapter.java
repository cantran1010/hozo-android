package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/21/2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {


    private final List<TaskResponse> tasks;
    private Context context;

    public interface ChatRoomListener{
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
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        TaskResponse taskResponse = tasks.get(position);

        Utils.displayImageAvatar(context, holder.imgAvatar, taskResponse.getPoster().getAvatar());
        holder.tvTitle.setText(taskResponse.getTitle());
        holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(taskResponse.getCreatedAt(), context));
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
                    if(chatRoomListener != null) chatRoomListener.onClick(getAdapterPosition());
                }
            });

        }
    }

}