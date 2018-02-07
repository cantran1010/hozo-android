package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 14/05/2017.
 */

public class TaskAdapter extends BaseAdapter<TaskResponse, TaskAdapter.WorkHolder, LoadingHolder> {
    private final static String TAG = TaskAdapter.class.getSimpleName();
    private final List<TaskResponse> taskResponses;
    private final Context context;

    public TaskAdapter(Context context, List<TaskResponse> taskResponses) {
        super(context, taskResponses);
        this.context = context;
        this.taskResponses = taskResponses;
    }

    public interface TaskAdapterListener {
        void onTaskAdapterClickListener(int position);
    }

    private TaskAdapterListener taskAdapterListener;


    public void setTaskAdapterListener(TaskAdapterListener taskAdapterListener) {
        this.taskAdapterListener = taskAdapterListener;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_find_task;
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
            workHolder.tvPrice.setText(context.getString(R.string.vnd, Utils.formatNumber(taskResponse.getWorkerRate())));
            workHolder.tvStartTime.setText(DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
            if (taskResponse.getWorkerCount() == taskResponse.getAssigneeCount())
                workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_assigned));
            else
                workHolder.tvStatus.setText(context.getString(R.string.make_an_offer_status));

            if (taskResponse.isOnline())
                workHolder.tvAddress.setText(context.getString(R.string.online_task_address));
            else {
                String strAddress = taskResponse.getDistrict() + " - " + taskResponse.getCity();
                if (strAddress.startsWith(" - "))
                    strAddress = strAddress.substring(3, strAddress.length());
                workHolder.tvAddress.setText(strAddress);
            }

            workHolder.progressBar.setMax(taskResponse.getWorkerCount());
            workHolder.progressBar.setProgress(taskResponse.getAssigneeCount());
            workHolder.ratingBar.setRating(taskResponse.getPoster().getPosterAverageRating());
            String str_bidder_count = context.getString(R.string.bidder_count, Utils.formatNumber(taskResponse.getBidderCount())) + " " + context.getString(R.string.comments, Utils.formatNumber(taskResponse.getCommentsCount()));
            workHolder.tvComment.setText(str_bidder_count);
            Utils.displayImageAvatar(context, workHolder.imgAvata, taskResponse.getPoster().getAvatar());
        }
    }

    class WorkHolder extends BaseHolder implements View.OnClickListener {
        private final CircleImageView imgAvata;
        private final TextViewHozo tvName;
        private final TextViewHozo tvAddress;
        private final TextViewHozo tvStatus;
        private final TextViewHozo tvStartTime;
        private final ProgressBar progressBar;
        private final RatingBar ratingBar;
        private final TextViewHozo tvPrice;
        private final TextViewHozo tvComment;

        public WorkHolder(View itemView) {
            super(itemView);
            imgAvata = itemView.findViewById(R.id.img_avatar);
            tvName = itemView.findViewById(R.id.tv_name);
            tvAddress = itemView.findViewById(R.id.tv_address);
            tvStartTime = itemView.findViewById(R.id.tv_start_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            progressBar = itemView.findViewById(R.id.simpleProgressBar);
            ratingBar = itemView.findViewById(R.id.rb_rating);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvComment = itemView.findViewById(R.id.tv_comment);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (taskAdapterListener != null) {
                taskAdapterListener.onTaskAdapterClickListener(getAdapterPosition());
            }

        }

    }
}
