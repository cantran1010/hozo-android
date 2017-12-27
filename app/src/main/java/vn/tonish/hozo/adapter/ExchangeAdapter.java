package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.rest.responseRes.AssignerExchangeResponse;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CheckBoxHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */

public class ExchangeAdapter extends RecyclerView.Adapter<ExchangeAdapter.MyViewHolder> {

    private static final String TAG = ExchangeAdapter.class.getSimpleName();
    private Context context;

    public ExchangeAdapter(List<AssignerExchangeResponse> assignerExchangeResponses) {
        this.assignerExchangeResponses = assignerExchangeResponses;
    }

    private List<AssignerExchangeResponse> assignerExchangeResponses;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_exchange, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        LogUtils.d(TAG, "onBindViewHolder position : " + position);
        final AssignerExchangeResponse assignerExchangeResponse = assignerExchangeResponses.get(position);
        LogUtils.d(TAG, "onBindViewHolder assignerExchangeResponse : " + assignerExchangeResponse);

        holder.tvName.setText(assignerExchangeResponse.getFullName());
        holder.tvPrice.setText(context.getString(R.string.unit_2, Utils.formatNumber(assignerExchangeResponse.getPrice())));

        final long originPrice = assignerExchangeResponse.getPrice();

        holder.imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (assignerExchangeResponse.getPrice() + 10000 <= originPrice * 1.5) {
                    assignerExchangeResponse.setPrice(assignerExchangeResponse.getPrice() + 10000);
                    holder.tvPrice.setText(context.getString(R.string.unit_2, Utils.formatNumber(assignerExchangeResponse.getPrice())));
                }

            }
        });

        holder.imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (assignerExchangeResponse.getPrice() >= 20000) {
                    assignerExchangeResponse.setPrice(assignerExchangeResponse.getPrice() - 10000);
                    holder.tvPrice.setText(context.getString(R.string.unit_2, Utils.formatNumber(assignerExchangeResponse.getPrice())));
                }

            }
        });

        holder.cbExchange.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                assignerExchangeResponse.setChecked(b);
            }
        });

        if (assignerExchangeResponse.isChecked()) holder.cbExchange.setChecked(true);
        else
            holder.cbExchange.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return assignerExchangeResponses.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUp, imgDown;
        private CheckBoxHozo cbExchange;
        private TextViewHozo tvName;
        private TextViewHozo tvPrice;

        public MyViewHolder(View itemView) {
            super(itemView);
            cbExchange = itemView.findViewById(R.id.cb_exchange);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            imgUp = itemView.findViewById(R.id.img_up);
            imgDown = itemView.findViewById(R.id.img_down);
        }

    }
}