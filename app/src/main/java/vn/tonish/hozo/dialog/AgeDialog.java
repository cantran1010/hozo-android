package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.NumberPicker;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.ButtonHozo;

/**
 * Created by LongBui on 5/6/2017.
 */

public class AgeDialog extends BaseDialog implements View.OnClickListener {

    public interface AgeDialogListener {
        void onAgeDialogLister(int ageFrom, int ageTo);
    }

    private AgeDialogListener ageDialogListener;

    public void setAgeDialogListener(AgeDialogListener ageDialogListener) {
        this.ageDialogListener = ageDialogListener;
    }

    private NumberPicker npAgeFrom, npAgeTo;

    private int ageFrom;
    private int ageTo;

    private static final int minAge = 18;
    private static final int maxAge = 80;

    public  AgeDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.age_dialog;
    }

    @Override
    protected void initData() {

        ButtonHozo btnOk = (ButtonHozo) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        npAgeFrom = (NumberPicker) findViewById(R.id.np_age_from);
        npAgeTo = (NumberPicker) findViewById(R.id.np_age_to);

        npAgeFrom.setMinValue(minAge);
        npAgeFrom.setMaxValue(maxAge);
        npAgeFrom.setWrapSelectorWheel(true);
        npAgeFrom.setValue(ageFrom);

        npAgeTo.setMinValue(minAge);
        npAgeTo.setMaxValue(maxAge);
        npAgeTo.setWrapSelectorWheel(true);
        npAgeTo.setValue(ageTo);

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

        if (npAgeFrom.getValue() >= npAgeTo.getValue()) {
            Utils.showLongToast(getContext(), getContext().getString(R.string.select_age_error));
            return;
        }

        if (ageDialogListener != null)
            ageDialogListener.onAgeDialogLister(npAgeFrom.getValue(), npAgeTo.getValue());
        hideView();

    }

    public void setAgeFrom(int ageFrom) {
        this.ageFrom = ageFrom;
    }


    public void setAgeTo(int ageTo) {
        this.ageTo = ageTo;
    }
}
