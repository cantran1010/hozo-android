package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.view.BidderOpenView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class PosterOpenAdapter extends RecyclerView.Adapter<PosterOpenAdapter.MyViewHolder> {

    private ArrayList<Bidder> bidders;

    public PosterOpenAdapter(ArrayList<Bidder> bidders){
        this.bidders = bidders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poster_open, parent, false);
        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bidderOpenView.updateData(bidders.get(position));
    }

    @Override
    public int getItemCount() {
        return bidders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private BidderOpenView bidderOpenView;

        public MyViewHolder(View itemView) {
            super(itemView);
            bidderOpenView = (BidderOpenView) itemView.findViewById(R.id.bidder_open_view);
        }

    }
}