package vn.tonish.hozo.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeAdapter extends RecyclerView.Adapter<TaskTypeAdapter.ViewHolder> {
    private final static String TAG = TaskTypeAdapter.class.getSimpleName();

    private final List<Category> taskTypes;

    public TaskTypeAdapter(List<Category> taskTypes) {
        this.taskTypes = taskTypes;

    }

    @Override
    public TaskTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_task_type, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        LogUtils.d(TAG, "checkbox -:" + taskTypes.get(pos).isSelected());
        viewHolder.tvName.setText(taskTypes.get(pos).getName());
        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setChecked(taskTypes.get(pos).isSelected());
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (pos == 0 && !taskTypes.get(0).isSelected()) {
                    for (Category category : taskTypes
                            ) {
                        category.setSelected(false);
                        notifyDataSetChanged();

                    }
                } else {
                    taskTypes.get(0).setSelected(false);
                    notifyDataSetChanged();
                }
                //set your object's last status
                taskTypes.get(pos).setSelected(isChecked);
//                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tvName;
        public final CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);

        }

    }

}
