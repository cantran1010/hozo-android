package vn.tonish.hozo.activity.payment;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;
import vn.tonish.hozo.adapter.BankAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.model.Bank;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by LongBui on 12/25/17.
 */

public class WithdrawalActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = WithdrawalActivity.class.getSimpleName();
    private ImageView imgAddBank;
    private ArrayList<Bank> banks = new ArrayList<>();
    private RecyclerView rcvBank;
    private BankAdapter bankAdapter;

    @Override
    protected int getLayout() {
        return R.layout.withdrawal_activity;
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

        imgAddBank = (ImageView) findViewById(R.id.img_add_bank);
        imgAddBank.setOnClickListener(this);

        rcvBank = (RecyclerView) findViewById(R.id.rcv_bank);


    }

    @Override
    protected void initData() {
        setUpBank();
    }

    @Override
    protected void resumeData() {

    }

    private void setUpBank() {
        bankAdapter = new BankAdapter(banks);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvBank.setLayoutManager(linearLayoutManager);
        rcvBank.setAdapter(bankAdapter);

        bankAdapter.setBankAdapterLister(new BankAdapter.BankAdapterLister() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onDelete(int position) {
                banks.remove(position);
                bankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onEdit(int position) {
                Intent intent = new Intent(WithdrawalActivity.this, BankActivity.class);
                intent.putExtra(Constants.BANK_EDIT_EXTRA,banks.get(position));
                startActivityForResult(intent, Constants.EDIT_BANK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.BANK_REQUEST_CODE
                && resultCode == Constants.BANK_RESULT_CODE) {
            Bank bank = (Bank) data.getSerializableExtra(Constants.BANK_EXTRA);
            banks.add(bank);
            bankAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                finish();
                break;

            case R.id.img_add_bank:
                Intent intent = new Intent(WithdrawalActivity.this, BankActivity.class);
                startActivityForResult(intent, Constants.BANK_REQUEST_CODE, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }
}
