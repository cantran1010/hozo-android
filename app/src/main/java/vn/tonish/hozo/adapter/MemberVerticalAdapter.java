package vn.tonish.hozo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;
import java.util.Map;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Member;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by cantran on 4/21/2017.
 */

public class MemberVerticalAdapter extends RecyclerView.Adapter<MemberVerticalAdapter.MyViewHolder> {

    private static final String TAG = MemberVerticalAdapter.class.getSimpleName();
    private final List<Member> members;
    private final Context context;

    public interface ChatPrivateListener {
        void onClick(int position);
    }

    private ChatPrivateListener chatPrivateListener;

    public ChatPrivateListener getChatPrivateListener() {
        return chatPrivateListener;
    }

    public void setMemberVerticalListener(ChatPrivateListener chatPrivateListener) {
        this.chatPrivateListener = chatPrivateListener;
    }

    public MemberVerticalAdapter(final Context context, List<Member> members) {
        this.context = context;
        this.members = members;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat_private_room, parent, false);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Member member = members.get(position);
        if (member.getFull_name().equalsIgnoreCase(context.getString(R.string.group_chat)))
            holder.imgAvatar.setImageResource(R.mipmap.app_icon);
        else
            Utils.displayImageAvatar(context, holder.imgAvatar, member.getAvatar());
        holder.tvTitle.setText(member.getFull_name());
        if (member.getMessage() != null) {
            if (member.getMessage().getType() == 1)
                holder.tvLastMsg.setText(context.getString(R.string.send_img));
            else
                holder.tvLastMsg.setText(member.getMessage().getMessage());
            Map<String, Boolean> map = member.getMessage().getReads();
            if (map != null)
                if (map != null && map.containsKey(String.valueOf(UserManager.getMyUser().getId()))) {
                    holder.imgDot.setVisibility(View.GONE);
//            assigners.get(pos).setRead(false);
                } else {
                    holder.imgDot.setVisibility(View.VISIBLE);
//            assigners.get(pos).setRead(true);
                }
            holder.tvTimeAgo.setVisibility(View.VISIBLE);
            holder.tvTimeAgo.setText(DateTimeUtils.getTimeAgo(member.getMessage().getCreated_atLong(true), context));
        }

    }

    @Override
    public int getItemCount() {
        return members.size();
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
}