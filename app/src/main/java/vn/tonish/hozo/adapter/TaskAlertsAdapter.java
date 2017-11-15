package vn.tonish.hozo.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.TaskAlert;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 6/22/17.
 */

public class TaskAlertsAdapter extends RecyclerView.Adapter<TaskAlertsAdapter.ViewHolder> {
    private final static String TAG = TaskTypeAdapter.class.getSimpleName();

    private final List<TaskAlert> taskAlerts;

    public TaskAlertsAdapter(List<TaskAlert> taskAlerts) {
        this.taskAlerts = taskAlerts;

    }

    @Override
    public TaskAlertsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_task_type, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        LogUtils.d(TAG, "checkbox -:" + taskAlerts.get(pos).isSelected());
        viewHolder.chkSelected.setText(taskAlerts.get(pos).getName());
        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setChecked(taskAlerts.get(pos).isSelected());
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                taskAlerts.get(pos).setSelected(isChecked);
//                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskAlerts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);

        }

    }
}
