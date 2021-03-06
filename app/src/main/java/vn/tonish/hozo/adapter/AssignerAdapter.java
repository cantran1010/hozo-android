package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.view.AssignerView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class AssignerAdapter extends RecyclerView.Adapter<AssignerAdapter.MyViewHolder> {

    private final ArrayList<Assigner> assigners;
    private final String assignType;
    private int taskId;
    private int posterID;

    public AssignerAdapter(ArrayList<Assigner> assigners, String assignType) {
        this.assigners = assigners;
        this.assignType = assignType;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assigner, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.assignerView.setTaskId(getTaskId());
        holder.assignerView.setPosterID(getPosterID());
        holder.assignerView.updateData(assigners.get(position), assignType);
    }

    @Override
    public int getItemCount() {
        return assigners.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final AssignerView assignerView;

        public MyViewHolder(View itemView) {
            super(itemView);
            assignerView = itemView.findViewById(R.id.assign_view);
        }

    }

    private int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getPosterID() {
        return posterID;
    }

    public void setPosterID(int posterID) {
        this.posterID = posterID;
    }
}
