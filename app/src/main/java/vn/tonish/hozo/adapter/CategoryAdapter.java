package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Category;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    private Context context;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    private final List<Category> categories;

    public interface CategoryAdapterLister {
        void onCallBack(int position);
    }

    private CategoryAdapterLister categoryAdapterLister;


    public void setCategoryAdapterLister(CategoryAdapterLister categoryAdapterLister) {
        this.categoryAdapterLister = categoryAdapterLister;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_category, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Utils.displayImage(context, holder.imgPresent, categories.get(position).getPresentPath());
        holder.tvName.setText(categories.get(position).getName());
        holder.tvDes.setText(categories.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CircleImageView imgPresent;
        private final TextViewHozo tvName;
        private final TextViewHozo tvDes;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgPresent = (CircleImageView) itemView.findViewById(R.id.img_present);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            tvDes = (TextViewHozo) itemView.findViewById(R.id.tv_description);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (categoryAdapterLister != null)
                categoryAdapterLister.onCallBack(getAdapterPosition());
        }

    }
}