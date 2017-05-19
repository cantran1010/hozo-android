package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.NumberTextWatcher;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/18/17.
 */

public class PriceSettingDialog extends BaseDialog implements View.OnClickListener {
    private ImageView imgBack;
    private EdittextHozo edtMinPrice, edtMaxPrice;
    private TextViewHozo btnReset, btnSave;
    private PriceDialogListener priceDialogListener;

    public PriceDialogListener getPriceDialogListener() {
        return priceDialogListener;
    }

    public void setPriceDialogListener(PriceDialogListener priceDialogListener) {
        this.priceDialogListener = priceDialogListener;
    }

    public PriceSettingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.price_dialog;
    }

    @Override
    protected void initData() {
        imgBack = (ImageView) findViewById(R.id.img_back);
        edtMinPrice = (EdittextHozo) findViewById(R.id.edt_min_price);
        edtMaxPrice = (EdittextHozo) findViewById(R.id.edt_max_price);
        btnReset = (TextViewHozo) findViewById(R.id.tv_reset);
        btnSave = (TextViewHozo) findViewById(R.id.tv_ok);

        imgBack.setOnClickListener(this);
        btnReset.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        edtMinPrice.addTextChangedListener(new NumberTextWatcher(edtMinPrice));
        edtMaxPrice.addTextChangedListener(new NumberTextWatcher(edtMaxPrice));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                hideView();
                break;
            case R.id.tv_reset:
                reset();
                break;
            case R.id.tv_ok:
                save();
                break;
        }

    }

    private void save() {
        if (edtMinPrice.getText().toString().trim().isEmpty()) {
            edtMinPrice.setError(getContext().getString(R.string.erro_emply_price));
        } else if (edtMaxPrice.getText().toString().trim().isEmpty()) {
            edtMaxPrice.setError(getContext().getString(R.string.erro_emply_price));
        } else {
            long minPrice = Long.valueOf(edtMinPrice.getText().toString().replace(".", ""));
            long maxPrice = Long.valueOf(edtMaxPrice.getText().toString().replace(".", ""));
            if (maxPrice < minPrice) {
                Utils.showLongToast(getContext(), getContext().getString(R.string.erro_price));
                return;
            } else {
                if (priceDialogListener != null)
                    priceDialogListener.onPriceDialogLister(minPrice, maxPrice);
                hideView();


            }
        }
    }

    private void reset() {
        edtMinPrice.setText("");
        edtMaxPrice.setText("");

    }


    public interface PriceDialogListener {
        void onPriceDialogLister(long minPrice, long maxPrice);
    }
}
