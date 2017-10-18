package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

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
    private KeyWordListener keyWordListener;

    public KeyWordAdapter(Context context, List<String> keyWords) {
        super(context, keyWords);
        this.context = context;
        this.keyWords = keyWords;
    }


    public interface KeyWordListener {
        void OnClickListener();
    }

    public void setKeyWordListener(KeyWordListener keyWordListener) {
        this.keyWordListener = keyWordListener;
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

    class WorkHolder extends BaseHolder implements View.OnClickListener {
        private final TextViewHozo tvName;
        private final ImageView imgClose;

        public WorkHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            imgClose = itemView.findViewById(R.id.ic_close);
            imgClose.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.ic_close:
                    keyWords.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    if (keyWordListener != null)
                        keyWordListener.OnClickListener();
                    break;
            }
        }
    }
}
