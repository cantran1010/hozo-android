package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.Utils;

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
            ((CategoryHolder) holder).tvName.setText(categories.get(position).getName());
            Utils.displayImage(context, ((CategoryHolder) holder).imgPresent, categories.get(position).getPresentPath());
        } else {

        }
    }

}
