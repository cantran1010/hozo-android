package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by CanTran on 3/12/18.
 */

public class NoteActivity extends BaseActivity implements View.OnClickListener {
    @Override
    protected int getLayout() {
        if (getIntent().getIntExtra(Constants.PREPAY_TYPE_EXTRA, 1) == 1) {
            return R.layout.note_prepay_poster_info;
        } else if (getIntent().getIntExtra(Constants.PREPAY_TYPE_EXTRA, 2) == 2) {
            return R.layout.note_prepay_tasker_info;
        } else {
            return R.layout.note_discount_info;
        }
    }

    @Override
    protected void initView() {
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        if (getIntent().hasExtra(Constants.DISCOUNT_TYPE_EXTRA)) {
            String content;
            TextViewHozo textViewHozo = (TextViewHozo) findViewById(R.id.tv_note_discount);
            if (getIntent().getBooleanExtra(Constants.PREPAY_TYPE_DEDUCTION, false))
                content = getString(R.string.discount_info_1, String.valueOf(getIntent().getIntExtra(Constants.DISCOUNT_TYPE_EXTRA, 0)), "%");
            else
                content = getString(R.string.discount_info_1, Utils.formatNumber(getIntent().getIntExtra(Constants.DISCOUNT_TYPE_EXTRA, 0)), "đ");
            textViewHozo.setText(content);
        }
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
        }
    }
}
