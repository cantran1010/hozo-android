package vn.tonish.hozo.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import vn.tonish.hozo.view.ButtonHozo;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 4/24/2017.
 */

public class PickImageDialog extends BaseDialog implements View.OnClickListener {

    public interface PickImageListener {
        void onCamera();

        void onGallery();
    }

    private PickImageListener pickImageListener;

    public PickImageListener getPickImageListener() {
        return pickImageListener;
    }

    public void setPickImageListener(PickImageListener pickImageListener) {
        this.pickImageListener = pickImageListener;
    }

    private ButtonHozo btnCamera, btnGallery;

    public PickImageDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_pick_image_dialog;
    }

    @Override
    protected void initData() {
        btnCamera = (ButtonHozo) findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(this);

        btnGallery = (ButtonHozo) findViewById(R.id.btn_gallery);
        btnGallery.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_camera:
                if (pickImageListener != null) pickImageListener.onCamera();
                hideView();
                break;

            case R.id.btn_gallery:
                if (pickImageListener != null) pickImageListener.onGallery();
                hideView();
                break;

        }
    }
}
