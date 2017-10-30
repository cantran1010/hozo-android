package vn.tonish.hozo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceContainer;

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

        if (!isInEditMode()) {
            Typeface tf = null;
            try {
                int styleTxt = getTypeface().getStyle();

                switch (styleTxt) {
                    case Typeface.NORMAL:
                        tf = TypefaceContainer.TYPEFACE_LIGHT;
                        break;
                    case Typeface.BOLD:
                        tf = TypefaceContainer.TYPEFACE_REGULAR;
                        break;
                    case Typeface.ITALIC:
                        tf = TypefaceContainer.TYPEFACE_LIGHT;
                        break;
                    case Typeface.BOLD_ITALIC:
                        tf = TypefaceContainer.TYPEFACE_LIGHT;
                        break;
                    default:
                        tf = TypefaceContainer.TYPEFACE_LIGHT;
                        break;
                }
            } catch (Exception e) {
                tf = TypefaceContainer.TYPEFACE_LIGHT;
            }
            setTypeface(tf);
        }
    }
}

