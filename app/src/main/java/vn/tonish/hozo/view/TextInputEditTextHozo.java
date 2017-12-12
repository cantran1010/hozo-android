package vn.tonish.hozo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceContainer;

/**
 * Created by Cantran on 5/4/2017.
 */

public class TextInputEditTextHozo extends TextInputEditText {
    public TextInputEditTextHozo(Context context) {
        super(context);
        init();
    }

    public TextInputEditTextHozo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextInputEditTextHozo(Context context, AttributeSet attrs, int defStyleAttr) {
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

