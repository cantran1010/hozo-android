package vn.tonish.hozo.adapter;

import android.annotation.TargetApi;
import android.content.Context;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
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
import vn.tonish.hozo.utils.PxUtils;

/**
 * Created by LongBui on 4/19/2017.
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

    @SuppressWarnings("deprecation")
    @NonNull
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        final Image item = getItem(position);

        //LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_image_select, parent, false);
            holder = new ViewHolder();
            holder.imgImage = (ImageView) convertView.findViewById(R.id.img_image);
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
            holder.mainLayout = (RelativeLayout) convertView.findViewById(R.id.layout_main);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item.isSelected) {
            holder.imgCheck.setVisibility(View.VISIBLE);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                //noinspection deprecation
                holder.imgImage.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.img_selected_bg));
            } else {
                holder.imgImage.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_selected_bg));
            }

            int padding = (int) PxUtils.pxFromDp(getContext(), 5);
            holder.imgImage.setPadding(padding, padding, padding, padding);
        } else {
            holder.imgCheck.setVisibility(View.GONE);
            final int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                holder.imgImage.setBackgroundDrawable(ContextCompat.getDrawable(getContext(), R.drawable.img_non_selected_bg));
            } else {
                holder.imgImage.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.img_non_selected_bg));
            }

            holder.imgImage.setPadding(0, 0, 0, 0);
        }

        if (isOnlyImage) {
            holder.imgImage.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {

                    holder.imgCheck.setVisibility(View.VISIBLE);
                    item.setSelected(true);

                    for (int i = 0; i < images.size(); i++) {
                        if (i != position) {
                            images.get(i).setSelected(false);
                        }
                    }
                    notifyDataSetChanged();
                }
            });
        } else {
            holder.imgImage.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    if (item != null ? item.isSelected : false) {
                        item.setSelected(false);
                    } else {
                        item.setSelected(true);
                    }
                    notifyDataSetChanged();
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
                .load(item != null ? item.getPath() : null)
                .centerCrop().into(holder.imgImage);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgImage, imgCheck;
        RelativeLayout mainLayout;
    }
}