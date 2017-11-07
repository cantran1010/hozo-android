package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/19/2017.
 */

public class ProfileTagAdapter extends RecyclerView.Adapter<ProfileTagAdapter.MyViewHolder> {

    public ProfileTagAdapter(List<String> strings) {
        this.strings = strings;
    }

    private final List<String> strings;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_tag, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTag.setText(strings.get(position));
    }

    @Override
    public int getItemCount() {
        return strings.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextViewHozo tvTag;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTag = (TextViewHozo) itemView.findViewById(R.id.tv_tag);
        }

    }

}
