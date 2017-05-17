package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.view.CompleteView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class CompleteAdapter extends RecyclerView.Adapter<CompleteAdapter.MyViewHolder> {

    private ArrayList<Assigner> assigners;

    public CompleteAdapter(ArrayList<Assigner> assigners) {
        this.assigners = assigners;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_complete, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.completeView.updateData(assigners.get(position));
    }

    @Override
    public int getItemCount() {
        return assigners.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CompleteView completeView;

        public MyViewHolder(View itemView) {
            super(itemView);
            completeView = (CompleteView) itemView.findViewById(R.id.complete_view);
        }

    }


}
