package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.MyViewHolder> {

    private Context context;
    private List<Assigner> members;

    public MemberAdapter(Context context, List<Assigner> members) {
        this.context = context;
        this.members = members;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_member, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (position == 0) {
            holder.imgAvatar.setImageResource(R.mipmap.app_icon);
        } else
            Utils.displayImage(context, holder.imgAvatar, members.get(position).getAvatar());
        String[] names = members.get(position).getFullName().split(" ");
        holder.tvName.setText(names[names.length - 1]);
    }

    @Override
    public int getItemCount() {
        return members.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView imgAvatar;
        private final TextViewHozo tvName;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);
        }

    }
}