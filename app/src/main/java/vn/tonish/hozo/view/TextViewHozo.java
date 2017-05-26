package vn.tonish.hozo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceContainer;

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

        if (!isInEditMode()) {
            Typeface tf;
            try {
               int styleTxt = getTypeface().getStyle();

                switch (styleTxt) {
                    case Typeface.NORMAL:
                        tf = TypefaceContainer.TYPEFACE_REGULAR;
                        break;
                    case Typeface.BOLD:
                        tf = TypefaceContainer.TYPEFACE_MEDIUM;
                        break;
                    case Typeface.ITALIC:
                        tf = TypefaceContainer.TYPEFACE_LIGHT;
                        break;
                    default:
                        tf = TypefaceContainer.TYPEFACE_REGULAR;
                        break;
                }
            } catch (Exception e) {
                tf = TypefaceContainer.TYPEFACE_REGULAR;
            }
            setTypeface(tf);
        }
    }

}
