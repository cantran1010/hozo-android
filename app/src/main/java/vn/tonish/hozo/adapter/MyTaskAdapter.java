package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 14/05/2017.
 */

public class MyTaskAdapter extends BaseAdapter<TaskResponse, MyTaskAdapter.WorkHolder, LoadingHolder> {

    private final List<TaskResponse> taskResponses;
    private final Context context;

    public MyTaskAdapter(Context context, List<TaskResponse> taskResponses) {
        super(context, taskResponses);
        this.context = context;
        this.taskResponses = taskResponses;
    }

    public interface MyTaskAdapterListener {
        void onMyTaskAdapterClickListener(int position);
    }

    private MyTaskAdapterListener myTaskAdapterListener;

    public void setMyTaskAdapterListener(MyTaskAdapterListener myTaskAdapterListener) {
        this.myTaskAdapterListener = myTaskAdapterListener;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_my_task;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public WorkHolder returnItemHolder(View view) {
        return new WorkHolder(view);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkHolder) {
            WorkHolder workHolder = ((WorkHolder) holder);

            TaskResponse taskResponse = taskResponses.get(position);
            workHolder.tvName.setText(taskResponse.getTitle());

            if (Utils.formatNumber(taskResponses.get(position).getWorkerRate()).equals("0"))
                workHolder.tvPrice.setText(context.getString(R.string.non_value));
            else
                workHolder.tvPrice.setText(context.getString(R.string.vnd, Utils.formatNumber(taskResponses.get(position).getWorkerRate())));

            workHolder.tvStartTime.setText(DateTimeUtils.getOnlyDateFromIso(taskResponses.get(position).getStartTime()));

            if (taskResponse.isOnline())
                workHolder.tvAddress.setText(context.getString(R.string.online_task_address));
            else {
                String strAddress = taskResponse.getDistrict() + " - " + taskResponse.getCity();
                workHolder.tvAddress.setText(strAddress);
            }

            workHolder.ratingBar.setRating(taskResponses.get(position).getPoster().getPosterAverageRating());
            String str_bidder_count = context.getString(R.string.bidder_count, Utils.formatNumber(taskResponses.get(position).getBidderCount())) + " " + context.getString(R.string.comments, Utils.formatNumber(taskResponses.get(position).getCommentsCount()));
            workHolder.tvComment.setText(str_bidder_count);
            Utils.displayImageAvatar(context, workHolder.imgAvata, taskResponses.get(position).getPoster().getAvatar());
            if (taskResponse.getRole().equals(Constants.ROLE_TASKER)) {

                if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_canceled));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));

                    workHolder.progressBar.setVisibility(View.GONE);

                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_missed));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));

                    workHolder.progressBar.setVisibility(View.GONE);

                } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_overdue));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_overdue));
                    workHolder.progressBar.setVisibility(View.GONE);

                } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_canceled));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));

                    workHolder.progressBar.setVisibility(View.GONE);

                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_open));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_recruitment));

                    workHolder.progressBar.setVisibility(View.GONE);

                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED)
                        && (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OPEN) || taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_ASSIGNED))) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_assigned));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_received));

                    workHolder.progressBar.setVisibility(View.GONE);

                }

                else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_AWAIT_APPROVAL)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_await_approval));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_await_approval));

                    workHolder.progressBar.setVisibility(View.GONE);
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_NOT_APPROVED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_not_approved));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_not_approved));

                    workHolder.progressBar.setVisibility(View.GONE);
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_COMPLETED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_completed));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_done));

                    workHolder.progressBar.setVisibility(View.GONE);
                }

            } else if (taskResponse.getRole().equals(Constants.ROLE_POSTER)) {
                switch (taskResponse.getStatus()) {
                    case Constants.TASK_TYPE_POSTER_DRAFT:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_draft));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_drag));
                        workHolder.progressBar.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_TYPE_POSTER_OPEN:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_open));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_transparent));

                        workHolder.progressBar.setVisibility(View.VISIBLE);

                        workHolder.progressBar.setMax(taskResponse.getWorkerCount());
                        workHolder.progressBar.setProgress(taskResponse.getAssigneeCount());
                        break;
                    case Constants.TASK_TYPE_POSTER_ASSIGNED:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_assigned));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_received_poster));

                        workHolder.progressBar.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_TYPE_POSTER_COMPLETED:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_completed));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_done));

                        workHolder.progressBar.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_TYPE_POSTER_OVERDUE:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_overdue));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_overdue));

                        workHolder.progressBar.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_TYPE_POSTER_CANCELED:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_canceled));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));

                        workHolder.progressBar.setVisibility(View.GONE);
                        break;
                    case Constants.TASK_TYPE_POSTER_AWAIT_APPROVAL:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_await_approval));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_await_approval));

                        workHolder.progressBar.setVisibility(View.GONE);
                        break;

                }
            }

        }

    }

    class WorkHolder extends BaseHolder implements View.OnClickListener {
        private final CircleImageView imgAvata;
        private final TextViewHozo tvName;
        private final TextViewHozo tvStatus;
        private final TextViewHozo tvAddress;
        private final TextViewHozo tvStartTime;
        private final TextViewHozo tvComment;
        private final RatingBar ratingBar;
        private final TextViewHozo tvPrice;
        private final ProgressBar progressBar;

        public WorkHolder(View itemView) {
            super(itemView);
            imgAvata = itemView.findViewById(R.id.img_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvStartTime = itemView.findViewById(R.id.tv_start_time);
            tvComment = itemView.findViewById(R.id.tv_comment);
            tvPrice = itemView.findViewById(R.id.tv_price);
            ratingBar = itemView.findViewById(R.id.rb_rating);
            progressBar = itemView.findViewById(R.id.simpleProgressBar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myTaskAdapterListener != null)
                myTaskAdapterListener.onMyTaskAdapterClickListener(getAdapterPosition());
        }
    }
}
