package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.view.BidderView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class BidderAdapter extends RecyclerView.Adapter<BidderAdapter.MyViewHolder> {

    private ArrayList<Bidder> bidders;

    public BidderAdapter(ArrayList<Bidder> bidders){
        this.bidders = bidders;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_bidder, parent, false);
        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bidderView.updateData(bidders.get(position));
    }

    @Override
    public int getItemCount() {
        return bidders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private BidderView bidderView;

        public MyViewHolder(View itemView) {
            super(itemView);
            bidderView = (BidderView) itemView.findViewById(R.id.bidder_view);
        }

    }
}
