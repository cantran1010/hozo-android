package vn.tonish.hozo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Album;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by ADMIN on 4/19/2017.
 */

public class AlbumAdapter extends ArrayAdapter<Album> {
    private static final String TAG = AlbumAdapter.class.getName();

    public AlbumAdapter(Context _context, ArrayList<Album> address) {
        super(_context, R.layout.item_album, address);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Album item = getItem(position);

        LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_album, parent, false);
            holder = new ViewHolder();
            holder.imgAlbum = (ImageView) convertView.findViewById(R.id.img_album);
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(item.getName());
//        Utils.displayImage(getContext(), holder.imgAlbum, item.getCoverPath());

        Glide.with(getContext())
                .load(item.getCoverPath())
                .centerCrop().into(holder.imgAlbum);

//        holder.imgAlbum.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        return convertView;
    }

    public static class ViewHolder {
        ImageView imgAlbum;
        TextView tvName;
    }
}
