package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PostATaskActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Category;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class CategoryAdapter extends BaseAdapter<Category, CategoryHolder, LoadingHolder> {


    private List<Category> categories;
    private Context context;


    public CategoryAdapter(Context context, List<Category> categories) {
        super(context, categories);
        this.context = context;
        this.categories = categories;
    }

    // set Item layout
    @Override
    public int getItemLayout() {
        return R.layout.adapter_category;
    }

    // set Loading layout
    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public CategoryHolder returnItemHolder(View view) {
        return new CategoryHolder(view, context);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CategoryHolder) {
            ((CategoryHolder) holder).category = categories.get(position);
            ((CategoryHolder) holder).tvName.setText(position + ") Work around young buffalo!");
        } else {

        }
    }

}
