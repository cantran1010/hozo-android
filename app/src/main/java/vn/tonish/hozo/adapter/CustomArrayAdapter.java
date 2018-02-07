package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 6/28/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> implements
        Filterable {
    private final List<String> list;
    private final List<Integer> listInt = new ArrayList<>();
    private CustomFilter customFilter;

    public CustomArrayAdapter(Context context, List<String> list) {
        super(context, R.layout.item_price, list);
        this.list = list;
        for (int i = 0; i < list.size(); i++)
            listInt.add(Integer.valueOf(list.get(i).replace(",", "").replace(".", "")));
    }

    @Override
    public void add(String object) {
        list.add(object);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int position) {
        return list.get(position);
    }

    @NonNull
    @Override
    public CustomFilter getFilter() {
        if (customFilter == null) {
            customFilter = new CustomFilter();
        }
        return customFilter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final String item = getItem(position);

        //LogUtils.d(TAG, "getView , item : " + item.toString());

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
            convertView = layoutInflater.inflate(R.layout.item_price, parent, false);
            holder = new ViewHolder();
            holder.tvPrice = convertView.findViewById(R.id.tv_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvPrice.setText(item);
        return convertView;
    }

    public static class ViewHolder {
        TextViewHozo tvPrice;
    }

    public void callFiltering(String term) {
        customFilter.performFiltering(term);
    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if (constraint != null) {
                results.values = listInt;
                results.count = list.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            if (results != null && results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }

}