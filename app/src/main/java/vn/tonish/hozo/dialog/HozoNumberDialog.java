package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.NumberPicker;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by cantran on 5/6/2017.
 */

public class HozoNumberDialog extends BaseDialog implements View.OnClickListener {

    public interface NumberDialogListener {
        void onNumberDialogLister(int number);
    }

    private NumberDialogListener numberDialogListener;

    public NumberDialogListener getHourDialogListener() {
        return numberDialogListener;
    }

    public void setHourDialogListener(NumberDialogListener numberDialogListener) {
        this.numberDialogListener = numberDialogListener;
    }

    private NumberPicker npHour;

    private int value;
    private String title;
    private int min;
    private int max;

    public HozoNumberDialog(@NonNull Context context, String title, int min, int max) {
        super(context);
        this.title = title;
        this.min = min;
        this.max = max;
    }


    @Override
    protected int getLayout() {
        return R.layout.dialog_hozo_number;
    }

    @Override
    protected void initData() {

        ButtonHozo btnOk = findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        npHour = findViewById(R.id.np_hour);
        TextViewHozo tvTitle = findViewById(R.id.tv_title_dialog);
        tvTitle.setText(title);
        npHour.setMinValue(min);
        npHour.setMaxValue(max);
        npHour.setWrapSelectorWheel(true);
        npHour.setValue(value);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_ok:
                doDone();
                break;

        }
    }

    private void doDone() {
        if (numberDialogListener != null)
            numberDialogListener.onNumberDialogLister(npHour.getValue());
        hideView();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}