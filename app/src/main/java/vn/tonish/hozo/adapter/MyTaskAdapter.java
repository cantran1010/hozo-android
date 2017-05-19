package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
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


            workHolder.tvName.setText(taskResponses.get(position).getTitle());
            workHolder.tvAddress.setText(taskResponses.get(position).getAddress());
            workHolder.tvStatus.setText(taskResponses.get(position).getStatus());
            workHolder.tvPrice.setText(taskResponses.get(position).getCurrency());

            workHolder.tvDes.setText(taskResponses.get(position).getStartTime());
        }
    }

    class WorkHolder extends BaseHolder {

        private TextViewHozo tvName, tvStatus, tvAddress, tvDes, tvPrice;

        public WorkHolder(View itemView, final Context context) {
            super(itemView);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            tvStatus = (TextViewHozo) itemView.findViewById(R.id.tv_status);
            tvAddress = (TextViewHozo) itemView.findViewById(R.id.tv_address);
            tvDes = (TextViewHozo) itemView.findViewById(R.id.tv_des);
            tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);

        }

    }
}
