package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MAC2015 on 4/13/17.
 */

public abstract class BaseHolder extends RecyclerView.ViewHolder {

    public BaseHolder(View itemView, Context context) {
        super(itemView);
    }

    public BaseHolder(View itemView) {
        super(itemView);
    }
}
