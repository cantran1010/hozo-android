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
    private final List<Category> taskTypes;

    public TaskTypeAdapter(List<Category> taskTypes) {
        this.taskTypes = taskTypes;

    }

    public interface CategoryListener {
        void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
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
        viewHolder.chkSelected.setText(taskTypes.get(pos).getName());
        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setChecked(taskTypes.get(pos).isSelected());
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.d(TAG, "buttonView isChecked: " + isChecked);
                if (buttonView.getText().toString().equalsIgnoreCase("Tất cả") && isChecked) {
                    buttonView.setChecked(true);
                    setAllStatus();
                } else if (!isChecked && getCountTick() == 1) {
                    buttonView.setChecked(true);
                    taskTypes.get(pos).setSelected(true);
                } else if (isChecked && !buttonView.getText().toString().equalsIgnoreCase("Tất cả") && (getCountTick() == taskTypes.size() - 2)) {
                    setAllStatus();
                } else {
                    taskTypes.get(0).setSelected(false);
                    taskTypes.get(pos).setSelected(isChecked);
                }
                notifyDataSetChanged();
                if (listener != null) listener.onCheckedChanged(buttonView, isChecked);
            }
        });

    }

    private void setAllStatus() {
        for (int i = 1; i < taskTypes.size(); i++) {
            taskTypes.get(i).setSelected(false);
        }
        taskTypes.get(0).setSelected(true);
    }

    private int getCountTick() {
        int n = 0;
        for (Category s : taskTypes
                ) {
            if (s.isSelected()) n++;
        }
        return n;
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