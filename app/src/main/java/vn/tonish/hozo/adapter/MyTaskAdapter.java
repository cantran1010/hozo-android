package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
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
            String strPrice = Utils.formatNumber(taskResponse.getWorkerRate() * taskResponse.getWorkerCount()) + " " + context.getString(R.string.currency);
            workHolder.tvPrice.setText(strPrice);
            workHolder.tvPrice.setText(context.getString(R.string.my_task_price, Utils.formatNumber(taskResponse.getWorkerRate())));

            if (taskResponse.getRole().equals(Constants.ROLE_TASKER)) {
                if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_OVERDUE)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_overdue));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));
                } else if (taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_CANCELED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_canceled));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_PENDING)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_open));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_recruitment));
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && !taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_assigned));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_received));
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_ACCEPTED) && taskResponse.getStatus().equals(Constants.TASK_TYPE_POSTER_COMPLETED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_completed));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_MISSED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_missed));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));
                } else if (taskResponse.getOfferStatus().equals(Constants.TASK_TYPE_BIDDER_CANCELED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_canceled));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));
                }
            } else if (taskResponse.getRole().equals(Constants.ROLE_POSTER)) {
                switch (taskResponse.getStatus()) {
                    case Constants.TASK_TYPE_POSTER_OPEN:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_open));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_recruitment));
                        break;
                    case Constants.TASK_TYPE_POSTER_ASSIGNED:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_assigned));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_received));
                        break;
                    case Constants.TASK_TYPE_POSTER_COMPLETED:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_completed));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
                        break;
                    case Constants.TASK_TYPE_POSTER_OVERDUE:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_overdue));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));
                        break;
                    case Constants.TASK_TYPE_POSTER_CANCELED:
                        workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_canceled));
                        Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_missed));
                        break;
                }
            }

            workHolder.tvStartTime.setText(context.getString(R.string.my_task_adapter_start_time, DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime())));
            workHolder.tvTaskType.setText(context.getString(R.string.my_task_adapter_task_type, CategoryManager.getCategoryById(taskResponse.getCategoryId()).getName()));
            workHolder.tvAddress.setText(context.getString(R.string.my_task_adapter_address, taskResponse.getAddress()));
        }
    }

    class WorkHolder extends BaseHolder implements View.OnClickListener {

        private final TextViewHozo tvName;
        private final TextViewHozo tvStatus;
        private final TextViewHozo tvAddress;
        private final TextViewHozo tvStartTime;
        private final TextViewHozo tvTaskType;
        private final TextViewHozo tvPrice;

        public WorkHolder(View itemView) {
            super(itemView);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            tvStatus = (TextViewHozo) itemView.findViewById(R.id.tv_status);
            tvAddress = (TextViewHozo) itemView.findViewById(R.id.tv_address);
            tvStartTime = (TextViewHozo) itemView.findViewById(R.id.tv_start_time);
            tvTaskType = (TextViewHozo) itemView.findViewById(R.id.tv_task_type);
            tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (myTaskAdapterListener != null)
                myTaskAdapterListener.onMyTaskAdapterClickListener(getAdapterPosition());
        }
    }
}
