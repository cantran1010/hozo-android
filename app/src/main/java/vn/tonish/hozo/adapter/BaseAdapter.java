package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by HuyQuynh on 4/12/17.
 */

public abstract class BaseAdapter<T, H extends BaseHolder, L extends BaseHolder> extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_LOADING = 1;
    private static final int VIEW_TYPE_ITEM = 0;

    public void stopLoadMore() {
        isLoad = false;
    }
    public void onLoadMore() {
        isLoad = true;
    }
    private boolean isLoad = true;

    final Context context;

    private final List<T> list;

    BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    protected abstract int getItemLayout();

    protected abstract int getLoadingLayout();

    protected abstract H returnItemHolder(View view);

    protected abstract L returnLoadingHolder(View view);

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(context).inflate(getItemLayout(), parent, false);
            return returnItemHolder(view);
        } else {
            View loading = LayoutInflater.from(context).inflate(getLoadingLayout(), parent, false);
            return returnLoadingHolder(loading);
        }
    }

    @Override
    public int getItemCount() {
        if (isLoad) {
            return list == null ? 0 : list.size() + 1;
        } else return list == null ? 0 : list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position >= list.size() ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

}
