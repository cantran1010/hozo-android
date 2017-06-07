package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.manager.CategoryManager;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
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
            LogUtils.d(TAG, "adapter " + taskResponses.get(position).toString());
            workHolder.tvName.setText(taskResponses.get(position).getTitle());
            workHolder.tvPrice.setText(Utils.formatNumber(taskResponses.get(position).getWorkerRate() * taskResponses.get(position).getWorkerCount()) + " " + context.getString(R.string.currency));
            workHolder.tvStartTime.setText(context.getString(R.string.my_task_adapter_start_time) + " " + DateTimeUtils.getOnlyDateFromIso(taskResponses.get(position).getStartTime()));
            workHolder.tvTaskType.setText(context.getString(R.string.my_task_adapter_task_type) + " " + CategoryManager.getCategoryById(taskResponses.get(position).getCategoryId()).getName());
            workHolder.tvAddress.setText(context.getString(R.string.my_task_adapter_address) + " " + taskResponses.get(position).getAddress());
        }
    }

    class WorkHolder extends BaseHolder implements View.OnClickListener {
        private final TextViewHozo tvName;
        private final TextViewHozo tvAddress;
        private final TextViewHozo tvStartTime;
        private final TextViewHozo tvTaskType;
        private final TextViewHozo tvPrice;


        public WorkHolder(View itemView) {
            super(itemView);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            tvAddress = (TextViewHozo) itemView.findViewById(R.id.tv_address);
            tvStartTime = (TextViewHozo) itemView.findViewById(R.id.tv_start_time);
            tvTaskType = (TextViewHozo) itemView.findViewById(R.id.tv_task_type);
            tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);
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
