package vn.tonish.hozo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceUtil;

/**
 * Created by LongBui on 5/4/2017.
 */

public class TextViewHozo extends AppCompatTextView {
    public TextViewHozo(Context context) {
        super(context);
        init();
    }

    public TextViewHozo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewHozo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(TypefaceUtil.getFontNormal(getContext()));
    }

}
