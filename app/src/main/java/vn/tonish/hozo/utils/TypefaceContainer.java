package vn.tonish.hozo.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by TungLT on 16/9/2016.
 */

public class TypefaceContainer {

    public static Typeface TYPEFACE_LIGHT;
    public static Typeface TYPEFACE_BOLD;
    public static Typeface TYPEFACE_REGULAR;

    public static void init(Context context) {
        TYPEFACE_LIGHT = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        TYPEFACE_BOLD = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Bold.ttf");
        TYPEFACE_REGULAR = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Regular.ttf");
    }
}
