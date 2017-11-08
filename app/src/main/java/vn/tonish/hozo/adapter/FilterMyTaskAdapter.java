package vn.tonish.hozo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.database.entity.StatusEntity;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.database.manager.StatusManager.insertIsSelectedStatus;

/**
 * Created by CanTran on 5/16/17.
 */

public class FilterMyTaskAdapter extends RecyclerView.Adapter<FilterMyTaskAdapter.ViewHolder> {
    private Context context;
    private final static String TAG = TaskTypeAdapter.class.getSimpleName();
    private int countTick = 0;
    private final List<StatusEntity> statuses;

    public FilterMyTaskAdapter(Context context, List<StatusEntity> statuses) {
        this.context = context;
        this.statuses = statuses;
    }

    @Override
    public FilterMyTaskAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        @SuppressLint("InflateParams") View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_task_type, null);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        viewHolder.chkSelected.setText(statuses.get(pos).getName());
        viewHolder.chkSelected.setOnCheckedChangeListener(null);
        viewHolder.chkSelected.setChecked(statuses.get(pos).isSelected());
        viewHolder.chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LogUtils.d(TAG, "buttonView isChecked: " + isChecked);
                if (buttonView.getText().toString().equalsIgnoreCase(context.getString(R.string.hozo_all)) && isChecked) {
                    buttonView.setChecked(isChecked);
                    setAllStatus();
                } else if (!isChecked && getCountTick() == 1) {
                    buttonView.setChecked(true);
                    insertIsSelectedStatus(statuses.get(pos), true);
                } else if (isChecked && !buttonView.getText().toString().equalsIgnoreCase(context.getString(R.string.hozo_all)) && (getCountTick() == statuses.size() - 2)) {
                    setAllStatus();
                } else {
                    insertIsSelectedStatus(statuses.get(0), false);
                    insertIsSelectedStatus(statuses.get(pos), isChecked);
                }
                notifyDataSetChanged();
            }
        });

    }

    private void setAllStatus() {
        for (int i = 1; i < statuses.size(); i++) {
            insertIsSelectedStatus(statuses.get(i), false);
        }
        insertIsSelectedStatus(statuses.get(0), true);
    }

    private int getCountTick() {
        int n = 0;
        for (StatusEntity s : statuses
                ) {
            if (s.isSelected()) n++;
        }
        return n;
    }


    @Override
    public int getItemCount() {
        return statuses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            chkSelected = itemLayoutView.findViewById(R.id.chkSelected);

        }

    }

}