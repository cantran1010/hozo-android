package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import vn.tonish.hozo.R;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/18/17.
 */

public class GenderDialog extends BaseDialog implements View.OnClickListener {
    private TextViewHozo tvCancel, tvOk;
    private RadioGroup rgGender;

    public interface AgeDialogListener {
        void onAgeDialogLister(String gender);
    }

    private AgeDialogListener ageDialogListener;

    public AgeDialogListener getAgeDialogListener() {
        return ageDialogListener;
    }

    public void setAgeDialogListener(AgeDialogListener ageDialogListener) {
        this.ageDialogListener = ageDialogListener;
    }

    public GenderDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.gender_dialog;
    }

    @Override
    protected void initData() {
        tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        tvOk = (TextViewHozo) findViewById(R.id.tv_ok);
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
        rgGender = (RadioGroup) findViewById(R.id.rg_gender);
    }

    public void doGender() {
        int selectedId = rgGender.getCheckedRadioButtonId();
        RadioButton radioSelected = (RadioButton) findViewById(selectedId);
        switch (selectedId) {
            case R.id.rb_any:
                if (ageDialogListener != null)
                    ageDialogListener.onAgeDialogLister(radioSelected.getText().toString());
                hideView();
                break;

            case R.id.rb_male:
                if (ageDialogListener != null)
                    ageDialogListener.onAgeDialogLister(radioSelected.getText().toString());
                hideView();
                break;

            case R.id.rb_female:
                if (ageDialogListener != null)
                    ageDialogListener.onAgeDialogLister(radioSelected.getText().toString());
                hideView();
                break;

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                hideView();
                break;
            case R.id.tv_ok:
                doGender();
                break;

        }

    }
}
