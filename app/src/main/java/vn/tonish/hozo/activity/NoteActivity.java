package vn.tonish.hozo.activity;

import android.view.View;
import android.widget.ImageView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
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
        TextViewHozo textViewHozo = (TextViewHozo) findViewById(R.id.tv_discount);
        String content = getString(R.string.discount_info_1, getIntent().getIntExtra(Constants.DISCOUNT_TYPE_EXTRA, 0), "%");
        textViewHozo.setText(content);

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
