package vn.tonish.hozo.customview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.LoginScreen;
import vn.tonish.hozo.activity.MainActivity;

/**
 * Created by CanTran on 18/04/2017.
 */

public class NameView extends FrameLayout implements View.OnClickListener {
    private Context context;
    private View rootView;
    private EditText edtName;
    private TextView btnSave;
    private FrameLayout btnBack;

    public NameView(Context context) {
        super(context);
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
        btnSave = (TextView) rootView.findViewById(R.id.btn_save);
        btnBack = (FrameLayout) rootView.findViewById(R.id.btn_back);
        btnSave.setOnClickListener(this);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                ((LoginScreen) context).closeExtendView();
                break;

            case R.id.btn_save:
                Toast.makeText(context, "wellcome "+edtName.getText().toString()+" to hozo", Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, MainActivity.class));
                break;
        }

    }
}
