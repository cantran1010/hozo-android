package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.Assigner;
import vn.tonish.hozo.view.AssignerCallView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class AssignerCallAdapter extends RecyclerView.Adapter<AssignerCallAdapter.MyViewHolder> {

    private ArrayList<Assigner> assigners;

    public AssignerCallAdapter(ArrayList<Assigner> assigners) {
        this.assigners = assigners;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_assigner_call, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.assignerCallView.updateData(assigners.get(position));
    }

    @Override
    public int getItemCount() {
        return assigners.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private AssignerCallView assignerCallView;

        public MyViewHolder(View itemView) {
            super(itemView);
            assignerCallView = (AssignerCallView) itemView.findViewById(R.id.assign_call_view);
        }

    }
}
