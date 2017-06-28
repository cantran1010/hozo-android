package vn.tonish.hozo.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CanTran on 6/28/17.
 */

public class CustomArrayAdapter extends ArrayAdapter<String> implements
        Filterable {
    private List<String> list;
    private List<Integer> listInt = new ArrayList<>();
    private CustomFilter customFilter;

    public CustomArrayAdapter(Context context, int textViewResourceId, List<String> list) {
        super(context, textViewResourceId);
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

    @Override
    public CustomFilter getFilter() {
        if (customFilter == null) {
            customFilter = new CustomFilter();
        }
        return customFilter;
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