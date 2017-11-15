package vn.tonish.hozo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import vn.tonish.hozo.R;

/**
 * Created by CanTran on 11/2/17.
 */

public class HozoSpinnerAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> list;

    public HozoSpinnerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint({"InflateParams", "ViewHolder"})
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.item_spinner_hozo, null);
        TextView names = view.findViewById(R.id.textView);
        names.setText(list.get(i));
        return view;
    }

}