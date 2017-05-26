package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.TextViewHozo;

import static vn.tonish.hozo.utils.DateTimeUtils.getOnlyDateFromIso;
import static vn.tonish.hozo.utils.Utils.getNameCategoryById;

/**
 * Created by Can Tran on 14/05/2017.
 */

public class TaskAdapter extends BaseAdapter<TaskResponse, TaskAdapter.WorkHolder, LoadingHolder> {
    private final static String TAG = TaskAdapter.class.getSimpleName();
    private List<TaskResponse> taskResponses;
    private Context context;

    public TaskAdapter(Context context, List<TaskResponse> taskResponses) {
        super(context, taskResponses);
        this.context = context;
        this.taskResponses = taskResponses;
    }

    public interface TaskAdapterListener {
        public void onTaskAdapterClickListener(int position);
    }

    private TaskAdapterListener taskAdapterListener;

    public TaskAdapterListener getTaskAdapterListener() {
        return taskAdapterListener;
    }

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
        return new WorkHolder(view, context);
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
            workHolder.tvDes.setText(context.getString(R.string.find_time_start) + " " + getOnlyDateFromIso(taskResponses.get(position).getStartTime()) + context.getString(R.string.find_task_category) + getNameCategoryById(taskResponses.get(position).getCategoryId()));
            workHolder.tvPrice.setText(context.getString(R.string.all_vnd) + " " + taskResponses.get(position).getWorkerRate());
            workHolder.tvAddress.setText(context.getString(R.string.find_task_address) + " " + taskResponses.get(position).getAddress());
        }
    }

    class WorkHolder extends BaseHolder implements View.OnClickListener {
        private TextViewHozo tvName;
        private TextViewHozo tvDes;
        private TextViewHozo tvPrice;
        private TextViewHozo tvAddress;


        public WorkHolder(View itemView, final Context context) {
            super(itemView);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            tvDes = (TextViewHozo) itemView.findViewById(R.id.tv_des);
            tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);
            tvAddress = (TextViewHozo) itemView.findViewById(R.id.tv_address);
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
