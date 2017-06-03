package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import vn.tonish.hozo.R;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 5/18/17.
 */

public class RadiusSettingDialog extends BaseDialog implements View.OnClickListener {
    private RadioGroup rgRadius;
    private RadiusDialogListener radiusDialogListener;
    private final int mIndex;

    public void setRadiusDialogListener(RadiusDialogListener radiusDialogListener) {
        this.radiusDialogListener = radiusDialogListener;
    }

    public interface RadiusDialogListener {
        void onRadiusDialogLister(String radius, int indexx);
    }

    public RadiusSettingDialog(@NonNull Context context, int mIndex) {
        super(context);
        this.mIndex = mIndex;
    }

    @Override
    protected int getLayout() {
        return R.layout.radius_dialog;
    }

    @Override
    protected void initData() {
        TextViewHozo tvCancel = (TextViewHozo) findViewById(R.id.tv_cancel);
        TextViewHozo tvOk = (TextViewHozo) findViewById(R.id.tv_ok);
        rgRadius = (RadioGroup) findViewById(R.id.rg_radius);
        setCheckedDefault();
        tvCancel.setOnClickListener(this);
        tvOk.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                hideView();
                break;
            case R.id.tv_ok:
                doRadius();
                break;

        }

    }

    private void doRadius() {
        int selectedId = rgRadius.getCheckedRadioButtonId();
        RadioButton ckSelected = (RadioButton) findViewById(selectedId);
        switch (selectedId) {
            case R.id.rb_5:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                hideView();
                break;
            case R.id.rb_10:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                hideView();
                break;
            case R.id.rb_15:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                hideView();
                break;

            case R.id.rb_20:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                hideView();
                break;

            case R.id.rb_25:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                hideView();
                break;
            case R.id.rb_50:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                hideView();
                break;
            case R.id.rb_everywhere:
                if (radiusDialogListener != null)
                    radiusDialogListener.onRadiusDialogLister(ckSelected.getText().toString(), selectedId);
                LogUtils.d("index", selectedId + "");
                hideView();
                break;
        }
    }

    private void setCheckedDefault() {
        RadioButton radioButton = (RadioButton) findViewById(mIndex);
        radioButton.setChecked(true);

    }
}
