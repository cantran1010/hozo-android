package vn.tonish.hozo.activity;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.rest.responseRes.BlockResponse;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 6/15/17.
 */

public class BlockActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = BlockActivity.class.getSimpleName();
    private BlockResponse blockResponse;
    private TextViewHozo tvReasons, tvHotline, tvEmail, tvLink;

    @Override
    protected int getLayout() {
        return R.layout.block_dialog;
    }

    @Override
    protected void initView() {
        tvReasons = (TextViewHozo) findViewById(R.id.tv_reasons);

        tvHotline = (TextViewHozo) findViewById(R.id.tv_hotline);
        tvEmail = (TextViewHozo) findViewById(R.id.tv_email);
        tvLink = (TextViewHozo) findViewById(R.id.tv_link);

        tvHotline.setOnClickListener(this);
        tvEmail.setOnClickListener(this);
        tvLink.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        Utils.cancelAllNotification(this);
        PreferUtils.setNewPushCount(this, 0);

        blockResponse = (BlockResponse) getIntent().getSerializableExtra(Constants.BLOCK_EXTRA);

        tvReasons.setText(blockResponse.getReason());

        SpannableString content = new SpannableString(blockResponse.getHotlinePhone());
        content.setSpan(new UnderlineSpan(), 0, blockResponse.getHotlinePhone().length(), 0);
        tvHotline.setText(content);

        content = new SpannableString(blockResponse.getEmailSupport());
        content.setSpan(new UnderlineSpan(), 0, blockResponse.getEmailSupport().length(), 0);
        tvEmail.setText(content);

        content = new SpannableString(blockResponse.getLinkSupport());
        content.setSpan(new UnderlineSpan(), 0, blockResponse.getLinkSupport().length(), 0);
        tvLink.setText(content);
    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_hotline:
                Utils.call(BlockActivity.this, blockResponse.getHotlinePhone());
                break;

            case R.id.tv_email:
                Utils.sendMail(BlockActivity.this, blockResponse.getEmailSupport());
                break;

            case R.id.tv_link:
                Utils.openBrowser(BlockActivity.this, blockResponse.getLinkSupport());
                break;

        }
    }
}
