package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Bank;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.MyViewHolder> {

    private Context context;

    public BankAdapter(List<Bank> banks) {
        this.banks = banks;
    }

    private final List<Bank> banks;

    public interface BankAdapterLister {
        void onItemClick(int position);

        void onDelete(int position);

        void onEdit(int position);
    }

    private BankAdapterLister bankAdapterLister;

    public BankAdapterLister getBankAdapterLister() {
        return bankAdapterLister;
    }

    public void setBankAdapterLister(BankAdapterLister bankAdapterLister) {
        this.bankAdapterLister = bankAdapterLister;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_bank, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvBankName.setText(banks.get(position).getBankName());
        holder.tvVnBankName.setText(banks.get(position).getVnBankName());
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imgChecked;
        private TextViewHozo tvBankName, tvVnBankName;
        private ImageView imgDelete, imgEdit;
        private RelativeLayout rootLayout;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgChecked = itemView.findViewById(R.id.img_checked);
            tvBankName = itemView.findViewById(R.id.tv_bank_name);
            tvVnBankName = itemView.findViewById(R.id.tv_vn_bank_name);
            imgDelete = itemView.findViewById(R.id.img_delete);
            imgEdit = itemView.findViewById(R.id.img_edit);
            rootLayout = itemView.findViewById(R.id.root_layout);

            itemView.setOnClickListener(this);
            imgDelete.setOnClickListener(this);
            imgEdit.setOnClickListener(this);
            rootLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {

            switch (view.getId()) {

                case R.id.img_delete:
                    if (bankAdapterLister != null) bankAdapterLister.onDelete(getAdapterPosition());
                    break;

                case R.id.img_edit:
                    if (bankAdapterLister != null) bankAdapterLister.onEdit(getAdapterPosition());
                    break;

                case R.id.root_layout:
                    if (bankAdapterLister != null)
                        bankAdapterLister.onItemClick(getAdapterPosition());
                    break;


            }
        }

    }
}