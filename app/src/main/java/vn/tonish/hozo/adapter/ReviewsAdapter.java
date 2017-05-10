package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Feedback;
import vn.tonish.hozo.view.ReviewsView;

/**
 * Created by LongBui on 4/21/2017.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {


    private final ArrayList<Feedback> feedbacks;

    public ReviewsAdapter(ArrayList<Feedback> feedbacks) {
        this.feedbacks = feedbacks;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.reviewsView.updateData(feedbacks.get(position));
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }


    public class MyViewHolder extends BaseHolder {
        public final ReviewsView reviewsView;


        public MyViewHolder(View itemView) {
            super(itemView);
            reviewsView = (ReviewsView) itemView.findViewById(R.id.reviews_view);
        }
    }
}
