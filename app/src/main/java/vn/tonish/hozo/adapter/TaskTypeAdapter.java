package vn.tonish.hozo.adapter;

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

/**
 * Created by CanTran on 5/16/17.
 */

public class TaskTypeAdapter extends RecyclerView.Adapter<TaskTypeAdapter.ViewHolder> {

    private List<Category> taskTypes;

    public TaskTypeAdapter(List<Category> taskTypes) {
        this.taskTypes = taskTypes;

    }

    @Override
    public TaskTypeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_task_type, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        viewHolder.tvName.setText(taskTypes.get(pos).getName());
        viewHolder.chkSelected.setChecked(taskTypes.get(pos).isSelected());
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //set your object's last status
                taskTypes.get(pos).setSelected(isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskTypes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName;
        public CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            tvName = (TextView) itemLayoutView.findViewById(R.id.tvName);
            chkSelected = (CheckBox) itemLayoutView.findViewById(R.id.chkSelected);

        }

    }

}