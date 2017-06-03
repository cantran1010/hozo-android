package vn.tonish.hozo.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.lang.reflect.Field;

import vn.tonish.hozo.view.ButtonHozo;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui.
 */
class TypefaceUtil {

    private static final String FONT_NORMAL = "Roboto-Medium.ttf";
    private static Typeface robotoMediumTtf;

    private static final String FONT_BOLD = "Roboto-Bold.ttf";
    private static Typeface robotoBoldTtf;

    /**
     * Using reflection to override default typeface
     * NOTICE: DO NOT FORGET TO SET TYPEFACE FOR APP THEME AS DEFAULT TYPEFACE WHICH WILL BE OVERRIDDEN
     *
     * @param context                    to work with assets
     * @param defaultFontNameToOverride  for example "monospace"
     * @param customFontFileNameInAssets file name of the font from assets
     */
    public static void overrideFont(Context context, String defaultFontNameToOverride, String customFontFileNameInAssets) {
        try {
            final Typeface customFontTypeface = Typeface.createFromAsset(context.getAssets(), customFontFileNameInAssets);

            final Field defaultFontTypefaceField = Typeface.class.getDeclaredField(defaultFontNameToOverride);
            defaultFontTypefaceField.setAccessible(true);
            defaultFontTypefaceField.set(null, customFontTypeface);
        } catch (Exception e) {
            LogUtils.e("TAG", "Can not set custom font " + customFontFileNameInAssets + " instead of " + defaultFontNameToOverride);
        }
    }

    //set tv font nomal
    public static void setFontTvNomal(Context context, TextViewHozo... tvs) {
        if (robotoMediumTtf == null) {
            robotoMediumTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NORMAL);
        }

        for (TextViewHozo tv : tvs) {
            tv.setTypeface(robotoMediumTtf);
        }

    }

    //set tv font bold
    public static void setFontTvBold(Context context, TextViewHozo... tvs) {
        if (robotoBoldTtf == null) {
            robotoBoldTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        for (TextViewHozo tv : tvs) {
            tv.setTypeface(robotoBoldTtf);
        }

    }

    //set edt font nomal
    public static void setFontEdtNomal(Context context, EdittextHozo... edts) {
        if (robotoMediumTtf == null) {
            robotoMediumTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NORMAL);
        }

        for (EdittextHozo edt : edts) {
            edt.setTypeface(robotoMediumTtf);
        }

    }

    //set edt font bold
    public static void setFontEdtBold(Context context, EdittextHozo... edts) {
        if (robotoBoldTtf == null) {
            robotoBoldTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        for (EdittextHozo edt : edts) {
            edt.setTypeface(robotoBoldTtf);
        }

    }

    //set btn font nomal
    public static void setFontBtnNomal(Context context, ButtonHozo... btns) {
        if (robotoMediumTtf == null) {
            robotoMediumTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NORMAL);
        }

        for (ButtonHozo btn : btns) {
            btn.setTypeface(robotoMediumTtf);
        }

    }

    //set btn font bold
    public static void setFontBtnBold(Context context, ButtonHozo... btns) {
        if (robotoBoldTtf == null) {
            robotoBoldTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        for (ButtonHozo btn : btns) {
            btn.setTypeface(robotoBoldTtf);
        }
    }

    public static Typeface getFontNormal(Context context) {
        if (robotoMediumTtf == null) {
            robotoMediumTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NORMAL);
        }

        return robotoMediumTtf;
    }

    public static Typeface getFontBold(Context context) {
        if (robotoBoldTtf == null) {
            robotoBoldTtf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        return robotoBoldTtf;
    }

}