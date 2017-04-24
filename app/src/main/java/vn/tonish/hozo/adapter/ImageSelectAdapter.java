package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.utils.DeviceUtils;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by ADMIN on 4/19/2017.
 */

public class ImageSelectAdapter extends ArrayAdapter<Image> {
    private static final String TAG = ImageSelectAdapter.class.getName();
    private boolean isOnlyImage = false;
    private final ArrayList<Image> images;

    public ImageSelectAdapter(Context _context, ArrayList<Image> images, boolean isOnlyImage) {
        super(_context, R.layout.item_image_select, images);
        this.isOnlyImage = isOnlyImage;
        this.images = images;
    }

    @Override
    public View getView(@NonNull final int position, View convertView, ViewGroup parent) {
        final Image item = getItem(position);

        //LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_image_select, parent, false);
            holder = new ViewHolder();
            holder.imgImage = (ImageView) convertView.findViewById(R.id.img_image);
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item.isSelected) {
            holder.imgCheck.setVisibility(View.VISIBLE);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
        }

        if (isOnlyImage) {
            holder.imgImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    holder.imgCheck.setVisibility(View.VISIBLE);
                    item.setSelected(true);

                    for (int i = 0; i < images.size(); i++) {
                        if (i != position) {
                            images.get(i).setSelected(false);
                        }
                    }

                }
            });
        } else {
            holder.imgImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item.isSelected) {
                        holder.imgCheck.setVisibility(View.GONE);
                        item.setSelected(false);
                    } else {
                        holder.imgCheck.setVisibility(View.VISIBLE);
                        item.setSelected(true);
                    }
                }
            });
        }


        DeviceUtils.DisplayInfo displayInfo = DeviceUtils.getDisplayInfo(getContext());

        int whImage = displayInfo.getWidth() / 3;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.imgImage.getLayoutParams();
        params.width = whImage;
        params.height = whImage;
        holder.imgImage.setLayoutParams(params);

        Glide.with(getContext())
                .load(item.getPath())
                .centerCrop().into(holder.imgImage);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgImage, imgCheck;
    }
}
