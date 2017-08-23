package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.NumberPicker;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by LongBui on 5/6/2017.
 */

public class HourDialog extends BaseDialog implements View.OnClickListener {

    public interface HourDialogListener {
        void onHourDialogLister(int hour);
    }

    private HourDialogListener hourDialogListener;

    public HourDialogListener getHourDialogListener() {
        return hourDialogListener;
    }

    public void setHourDialogListener(HourDialogListener hourDialogListener) {
        this.hourDialogListener = hourDialogListener;
    }

    private NumberPicker npHour;

    private int hour;

    private static final int minHour = 1;
    private static final int maxHour = 12;

    public HourDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_hour;
    }

    @Override
    protected void initData() {

        ButtonHozo btnOk =findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        npHour = findViewById(R.id.np_hour);
        npHour.setMinValue(minHour);
        npHour.setMaxValue(maxHour);
        npHour.setWrapSelectorWheel(true);
        npHour.setValue(hour);

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
        if (hourDialogListener != null)
            hourDialogListener.onHourDialogLister(npHour.getValue());
        hideView();
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
