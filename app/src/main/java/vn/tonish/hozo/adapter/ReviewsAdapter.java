package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Review;
import vn.tonish.hozo.view.ReviewsView;

/**
 * Created by Can Tran on 14/05/2017.
 */

public class ReviewsAdapter extends BaseAdapter<Review, ReviewsAdapter.MyViewHolder, LoadingHolder> {
    private ArrayList<Review> reviews;


    public ReviewsAdapter(Context context, ArrayList<Review> reviews) {
        super(context, reviews);
        this.reviews = reviews;
    }


    @Override
    public int getItemLayout() {
        return R.layout.item_freebackview;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public MyViewHolder returnItemHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder holder_ = (MyViewHolder) holder;
        holder_.reviewsView.updateData(reviews.get(position));

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }


    public class MyViewHolder extends BaseHolder {
        public final ReviewsView reviewsView;


        public MyViewHolder(View itemView) {
            super(itemView);
            reviewsView = (ReviewsView) itemView.findViewById(R.id.reviews_view);
        }
    }
}
