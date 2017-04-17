package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Work;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class WorkAdapter extends RecyclerView.Adapter<WorkAdapter.WorkHolder> {

    private List<Work> works;
    private Context context;

    public WorkAdapter(Context context, List<Work> works) {
        this.context = context;
        this.works = works;
    }

    private View view;

    @Override
    public WorkHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_work, parent, false);
        return new WorkHolder(view, context);
    }

    @Override
    public void onBindViewHolder(WorkHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return works == null ? 0 : works.size();
    }

    class WorkHolder extends RecyclerView.ViewHolder {

        private Work work;
        private TextView tvName;
        private TextView tvDes;

        public WorkHolder(View itemView, Context context) {
            super(itemView);
        }

    }
}
