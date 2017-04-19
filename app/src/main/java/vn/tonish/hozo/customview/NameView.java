package vn.tonish.hozo.customview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import vn.tonish.hozo.R;

/**
 * Created by CanTran on 18/04/2017.
 */

public class NameView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private View rootView;
    private String phone;
    private EditText edtName;
    private TextView tvSave;
    private LinearLayout btnBack;

    public NameView(Context context, String phone) {
        super(context);
        this.phone = phone;
        this.context = context;
        initView();
        initData();
    }

    private void initData() {
    }


    private void initView() {
        rootView = LayoutInflater.from(context).inflate(R.layout.view_name, null);
        addView(rootView);
        edtName = (EditText) rootView.findViewById(R.id.edt_name);
        tvSave = (TextView) rootView.findViewById(R.id.tv_save);
        btnBack = (LinearLayout) rootView.findViewById(R.id.btn_back);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:

                break;
        }

    }
}
