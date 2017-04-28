package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Album;
import vn.tonish.hozo.utils.DeviceUtils;

/**
 * Created by LongBui on 4/19/2017.
 */

public class AlbumAdapter extends ArrayAdapter<Album> {
    private static final String TAG = AlbumAdapter.class.getName();

    public AlbumAdapter(Context _context, ArrayList<Album> address) {
        super(_context, R.layout.item_album, address);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Album item = getItem(position);

        //LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_album, parent, false);
            holder = new ViewHolder();
            holder.imgAlbum = (ImageView) convertView.findViewById(R.id.img_album);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            holder.mainLayout = (RelativeLayout) convertView.findViewById(R.id.layout_main);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(item != null ? item.getName() : "");
//        Utils.displayImage(getContext(), holder.imgAlbum, item.getCoverPath());

        Glide.with(getContext())
                .load(item.getCoverPath())
                .centerCrop().into(holder.imgAlbum);

        DeviceUtils.DisplayInfo displayInfo = DeviceUtils.getDisplayInfo(getContext());
        int whImage = displayInfo.getWidth() / 3;

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.mainLayout.getLayoutParams();
        params.width = whImage;
        params.height = whImage;
        holder.mainLayout.setLayoutParams(params);

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgAlbum;
        TextView tvName;
        RelativeLayout mainLayout;
    }
}