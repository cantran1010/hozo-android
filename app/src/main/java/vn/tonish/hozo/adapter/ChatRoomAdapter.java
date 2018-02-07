package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.ChatGroupActivity;
import vn.tonish.hozo.activity.ChatPrivateActivity;
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.ChatRoom;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/21/2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {

    private static final String TAG = ChatRoomAdapter.class.getSimpleName();
    private final List<ChatRoom> chatRooms;
    private Context context;
    public MemberVerticalAdapter chatPrivateAdapter;

    public ChatRoomAdapter(final Context context, List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_chat, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (holder.getAdapterPosition() < 0) return;
        try {
            ChatRoom chatRoom = chatRooms.get(position);
            holder.tvName.setText(chatRoom.getName());
            holder.tvDate.setText(DateTimeUtils.getOnlyDateFromIso(chatRoom.getCreated_at()));
            updateUI(position, holder.rcvAss);
            LogUtils.d(TAG, "onBindViewHolder" + chatRoom.toString());
//
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return chatRooms.size();
    }

    public class MyViewHolder extends BaseHolder {
        RecyclerView rcvAss;
        TextViewHozo tvName, tvDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDate = itemView.findViewById(R.id.tv_date);
            rcvAss = itemView.findViewById(R.id.rcv_assigner);
            tvName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, DetailTaskActivity.class);
                    intent.putExtra(Constants.TASK_ID_EXTRA, chatRooms.get(getAdapterPosition()).getId());
                    ((BaseActivity) context).startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            });

        }
    }

    private void updateUI(final int pos, RecyclerView rcvChat) {
        final ChatRoom chatRoom = chatRooms.get(pos);
        chatPrivateAdapter = new MemberVerticalAdapter(context, chatRoom.getMembers());
        LinearLayoutManager layoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rcvChat.setLayoutManager(layoutManagaer);
        rcvChat.setAdapter(chatPrivateAdapter);
        chatPrivateAdapter.setMemberVerticalListener(new MemberVerticalAdapter.ChatPrivateListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    Intent intentContact = new Intent(context, ChatGroupActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, chatRoom.getTaskResponse());
                    ((BaseActivity) context).startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(context, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, chatRoom.getTaskResponse());
                    intentContact.putExtra(Constants.ASSIGNER_EXTRA, chatRooms.get(pos).getMembers().get(position));
                    ((BaseActivity) context).startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }

            }
        });
    }

}