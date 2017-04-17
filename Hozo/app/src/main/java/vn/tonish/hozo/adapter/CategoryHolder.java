package vn.tonish.hozo.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.PostATaskActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Category;

/**
 * Created by MAC2015 on 4/13/17.
 */

public class CategoryHolder extends BaseHolder {
    public final TextView tvName;
    public final View.OnClickListener onClickListener;
    public View view;
    public Category category;

    public CategoryHolder(View itemView, final Context context) {
        super(itemView, context);
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
