package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Bidder;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.BidderOpenView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class PosterOpenAdapter extends RecyclerView.Adapter<PosterOpenAdapter.MyViewHolder> {
    private final static String TAG = PosterOpenAdapter.class.getSimpleName();
    private final ArrayList<Bidder> bidders;
    private final String type;
    private int taskId;
    private int assinerCount;
    private int workerCount;
    private int posterId;

    public PosterOpenAdapter(ArrayList<Bidder> bidders, String type) {
        this.bidders = bidders;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poster_open, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bidderOpenView.setTaskId(getTaskId());
        holder.bidderOpenView.setWorkerCount(getWorkerCount());
        holder.bidderOpenView.setAssinerCount(getAssinerCount());
        holder.bidderOpenView.setPosterID(getPosterId());
        holder.bidderOpenView.updateData(bidders.get(position), type);
        LogUtils.d(TAG, "posterId:" + getPosterId());
    }

    @Override
    public int getItemCount() {
        return bidders.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final BidderOpenView bidderOpenView;

        public MyViewHolder(View itemView) {
            super(itemView);
            bidderOpenView = itemView.findViewById(R.id.bidder_open_view);
        }

    }

    private int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getAssinerCount() {
        return assinerCount;
    }

    public void setAssinerCount(int assinerCount) {
        this.assinerCount = assinerCount;
    }

    public int getWorkerCount() {
        return workerCount;
    }

    public void setWorkerCount(int workerCount) {
        this.workerCount = workerCount;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }
}
