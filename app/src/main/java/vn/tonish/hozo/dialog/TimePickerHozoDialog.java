package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/18/2017.
 */

public class TimePickerHozoDialog extends BaseDialog implements View.OnClickListener {

    private NumberPicker npTime;
    protected Button btnOk;

    public interface OnPickLister {
        void onPick(int time);
    }

    private OnPickLister onPickLister;

    public OnPickLister getOnPickLister() {
        return onPickLister;
    }

    public void setOnPickLister(OnPickLister onPickLister) {
        this.onPickLister = onPickLister;
    }

    public TimePickerHozoDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.time_picker_dialog;
    }

    @Override
    protected void initData() {
        npTime = (NumberPicker) findViewById(R.id.np_time);

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(this);

        npTime.setMinValue(0);
        npTime.setMaxValue(24);
        npTime.setWrapSelectorWheel(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (onPickLister != null) {
                    onPickLister.onPick(npTime.getValue());
                    hideView();
                }
                break;
        }
    }
}
