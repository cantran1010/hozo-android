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
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeAdapter extends RecyclerView.Adapter<TaskTypeAdapter.ViewHolder> {
    private final static String TAG = TaskTypeAdapter.class.getSimpleName();
    private int countTick = 0;
    private final List<Category> taskTypes;

    public TaskTypeAdapter(List<Category> taskTypes) {
        this.taskTypes = taskTypes;

    }

    public interface CategoryListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }

    private CategoryListener listener;

    public CategoryListener getListener() {
        return listener;
    }

    public void setListener(CategoryListener listener) {
        this.listener = listener;
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
        if (countTick == 0) {
            taskTypes.get(0).setSelected(true);
            for (int i = 1; i < taskTypes.size(); i++) {
                taskTypes.get(i).setSelected(false);
            }
        }
        viewHolder.chkSelected.setText(taskTypes.get(pos).getName());
        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setChecked(taskTypes.get(pos).isSelected());

        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) listener.onCheckedChanged(buttonView, isChecked);
//                if (pos == 0 && isChecked) countTick = 0;
//                if (!taskTypes.get(pos).isSelected()) {
//                    if (pos > 0) {
//                        countTick++;
//                        taskTypes.get(0).setSelected(false);
//                        if (countTick == taskTypes.size() - 1) countTick = 0;
//                    }
//                } else {
//                    if (pos > 0) {
//                        countTick--;
//                    }
//
//                }
//                if (pos > 0) taskTypes.get(pos).setSelected(isChecked);
//                notifyDataSetChanged();

            }
        });

    }


    @Override
    public int getItemCount() {
        return taskTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            chkSelected = itemLayoutView.findViewById(R.id.chkSelected);

        }

    }

}
