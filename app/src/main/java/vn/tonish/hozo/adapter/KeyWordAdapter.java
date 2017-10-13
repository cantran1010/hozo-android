package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 14/05/2017.
 */

public class KeyWordAdapter extends BaseAdapter<String, KeyWordAdapter.WorkHolder, LoadingHolder> {
    private final static String TAG = KeyWordAdapter.class.getSimpleName();
    private final List<String> keyWords;
    private final Context context;

    public KeyWordAdapter(Context context, List<String> keyWords) {
        super(context, keyWords);
        this.context = context;
        this.keyWords = keyWords;
    }


    @Override
    public int getItemLayout() {
        return R.layout.item_key_word;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public WorkHolder returnItemHolder(View view) {
        return new WorkHolder(view);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkHolder) {
            WorkHolder workHolder = ((WorkHolder) holder);
            workHolder.tvName.setText(keyWords.get(position));
        }
    }

    class WorkHolder extends BaseHolder {
        private final TextViewHozo tvName;

        public WorkHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
        }

    }
}
