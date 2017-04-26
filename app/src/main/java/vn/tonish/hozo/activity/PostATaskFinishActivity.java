package vn.tonish.hozo.activity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigInteger;

import vn.tonish.hozo.R;

/**
 * Created by LongBD on 4/18/2017.
 */

public class PostATaskFinishActivity extends BaseActivity implements View.OnClickListener {

    protected RelativeLayout layoutBack;
    protected Button btnDone;
    private EditText edtBudget, edtNumberWorker;
    private TextView tvTotal;

    @Override
    protected int getLayout() {
        return R.layout.post_a_task_finish_activity;
    }

    @Override
    protected void initView() {
        layoutBack = (RelativeLayout) findViewById(R.id.layout_back);
        layoutBack.setOnClickListener(this);

        edtBudget = (EditText) findViewById(R.id.edt_budget);
        edtNumberWorker = (EditText) findViewById(R.id.edt_number_worker);
        tvTotal = (TextView) findViewById(R.id.tv_total);

        btnDone = (Button) findViewById(R.id.btn_done);
        btnDone.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        edtBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPayment();
            }
        });

        edtNumberWorker.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateTotalPayment();
            }
        });
    }

    @Override
    protected void resumeData() {

    }

    private void updateTotalPayment() {
        try {
            if (edtBudget.getText().toString().equals("") || edtNumberWorker.getText().toString().equals("")) {
                tvTotal.setText("");
            } else {
                BigInteger bBudget = new BigInteger(edtBudget.getText().toString());
                BigInteger bNumberWorker = new BigInteger(edtNumberWorker.getText().toString());
                BigInteger total = bBudget.multiply(bNumberWorker);
                tvTotal.setText(total + "");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.layout_back:
                finish();
                break;

            case R.id.btn_done:

                break;

        }
    }
}
