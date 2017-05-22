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
 * Created by Can Tran on 14/05/2017.
 */

public class MyTaskAdapter extends BaseAdapter<TaskResponse, MyTaskAdapter.WorkHolder, LoadingHolder> {

    private List<TaskResponse> taskResponses;
    private Context context;

    public MyTaskAdapter(Context context, List<TaskResponse> taskResponses) {
        super(context, taskResponses);
        this.context = context;
        this.taskResponses = taskResponses;
    }

    public interface MyTaskAdapterListener {
        public void onMyTaskAdapterClickListener(int position);
    }

    private MyTaskAdapterListener myTaskAdapterListener;

    public MyTaskAdapterListener getMyTaskAdapterListener() {
        return myTaskAdapterListener;
    }

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
            workHolder.tvPrice.setText(taskResponse.getCurrency());

            if (taskResponse.getRole().equals(Constants.ROLE_WORKER)) {
                if (taskResponse.getStatus().equals(Constants.TASK_STATUS_OPEN)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_open));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_recruitment));
                } else if (taskResponse.getStatus().equals(Constants.TASK_STATUS_ASSIGNED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_assigned));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_received));
                } else if (taskResponse.getStatus().equals(Constants.TASK_STATUS_COMPLETED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_worker_completed));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
                }
            } else if (taskResponse.getRole().equals(Constants.ROLE_POSTER)) {
                if (taskResponse.getStatus().equals(Constants.TASK_STATUS_OPEN)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_open));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_recruitment));
                } else if (taskResponse.getStatus().equals(Constants.TASK_STATUS_ASSIGNED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_assigned));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_received));
                } else if (taskResponse.getStatus().equals(Constants.TASK_STATUS_COMPLETED)) {
                    workHolder.tvStatus.setText(context.getString(R.string.my_task_status_poster_completed));
                    Utils.setViewBackground(workHolder.tvStatus, ContextCompat.getDrawable(context, R.drawable.bg_border_done));
                }
            }

            workHolder.tvStartTime.setText(context.getString(R.string.my_task_adapter_start_time) + " " + DateTimeUtils.getOnlyDateFromIso(taskResponse.getStartTime()));
            workHolder.tvTaskType.setText(context.getString(R.string.my_task_adapter_task_type) + " " + CategoryManager.getCategoryById(taskResponse.getCategoryId()).getName());
            workHolder.tvAddress.setText(context.getString(R.string.my_task_adapter_address) + " " + taskResponse.getAddress());
        }
    }

    class WorkHolder extends BaseHolder implements View.OnClickListener {

        private TextViewHozo tvName, tvStatus, tvAddress, tvStartTime, tvTaskType, tvPrice;

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