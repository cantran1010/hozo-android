package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by Can Tran on 14/05/2017.
 */

public class TaskAdapter extends BaseAdapter<TaskResponse, TaskAdapter.WorkHolder, LoadingHolder> {

    private List<TaskResponse> works;
    private Context context;

    public TaskAdapter(Context context, List<TaskResponse> works) {
        super(context, works);
        this.context = context;
        this.works = works;
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
            workHolder.work = works.get(position);
            workHolder.tvName.setText(workHolder.work.getTitle());
            workHolder.tvDes.setText(workHolder.work.getDescription());
            workHolder.tvPrice.setText(workHolder.work.getCurrency());
        }
    }

    class WorkHolder extends BaseHolder {

        private TaskResponse work;
        private final TextViewHozo tvName;
        private final TextViewHozo tvDes;
        private final TextViewHozo tvPrice;

        private final View.OnClickListener onClickListener;

        private final View view;

        public WorkHolder(View itemView, final Context context) {
            super(itemView);
            this.view = itemView;
            this.tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            this.tvDes = (TextViewHozo) itemView.findViewById(R.id.tv_des);
            this.tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);
            this.onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "Coming soon...!", Toast.LENGTH_SHORT).show();
                }
            };
            this.view.setOnClickListener(onClickListener);
        }

    }
}