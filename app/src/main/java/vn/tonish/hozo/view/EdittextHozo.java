package vn.tonish.hozo.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceUtil;

/**
 * Created by LongBui on 5/4/2017.
 */

public class EdittextHozo extends AppCompatEditText {
    public EdittextHozo(Context context) {
        super(context);
        init();
    }

    public EdittextHozo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EdittextHozo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setTypeface(TypefaceUtil.getFontNormal(getContext()));
    }
}
