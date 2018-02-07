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

import java.io.File;
import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.utils.DeviceUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PxUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/19/2017.
 */

public class ImageSelectAdapter extends ArrayAdapter<Image> {
    private static final String TAG = ImageSelectAdapter.class.getName();
    private boolean isOnlyImage = false;
    private final ArrayList<Image> images;
    private int countImageAttach;

    public ImageSelectAdapter(Context _context, ArrayList<Image> images, boolean isOnlyImage) {
        super(_context, R.layout.item_image_select, images);
        this.isOnlyImage = isOnlyImage;
        this.images = images;
    }

    public int getCountImageAttach() {
        return countImageAttach;
    }

    public void setCountImageAttach(int countImageAttach) {
        this.countImageAttach = countImageAttach;
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
            holder.imgImage = convertView.findViewById(R.id.img_image);
            holder.imgCheck = convertView.findViewById(R.id.img_check);
            holder.mainLayout = convertView.findViewById(R.id.layout_main);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (item != null && item.isSelected) {
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

        final File file = new File(item != null ? item.getPath() : null != null ? item != null ? item.getPath() : null : null);
        final long file_size = (file.length() / 1024 / 1024);
        LogUtils.d(TAG, "ImageSelectAdapter , file_size : " + file_size);
        LogUtils.d(TAG, "ImageSelectAdapter , file.length() : " + file.length());
        if (isOnlyImage) {

            holder.imgImage.setOnClickListener(new View.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {

                    if (file_size > Constants.MAX_FILE_SIZE) {
                        Utils.showLongToast(getContext(), getContext().getString(R.string.max_size_attach_error, Constants.MAX_FILE_SIZE), true, true);
                    } else if (file.length() == 0) {
                        Utils.showLongToast(getContext(), getContext().getString(R.string.min_size_attach_error), true, true);
                    } else {
                        holder.imgCheck.setVisibility(View.VISIBLE);
                        assert item != null;
                        item.setSelected(true);

                        for (int i = 0; i < images.size(); i++) {
                            if (i != position) {
                                images.get(i).setSelected(false);
                            }
                        }
                        notifyDataSetChanged();
                    }

                }
            });
        } else {

            holder.imgImage.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {

                    if (file_size > Constants.MAX_FILE_SIZE) {
                        Utils.showLongToast(getContext(), getContext().getString(R.string.max_size_attach_error, Constants.MAX_FILE_SIZE), true, true);
                    } else if (file.length() == 0) {
                        Utils.showLongToast(getContext(), getContext().getString(R.string.min_size_attach_error), true, true);
                    } else {
                        if (countImageSelected() + countImageAttach < Constants.MAX_IMAGE_ATTACH) {
                            if (item != null && item.isSelected) {
                                item.setSelected(false);
                            } else {
                                item.setSelected(true);
                            }
                        } else if (countImageSelected() + countImageAttach >= Constants.MAX_IMAGE_ATTACH && (item != null && item.isSelected)) {
                            item.setSelected(false);
                        } else {
                            Utils.showLongToast(getContext(), getContext().getResources().getString(R.string.max_image_attach_err, Constants.MAX_IMAGE_ATTACH), true, false);
                        }
                        notifyDataSetChanged();
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

        Utils.displayImageCenterCrop(getContext(), holder.imgImage, item != null ? item.getPath() : null);

        return convertView;
    }

    private int countImageSelected() {
        int count = 0;
        for (int i = 0, l = images.size(); i < l; i++) {
            if (images.get(i).isSelected) {
                count++;
            }
        }
        return count;
    }

    public static class ViewHolder {
        ImageView imgImage, imgCheck;
        RelativeLayout mainLayout;
    }
}