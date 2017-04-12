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
import vn.tonish.hozo.database.entity.Category;

/**
 * Created by MAC2015 on 4/12/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryHolder> {


    private List<Category> categories;
    private Context context;
    private View view;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @Override
    public CategoryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.adapter_category, parent, false);
        return new CategoryHolder(view, context);
    }

    @Override
    public void onBindViewHolder(CategoryHolder holder, int position) {
        holder.category = categories.get(position);
        holder.tvName.setText(position + ") Job Title");
    }

    @Override
    public int getItemCount() {
        return (categories == null) ? 0 : categories.size();
    }

    class CategoryHolder extends RecyclerView.ViewHolder {

        private final TextView tvName;
        private final View.OnClickListener onClickListener;
        private View view;
        private Category category;

        public CategoryHolder(View itemView, final Context context) {
            super(itemView);
            view = itemView;
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, PostATaskActivity.class);
                    intent.putExtra(Constants.DATA, category);
                    context.startActivity(intent);
                }
            };
            view.setOnClickListener(onClickListener);

        }
    }
}
