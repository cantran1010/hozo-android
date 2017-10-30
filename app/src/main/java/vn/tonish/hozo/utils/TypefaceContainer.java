package vn.tonish.hozo.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by TungLT on 16/9/2016.
 */

public class TypefaceContainer {

    public static Typeface TYPEFACE_LIGHT;
    public static Typeface TYPEFACE_MEDIUM;
    public static Typeface TYPEFACE_REGULAR;

    public static void init(Context context) {
        TYPEFACE_LIGHT = Typeface.createFromAsset(context.getAssets(),
                "fonts/Montserrat-Light.otf");
        TYPEFACE_MEDIUM = Typeface.createFromAsset(context.getAssets(),
                "fonts/Montserrat-Medium.otf");
        TYPEFACE_REGULAR = Typeface.createFromAsset(context.getAssets(),
                "fonts/Montserrat-Regular.otf");
    }
}
