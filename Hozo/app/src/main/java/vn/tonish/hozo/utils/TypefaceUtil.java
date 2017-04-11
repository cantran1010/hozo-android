package vn.tonish.hozo.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.Field;

public class TypefaceUtil {

    private static final String FONT_NOMAL = "NanumGothic-Regular.ttf";
    private static Typeface nanumBarumTf;

    private static final String FONT_BOLD = "Noto-KR-Medium.otf";
    private static Typeface nanumBarunBold;

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

//    //set tv font nomal
//    public static void setFontTvNomal(Context context, TextView... tvs) {
//        if (nanumBarumTf == null) {
//            nanumBarumTf = Typeface.createFromAsset(context.getAssets(),
//                    "fonts/" + FONT_NOMAL);
//        }
//
//        for (TextView tv : tvs) {
//            tv.setTypeface(nanumBarumTf);
//        }
//
//    }

    //set tv font bold
    public static void setFontTvBold(Context context, TextView... tvs) {
        if (nanumBarunBold == null) {
            nanumBarunBold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        for (TextView tv : tvs) {
            tv.setTypeface(nanumBarunBold);
        }

    }

    //set edt font nomal
    public static void setFontEdtNomal(Context context, EditText... edts) {
        if (nanumBarumTf == null) {
            nanumBarumTf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NOMAL);
        }

        for (EditText edt : edts) {
            edt.setTypeface(nanumBarumTf);
        }

    }

    //set edt font bold
    public static void setFontEdtBold(Context context, EditText... edts) {
        if (nanumBarunBold == null) {
            nanumBarunBold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        for (EditText edt : edts) {
            edt.setTypeface(nanumBarunBold);
        }

    }

    //set btn font nomal
    public static void setFontBtnNomal(Context context, Button... btns) {
        if (nanumBarumTf == null) {
            nanumBarumTf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NOMAL);
        }

        for (Button btn : btns) {
            btn.setTypeface(nanumBarumTf);
        }

    }

    //set btn font bold
    public static void setFontBtnBold(Context context, Button... btns) {
        if (nanumBarunBold == null) {
            nanumBarunBold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        for (Button btn : btns) {
            btn.setTypeface(nanumBarunBold);
        }
    }

    public static Typeface getFontNomal(Context context) {
        if (nanumBarumTf == null) {
            nanumBarumTf = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_NOMAL);
        }

        return nanumBarumTf;
    }

    public static Typeface getFontBold(Context context) {
        if (nanumBarunBold == null) {
            nanumBarunBold = Typeface.createFromAsset(context.getAssets(),
                    "fonts/" + FONT_BOLD);
        }

        return nanumBarunBold;
    }

}