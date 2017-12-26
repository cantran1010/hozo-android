package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.entity.ReviewEntity;
import vn.tonish.hozo.rest.responseRes.BidResponse;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.ReviewsListView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 11/21/17.
 */

public class ViewPageAssignAdapter extends PagerAdapter {
    private static final String TAG = ViewPageAssignAdapter.class.getSimpleName();
    // Declare Variables
    private final Context context;
    private final int taskID;
    private final List<BidResponse> bidResponses;


    public ViewPageAssignAdapter(Context context, List<BidResponse> bidResponses, int taskID) {
        this.context = context;
        this.bidResponses = bidResponses;
        this.taskID = taskID;

    }

    @Override
    public int getCount() {
        return bidResponses.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        BidResponse bidResponse = bidResponses.get(position);
        final TextViewHozo tvAssigner, tvAssignerCount, tvBidderCount, tvName, tvDes;
        final TextViewHozo tvRatingCount, tvBidsCount, tvComplex;
        final TextViewHozo tv5star, tv4star, tv3star, tv2star, tv1star;
        final TextViewHozo tvPrice, tvDesMsg, tvShowReviews, tvMoreReviews;
        final ProgressBar assignProgress;
        final ReviewsListView reviewsListView;
        final RatingBar rbRating, rbRating5, rbRating4, rbRating3, rbRating2, rbRating1;
        final CircleImageView imgAvatarAssign, imgAvatarDes;

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        View itemView = inflater.inflate(R.layout.viewpager_assign_item, container,
                false);
        tvAssigner = (TextViewHozo) itemView.findViewById(R.id.tv_assigner);
        tvAssignerCount = (TextViewHozo) itemView.findViewById(R.id.tv_assigner_count);
        tvBidderCount = (TextViewHozo) itemView.findViewById(R.id.tv_bidder_count);
        tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
        tvDes = (TextViewHozo) itemView.findViewById(R.id.tv_des);
        tvRatingCount = (TextViewHozo) itemView.findViewById(R.id.tv_rating_count);
        tvBidsCount = (TextViewHozo) itemView.findViewById(R.id.tv_bids_count);
        tvComplex = (TextViewHozo) itemView.findViewById(R.id.tv_complex);
        tv5star = (TextViewHozo) itemView.findViewById(R.id.tv_5star);
        tv4star = (TextViewHozo) itemView.findViewById(R.id.tv_4star);
        tv3star = (TextViewHozo) itemView.findViewById(R.id.tv_3star);
        tv2star = (TextViewHozo) itemView.findViewById(R.id.tv_2star);
        tv1star = (TextViewHozo) itemView.findViewById(R.id.tv_1star);
        tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);
        tvDesMsg = (TextViewHozo) itemView.findViewById(R.id.tv_des_msg);
        tvShowReviews = (TextViewHozo) itemView.findViewById(R.id.tv_show_reviews);
        tvMoreReviews = (TextViewHozo) itemView.findViewById(R.id.tv_more_reviews);
        assignProgress = (ProgressBar) itemView.findViewById(R.id.assign_progress);
        reviewsListView = (ReviewsListView) itemView.findViewById(R.id.rcv_reviews);
        rbRating = (RatingBar) itemView.findViewById(R.id.rb_rating);
        rbRating5 = (RatingBar) itemView.findViewById(R.id.rb_rating5);
        rbRating4 = (RatingBar) itemView.findViewById(R.id.rb_rating4);
        rbRating3 = (RatingBar) itemView.findViewById(R.id.rb_rating3);
        rbRating2 = (RatingBar) itemView.findViewById(R.id.rb_rating2);
        rbRating1 = (RatingBar) itemView.findViewById(R.id.rb_rating1);
        imgAvatarAssign = (CircleImageView) itemView.findViewById(R.id.img_avatar_assign);
        imgAvatarDes = (CircleImageView) itemView.findViewById(R.id.avatar_des);

        String title = formatTitle(position + 1) + context.getString(R.string.slash) + formatTitle(getCount());
        tvBidderCount.setText(title);
        Utils.displayImageAvatar(context, imgAvatarAssign, bidResponse.getAvatar());
        tvName.setText(bidResponse.getFullName());
        tvDes.setText(bidResponse.getDescription());
        rbRating.setRating(bidResponse.getTaskerAverageRating());
        tvRatingCount.setText(context.getString(R.string.reviews_count, bidResponse.getTaskerRatingCount()));
        tvBidsCount.setText(context.getString(R.string.tasks_count, bidResponse.getTaskerCount()));
        tvComplex.setText(context.getString(R.string.tasks_complex, Math.round(100 * bidResponse.getTaskerDoneRate())));
        if (bidResponse.getTaskerRatings().get(0) > 0) {
            rbRating1.setRating(5);
            tv1star.setText(String.valueOf(bidResponse.getTaskerRatings().get(0)));
        } else {
            rbRating1.setRating(0);
            tv1star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(1) > 0) {
            rbRating2.setRating(5);
            tv2star.setText(String.valueOf(bidResponse.getTaskerRatings().get(1)));
        } else {
            rbRating2.setRating(0);
            tv2star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(2) > 0) {
            rbRating3.setRating(5);
            tv3star.setText(String.valueOf(bidResponse.getTaskerRatings().get(2)));
        } else {
            rbRating3.setRating(0);
            tv3star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(3) > 0) {
            rbRating4.setRating(5);
            tv4star.setText(String.valueOf(bidResponse.getTaskerRatings().get(3)));
        } else {
            rbRating1.setRating(0);
            tv1star.setText("0");
        }
        if (bidResponse.getTaskerRatings().get(4) > 0) {
            rbRating5.setRating(5);
            tv5star.setText(String.valueOf(bidResponse.getTaskerRatings().get(4)));
        } else {
            rbRating5.setRating(0);
            tv5star.setText("0");
        }
        tvPrice.setText(String.valueOf(bidResponse.getPrice()));
        tvDesMsg.setText(bidResponse.getDescription());
        Utils.displayImageAvatar(context, imgAvatarDes, bidResponse.getAvatar());
        if (bidResponse.getTaskerRatingCount() > 0) {
            tvShowReviews.setVisibility(View.VISIBLE);
            tvShowReviews.setText(context.getString(R.string.bidder_reviews, bidResponse.getTaskerRatingCount()));
            reviewsListView.updateData((ArrayList<ReviewEntity>) bidResponse.getReviews());
            if (bidResponse.getTaskerRatingCount() > 5)
                tvMoreReviews.setVisibility(View.VISIBLE);
            else tvMoreReviews.setVisibility(View.GONE);
        } else {
            tvShowReviews.setVisibility(View.GONE);
        }

        ((ViewPager) container).addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);

    }

    private String formatTitle(int pos) {
        if (pos < 10) return "0" + pos;
        else return String.valueOf(pos);
    }


}