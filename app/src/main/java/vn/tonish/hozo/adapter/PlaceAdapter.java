package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.HozoPlace;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 5/3/2017.
 */

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {
    private Context context;
    private List<HozoPlace> places;

    public PlaceAdapter(Context context, List<HozoPlace> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_search, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        HozoPlace place = new HozoPlace();
        place = places.get(position);
        holder.address.setText(place.getAddress());
    }

    @Override
    public int getItemCount() {
        return places.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextViewHozo address;

        public MyViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.tv_address);
        }

    }

}
