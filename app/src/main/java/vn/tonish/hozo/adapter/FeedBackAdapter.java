package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.FeedBack;

/**
 * Created by MAC2015 on 4/20/17.
 */

public class FeedBackAdapter extends BaseAdapter<
        FeedBack, FeedBackAdapter.FeedBackHolder, LoadingHolder> {

    private final Context context;
    protected final List<FeedBack> list;

    public FeedBackAdapter(Context context, List<FeedBack> list) {
        super(context, list);
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_feedback;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public FeedBackHolder returnItemHolder(View view) {
        return new FeedBackHolder(view, context);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    }

    public class FeedBackHolder extends BaseHolder {

        public FeedBackHolder(View itemView, Context context) {
            super(itemView, context);
        }
    }
}
