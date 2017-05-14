package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 4/19/2017.
 */

public class ImageAdapter extends ArrayAdapter<Image> {
    private static final String TAG = ImageAdapter.class.getName();
    private ArrayList<Image> images;
    public ImageAdapter(Context _context, ArrayList<Image> images) {
        super(_context, R.layout.item_image, images);
        this.images = images;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Image item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + (item != null ? item.toString() : null));

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder();
            holder.imgImage = (ImageView) convertView.findViewById(R.id.img_image);
            holder.imgAdd = (ImageView) convertView.findViewById(R.id.img_add);
            holder.imgRemove = (ImageView) convertView.findViewById(R.id.img_remove);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item != null && item.isAdd) {
            holder.imgAdd.setVisibility(View.VISIBLE);
            holder.imgRemove.setVisibility(View.GONE);
        } else {
            holder.imgAdd.setVisibility(View.GONE);
            holder.imgRemove.setVisibility(View.VISIBLE);
        }

        holder.imgRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.remove(position);
                notifyDataSetChanged();
            }
        });

        Glide.with(getContext())
                .load(item.getPath())
                .centerCrop().into(holder.imgImage);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgImage, imgAdd, imgRemove;
    }
}
