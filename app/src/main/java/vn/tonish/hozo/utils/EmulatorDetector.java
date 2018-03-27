package vn.tonish.hozo.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CanTran on 2/5/18.
 */

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
public final class EmulatorDetector {
    public interface OnEmulatorDetectorListener {
        void onResult(boolean isEmulator);
    }

    private static final String[] GENY_FILES = {
            "/dev/socket/genyd",
            "/dev/socket/baseband_genyd"
    };

    private static final String[] QEMU_DRIVERS = {"goldfish"};

    private static final String[] PIPES = {
            "/dev/socket/qemud",
            "/dev/qemu_pipe"
    };

    private static final String[] X86_FILES = {
            "ueventd.android_x86.rc",
            "x86.prop",
            "ueventd.ttVM_x86.rc",
            "init.ttVM_x86.rc",
            "fstab.ttVM_x86",
            "fstab.vbox86",
            "init.vbox86.rc",
            "ueventd.vbox86.rc"
    };

    private static final String[] ANDY_FILES = {
            "fstab.andy",
            "ueventd.andy.rc"
    };

    private static final String[] NOX_FILES = {
            "fstab.nox",
            "init.nox.rc",
            "ueventd.nox.rc"
    };

    private static final Property[] PROPERTIES = {
            new Property("init.svc.qemud", null),
            new Property("init.svc.qemu-props", null),
            new Property("qemu.hw.mainkeys", null),
            new Property("qemu.sf.fake_camera", null),
            new Property("qemu.sf.lcd_density", null),
            new Property("ro.bootloader", "unknown"),
            new Property("ro.bootmode", "unknown"),
            new Property("ro.hardware", "goldfish"),
            new Property("ro.kernel.android.qemud", null),
            new Property("ro.kernel.qemu.gles", null),
            new Property("ro.kernel.qemu", "1"),
            new Property("ro.product.device", "generic"),
            new Property("ro.product.model", "sdk"),
            new Property("ro.product.name", "sdk"),
            new Property("ro.serialno", null)
    };

    private static final String IP = "10.0.2.15";

    private static final int MIN_PROPERTIES_THRESHOLD = 0x5;

    private final Context mContext;
    private boolean isDebug = false;
    private final List<String> mListPackageName = new ArrayList<>();

    @SuppressLint("StaticFieldLeak")
    //Since we use application context now this won't leak memory anymore. This is only to please Lint
    private static EmulatorDetector mEmulatorDetector;

    public static EmulatorDetector with(Context pContext) {
        if (pContext == null) {
            throw new IllegalArgumentException("Context must not be null.");
        }
        if (mEmulatorDetector == null)
            mEmulatorDetector = new EmulatorDetector(pContext.getApplicationContext());
        return mEmulatorDetector;
    }

    private EmulatorDetector(Context pContext) {
        mContext = pContext;
        mListPackageName.add("com.google.android.launcher.layouts.genymotion");
        mListPackageName.add("com.bluestacks");
        mListPackageName.add("com.bignox.app");
    }

    public EmulatorDetector setDebug(boolean isDebug) {
        this.isDebug = isDebug;
        return this;
    }

    public boolean isDebug() {
        return isDebug;
    }


    public EmulatorDetector addPackageName(String pPackageName) {
        this.mListPackageName.add(pPackageName);
        return this;
    }

    public void detect(final OnEmulatorDetectorListener pOnEmulatorDetectorListener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isEmulator = detect();
                if (pOnEmulatorDetectorListener != null) {
                    pOnEmulatorDetectorListener.onResult(isEmulator);
                }
            }
        }).start();
    }

    private boolean detect() {
        return checkBasic() || checkAdvanced() || checkPackageName();
    }

    private boolean checkBasic() {
        @SuppressLint("HardwareIds") boolean result = Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.toLowerCase().contains("droid4x")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.HARDWARE.equals("goldfish")
                || Build.HARDWARE.equals("vbox86")
                || Build.PRODUCT.equals("sdk")
                || Build.PRODUCT.equals("google_sdk")
                || Build.PRODUCT.equals("sdk_x86")
                || Build.PRODUCT.equals("vbox86p")
                || Build.BOARD.toLowerCase().contains("nox")
                || Build.BOOTLOADER.toLowerCase().contains("nox")
                || Build.HARDWARE.toLowerCase().contains("nox")
                || Build.PRODUCT.toLowerCase().contains("nox")
                || Build.SERIAL.toLowerCase().contains("nox");
        if (result) return true;
        result |= Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic");
        if (result) return true;
        result |= "google_sdk".equals(Build.PRODUCT);
        return result;
    }

    private boolean checkAdvanced() {
        return checkFiles(GENY_FILES, "Geny")
                || checkFiles(ANDY_FILES, "Andy")
                || checkFiles(NOX_FILES, "Nox")
                || checkQEmuDrivers()
                || checkFiles(PIPES, "Pipes")
                || checkIp()
                || (checkQEmuProps() && checkFiles(X86_FILES, "X86"));
    }

    private boolean checkPackageName() {
        boolean isCheckPackage = true;
        if (!isCheckPackage || mListPackageName.isEmpty()) {
            return false;
        }
        final PackageManager packageManager = mContext.getPackageManager();
        for (final String pkgName : mListPackageName) {
            final Intent tryIntent = packageManager.getLaunchIntentForPackage(pkgName);
            if (tryIntent != null) {
                final List<ResolveInfo> resolveInfos = packageManager.queryIntentActivities(tryIntent, PackageManager.MATCH_DEFAULT_ONLY);
                if (!resolveInfos.isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean checkQEmuDrivers() {
        for (File drivers_file : new File[]{new File("/proc/tty/drivers"), new File("/proc/cpuinfo")}) {
            if (drivers_file.exists() && drivers_file.canRead()) {
                byte[] data = new byte[1024];
                try {
                    InputStream is = new FileInputStream(drivers_file);
                    is.read(data);
                    is.close();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                String driver_data = new String(data);
                for (String known_qemu_driver : QEMU_DRIVERS) {
                    if (driver_data.contains(known_qemu_driver)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private boolean checkFiles(String[] targets, String type) {
        for (String pipe : targets) {
            File qemu_file = new File(pipe);
            if (qemu_file.exists()) {
                return true;
            }
        }
        return false;
    }

    private boolean checkQEmuProps() {
        int found_props = 0;

        for (Property property : PROPERTIES) {
            String property_value = getProp(mContext, property.name);
            if ((property.seek_value == null) && (property_value != null)) {
                found_props++;
            }
            if ((property.seek_value != null)
                    && (property_value.contains(property.seek_value))) {
                found_props++;
            }

        }

        return found_props >= MIN_PROPERTIES_THRESHOLD;
    }

    private boolean checkIp() {
        boolean ipDetected = false;
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
            String[] args = {"/system/bin/netcfg"};
            StringBuilder stringBuilder = new StringBuilder();
            try {
                ProcessBuilder builder = new ProcessBuilder(args);
                builder.directory(new File("/system/bin/"));
                builder.redirectErrorStream(true);
                Process process = builder.start();
                InputStream in = process.getInputStream();
                byte[] re = new byte[1024];
                while (in.read(re) != -1) {
                    stringBuilder.append(new String(re));
                }
                in.close();

            } catch (Exception ex) {
                // empty catch
            }

            String netData = stringBuilder.toString();

            if (!TextUtils.isEmpty(netData)) {
                String[] array = netData.split("\n");

                for (String lan :
                        array) {
                    if ((lan.contains("wlan0") || lan.contains("tunl0") || lan.contains("eth0"))
                            && lan.contains(IP)) {
                        ipDetected = true;
                        break;
                    }
                }

            }
        }
        return ipDetected;
    }

    private String getProp(Context context, String property) {
        try {
            ClassLoader classLoader = context.getClassLoader();
            @SuppressLint("PrivateApi") Class<?> systemProperties = classLoader.loadClass("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);
            Object[] params = new Object[1];
            params[0] = property;

            return (String) get.invoke(systemProperties, params);
        } catch (Exception exception) {
            // empty catch
        }
        return null;
    }

}
