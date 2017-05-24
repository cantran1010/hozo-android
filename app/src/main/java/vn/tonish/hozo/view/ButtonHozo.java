package vn.tonish.hozo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceContainer;

/**
 * Created by LongBui on 5/4/2017.
 */

public class ButtonHozo extends AppCompatButton {
    public ButtonHozo(Context context) {
        super(context);
        init();
    }

    public ButtonHozo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ButtonHozo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!isInEditMode()) {
            Typeface tf;
            try {
                int style = getTypeface().getStyle();
                switch (style) {
                    case 0:
                        tf = TypefaceContainer.TYPEFACE_REGULAR;
                        break;
                    case 1:
                        tf = TypefaceContainer.TYPEFACE_MEDIUM;
                        break;
                    case 2:
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
