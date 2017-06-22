package vn.tonish.hozo.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.ImageView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.GeneralInfoActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 6/21/17.
 */

public class BlockDialog extends BaseDialog implements View.OnClickListener {

    private TextViewHozo tvContent;

    public BlockDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.block_dialog;
    }

    @Override
    protected void initData() {
        tvContent = (TextViewHozo) findViewById(R.id.tv_content);
        ImageView imgClose = (ImageView) findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
    }

    public void updateContent(String content) {
        tvContent.setText(content);
        String matcher = getContext().getString(R.string.term_and_policy);

        SpannableString spannable = new SpannableString(tvContent.getText().toString());

        Pattern patternId = Pattern.compile(matcher);
        Matcher matcherId = patternId.matcher(tvContent.getText().toString());
        while (matcherId.find()) {
            spannable.setSpan(new ClickableSpan() {
                @Override
                public void onClick(View widget) {
                    openGeneralInfoActivity(getContext().getString(R.string.other_condition), "http://hozo.vn/dieu-khoan-su-dung/?ref=app");
                }
            }, matcherId.start(), matcherId.end(), 0);
//            spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#00A2E5")), matcherId.start(), matcherId.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        tvContent.setMovementMethod(LinkMovementMethod.getInstance());
        tvContent.setText(spannable);
        tvContent.setContentDescription(spannable);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_close:
                hideView();
                break;

        }
    }

    private void openGeneralInfoActivity(String title, String url) {
        Intent intent = new Intent(getContext(), GeneralInfoActivity.class);
        intent.putExtra(Constants.URL_EXTRA, url);
        intent.putExtra(Constants.TITLE_INFO_EXTRA, title);
        getContext().startActivity(intent);
        hideView();
    }
}
