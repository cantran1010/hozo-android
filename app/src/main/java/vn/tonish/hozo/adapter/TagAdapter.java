package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TagResponse;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/19/2017.
 */

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {

    public TagAdapter(List<TagResponse> tagResponses) {
        this.tagResponses = tagResponses;
    }

    private final List<TagResponse> tagResponses;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tag, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvTag.setText(tagResponses.get(position).getValue());

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagResponses.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return tagResponses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextViewHozo tvTag;
        private ImageView imgDelete;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            imgDelete = itemView.findViewById(R.id.img_delete);
        }

    }

}
