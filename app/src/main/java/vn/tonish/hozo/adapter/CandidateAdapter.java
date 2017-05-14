package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.view.CandidateView;

/**
 * Created by LongBui on 5/3/2017.
 */

public class CandidateAdapter extends RecyclerView.Adapter<CandidateAdapter.MyViewHolder> {

    private ArrayList<User> users;

    public CandidateAdapter(ArrayList<User> users){
        this.users = users;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_candidate, parent, false);
        return new MyViewHolder(itemView);    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.candidateView.updateData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private CandidateView candidateView;

        public MyViewHolder(View itemView) {
            super(itemView);
            candidateView = (CandidateView) itemView.findViewById(R.id.candidate_view);
        }

    }
}
