package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.ImageProfileResponse;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/19/2017.
 */

public class ProfileImagesAdapter extends RecyclerView.Adapter<ProfileImagesAdapter.MyViewHolder> {

    private Context context;

    public ProfileImagesAdapter(Context context, List<ImageProfileResponse> imageResponses) {
        this.imageResponses = imageResponses;
        this.context = context;
    }

    private final List<ImageProfileResponse> imageResponses;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_profile_image, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Utils.displayImage(context, holder.imgAttach, imageResponses.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return imageResponses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imgAttach;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgAttach = itemView.findViewById(R.id.img_attach);
        }

    }

}
