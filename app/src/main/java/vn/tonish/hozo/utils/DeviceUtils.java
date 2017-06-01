package vn.tonish.hozo.utils;

import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
/**
 * Created by LongBui.
 */
public class DeviceUtils {

    public static DisplayInfo getDisplayInfo(Context context) {
        DisplayInfo sDisplayInfo = new DisplayInfo();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        Display display = wm.getDefaultDisplay();

        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        sDisplayInfo.density = metrics.densityDpi;
        sDisplayInfo.width = metrics.widthPixels;
        sDisplayInfo.height = metrics.heightPixels;
        return sDisplayInfo;
    }

    public static class DisplayInfo {
        private int width;
        private int height;
        private int density;

        public DisplayInfo() {

        }

        public DisplayInfo(int width, int height, int density) {
            this.width = width;
            this.height = height;
            this.density = density;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getDensity() {
            return density;
        }

        public void setDensity(int density) {
            this.density = density;
        }

    }

    public static String getModel() {
        return Build.MODEL;
    }


}
