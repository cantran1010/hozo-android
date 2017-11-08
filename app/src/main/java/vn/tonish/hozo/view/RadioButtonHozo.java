package vn.tonish.hozo.view;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.AttributeSet;

import vn.tonish.hozo.utils.TypefaceContainer;

/**
 * Created by CanTran on 5/16/17.
 */

public class RadioButtonHozo extends AppCompatCheckBox {
    public RadioButtonHozo(Context context) {
        super(context);
        init();
    }

    public RadioButtonHozo(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RadioButtonHozo(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
