package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ReviewsAdapter;
import vn.tonish.hozo.database.entity.ReviewEntity;

/**
 * Created by CanTran on 14/05/2017.
 */

public class ReviewsListView extends RelativeLayout {

    private RecyclerView rcvComment;

    public ReviewsListView(Context context) {
        super(context);
        init();
    }

    public ReviewsListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ReviewsListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ReviewsListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_comment_full, this, true);

        rcvComment = (RecyclerView) findViewById(R.id.rcv_comment);
    }

    public void updateData(ArrayList<ReviewEntity> reviews) {
        ReviewsAdapter reviewsAdapter = new ReviewsAdapter(getContext(), reviews);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvComment.setLayoutManager(layoutManager);
        rcvComment.setAdapter(reviewsAdapter);
        reviewsAdapter.notifyDataSetChanged();
        reviewsAdapter.stopLoadMore();
    }
}