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

    public static final int VIEW_TYPE_LOADING = 1;
    public static final int VIEW_TYPE_ITEM = 0;

    public void stopLoadMore() {
        isLoad = false;
    }

    public boolean isLoad = true;

    public Context context;
    public View view, loading;

    public List<T> list;

    public BaseAdapter(Context context, List<T> list) {
        this.context = context;
        this.list = list;
    }

    public abstract int getItemLayout();

    public abstract int getLoadingLayout();

    public abstract H returnItemHolder(View view);

    public abstract L returnLoadingHolder(View view);

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            view = LayoutInflater.from(context).inflate(getItemLayout(), parent, false);
            return returnItemHolder(view);
        } else {
            loading = LayoutInflater.from(context).inflate(getLoadingLayout(), parent, false);
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
