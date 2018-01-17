package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.TransactionResponse;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */

public class PaymentAdapter extends BaseAdapter<TransactionResponse, PaymentAdapter.MyViewHolder, LoadingHolder> {

    private final List<TransactionResponse> payments;
    private final Context context;

    public PaymentAdapter(Context context, List<TransactionResponse> payments) {
        super(context, payments);
        this.payments = payments;
        this.context = context;
    }

    public interface OnItemClickLister {
        void onClick(int position);
    }

    private OnItemClickLister onItemClickLister;

    public OnItemClickLister getOnItemClickLister() {
        return onItemClickLister;
    }

    public void setOnItemClickLister(OnItemClickLister onItemClickLister) {
        this.onItemClickLister = onItemClickLister;
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_payment;
    }

    @Override
    protected int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    protected MyViewHolder returnItemHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    protected LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyViewHolder) {

            MyViewHolder myViewHolder = (MyViewHolder) holder;
            String myPrice;

            TransactionResponse payment = payments.get(position);

            if (payment.getType().equals("in")) {
                myPrice = "+ " + Utils.formatNumber(payment.getAmount());
                myViewHolder.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.hozo_bg));
                myViewHolder.tvUnit.setTextColor(ContextCompat.getColor(context, R.color.hozo_bg));
            } else {
                myPrice = "- " + Utils.formatNumber(payment.getAmount());
                myViewHolder.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.hozo_red));
                myViewHolder.tvUnit.setTextColor(ContextCompat.getColor(context, R.color.hozo_red));
            }

            myViewHolder.tvPrice.setText(myPrice);
            myViewHolder.tvDate.setText(DateTimeUtils.getPaymentTime(payment.getCreatedAt()));
            myViewHolder.tvContent.setText(Utils.getContentTransaction(context, payment));
        }
    }


    public class MyViewHolder extends BaseHolder implements View.OnClickListener {

        private final TextViewHozo tvPrice;
        private final TextViewHozo tvDate;
        private final TextViewHozo tvContent;
        private final TextViewHozo tvUnit;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvContent = itemView.findViewById(R.id.tv_content);
            tvUnit = itemView.findViewById(R.id.tv_unit);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (onItemClickLister != null)
                onItemClickLister.onClick(getAdapterPosition());
        }
    }
}