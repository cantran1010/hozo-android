package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Work;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class WorkAdapter extends BaseAdapter<Work, WorkAdapter.WorkHolder, LoadingHolder> {

    private List<Work> works;
    private Context context;

    public WorkAdapter(Context context, List<Work> works) {
        super(context, works);
        this.context = context;
        this.works = works;
    }

    @Override
    public int getItemLayout() {
        return R.layout.adapter_work;
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
            workHolder.tvName.setText(workHolder.work.getName());
            workHolder.tvDes.setText(workHolder.work.getDes());
            workHolder.tvPrice.setText(workHolder.work.getPrice());
        }
    }

    class WorkHolder extends BaseHolder {

        private Work work;
        private final TextView tvName;
        private final TextView tvDes;
        private final TextView tvPrice;

        private final View.OnClickListener onClickListener;

        private final View view;

        public WorkHolder(View itemView, final Context context) {
            super(itemView);
            this.view = itemView;
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name);
            this.tvDes = (TextView) itemView.findViewById(R.id.tv_des);
            this.tvPrice = (TextView) itemView.findViewById(R.id.tv_price);
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
