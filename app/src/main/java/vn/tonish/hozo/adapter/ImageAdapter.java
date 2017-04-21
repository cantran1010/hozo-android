package vn.tonish.hozo.adapter;

import android.content.Context;
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
 * Created by ADMIN on 4/19/2017.
 */

public class ImageAdapter extends ArrayAdapter<Image> {
    private static final String TAG = ImageAdapter.class.getName();

    public interface ImageAdapterListener {
        void onImageAdapterListener();
    }

    private ImageAdapterListener imageAdapterListener;

    public ImageAdapterListener getImageAdapterListener() {
        return imageAdapterListener;
    }

    public void setImageAdapterListener(ImageAdapterListener imageAdapterListener) {
        this.imageAdapterListener = imageAdapterListener;
    }

    public ImageAdapter(Context _context, ArrayList<Image> address) {
        super(_context, R.layout.item_image, address);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Image item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder();
            holder.imgImage = (ImageView) convertView.findViewById(R.id.img_image);
            holder.imgAdd = (ImageView) convertView.findViewById(R.id.img_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item.isAdd) {
            holder.imgAdd.setVisibility(View.VISIBLE);
            holder.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (imageAdapterListener != null) imageAdapterListener.onImageAdapterListener();
                }
            });
        } else {
            holder.imgAdd.setVisibility(View.GONE);
            holder.imgAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }

        Glide.with(getContext())
                .load(item.getPath())
                .centerCrop().into(holder.imgImage);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgImage, imgAdd;
    }
}
