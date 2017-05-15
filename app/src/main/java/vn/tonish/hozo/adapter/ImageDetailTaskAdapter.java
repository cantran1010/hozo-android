package vn.tonish.hozo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/19/2017.
 */

public class ImageDetailTaskAdapter extends android.widget.BaseAdapter{
    private static final String TAG = ImageDetailTaskAdapter.class.getName();

    private List<String> attachments;
    private Context context;

    public ImageDetailTaskAdapter(Context context,List<String> attachments){
        this.attachments = attachments;
        this.context = context;
    }

    @Override
    public int getCount() {
        return attachments.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String item = attachments.get(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.item_image_detail_task, parent, false);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.img_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Utils.displayImage(context,holder.img,item);
        return convertView;
    }

    public static class ViewHolder {
        ImageView img;
    }
}
