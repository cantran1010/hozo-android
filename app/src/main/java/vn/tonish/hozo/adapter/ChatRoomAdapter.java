package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.activity.ChatActivity;
import vn.tonish.hozo.activity.ChatPrivateActivity;
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/21/2017.
 */

public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.MyViewHolder> {

    private static final String TAG = ChatRoomAdapter.class.getSimpleName();
    private final List<TaskResponse> tasks;
    private Context context;
    public ChatPrivateAdapter chatPrivateAdapter;
    public interface ChatRoomListener {
        void onClick(int position);
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
            TaskResponse taskResponse = tasks.get(position);
            holder.tvName.setText(taskResponse.getTitle());
            holder.tvDate.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
            updateUI(position, holder.rcvAss);
//
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return tasks.size();
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
                    intent.putExtra(Constants.TASK_ID_EXTRA, tasks.get(getAdapterPosition()).getId());
                    ((BaseActivity) context).startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                }
            });

        }
    }


    private void updateUI(final int pos, RecyclerView rcvChat) {
        final TaskResponse taskResponse = tasks.get(pos);
        chatPrivateAdapter = new ChatPrivateAdapter(context, taskResponse.getId(), taskResponse.getPoster().getId(), getAssigners(taskResponse, pos));
        LinearLayoutManager layoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rcvChat.setLayoutManager(layoutManagaer);
        rcvChat.setAdapter(chatPrivateAdapter);
        chatPrivateAdapter.setChatPrivateListener(new ChatPrivateAdapter.ChatPrivateListener() {
            @Override
            public void onClick(int position) {
                if (position == 0) {
                    Intent intentContact = new Intent(context, ChatActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    ((BaseActivity) context).startActivityForResult(intentContact, Constants.REQUEST_CODE_CHAT, TransitionScreen.DOWN_TO_UP);
                } else {
                    Intent intentContact = new Intent(context, ChatPrivateActivity.class);
                    intentContact.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
                    intentContact.putExtra(Constants.ASSIGNER_EXTRA, getAssigners(taskResponse, pos).get(position));
                    ((BaseActivity) context).startActivity(intentContact, TransitionScreen.DOWN_TO_UP);
                }

            }
        });
    }

    private void addGroup(List<Assigner> list, TaskResponse response) {
        Assigner assigner = new Assigner();
        assigner.setFullName(context.getString(R.string.group_chat));
        assigner.setId(response.getId());
        list.add(0, assigner);
    }

    public void killAdapter() {
        if (chatPrivateAdapter != null) chatPrivateAdapter.killListener();

    }

    private boolean checkGroup(List<Assigner> list, int pos) {
        boolean b = false;
        for (Assigner assigner : list
                ) {
            if (assigner.getFullName().equals(R.string.group_chat)) {
                b = true;
            }
        }
        return b;
    }

    private List<Assigner> getAssigners(TaskResponse taskResponse, int pos) {
        final List<Assigner> chatAssigners = new ArrayList<>();
        if (UserManager.getMyUser().getId() == taskResponse.getPoster().getId())
            chatAssigners.addAll(taskResponse.getAssignees());
        else {
            Assigner assigner = new Assigner();
            assigner.setId(taskResponse.getPoster().getId());
            assigner.setFullName(taskResponse.getPoster().getFullName());
            assigner.setPhone(taskResponse.getPoster().getPhone());
            chatAssigners.add(assigner);
        }
        if (!checkGroup(chatAssigners, pos))
            addGroup(chatAssigners, taskResponse);
        return chatAssigners;
    }

}