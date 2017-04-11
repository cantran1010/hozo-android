package vn.tonish.hozo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class DeviceUtils {

    public static String getUDID(Context context) {
        String imei = getImei(context);
        if (imei != null) return Utils.md5(imei);

        String androidId = getAndroidId(context);
        if (androidId != null) return Utils.md5(androidId);

        String serialNumber = getSerialNumber();
        if (serialNumber != null) return Utils.md5(serialNumber);

        return "";
    }

    private static String getImei(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getAndroidId(Context context) {
        return Settings.Secure
                .getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getSerialNumber() {
        String serial_number = Build.SERIAL;
        return serial_number;
    }

    public static AppVersionInfo getVersionInfo(Context context) {

        AppVersionInfo sVersionInfo = new AppVersionInfo();
        String pkg = context.getPackageName();
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(pkg, 0);
            sVersionInfo.versionCode = pi.versionCode;
            sVersionInfo.versionName = pi.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sVersionInfo;
    }

    public static class AppVersionInfo {
        private String versionName;
        private int versionCode;

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

    }

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

    /**
     * return temperature of battery
     *
     * @param context
     * @return
     */
    public static float getBatteryTemp(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
        float temp = ((float) intent.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, 0)) / 10;
        return temp;
    }

    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }


    //Graphics Processing Unit
    public static String getGpuVendor(Context context) {
        return null;
    }

    public static String getPhoneNumber(Context context) {
        TelephonyManager tMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        return mPhoneNumber;

//        TelephonyManager telemamanger = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
//        String getSimSerialNumber = telemamanger.getSimSerialNumber();
//        return getSimSerialNumber;

    }


}
