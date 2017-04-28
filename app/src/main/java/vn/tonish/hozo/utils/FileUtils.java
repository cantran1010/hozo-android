package vn.tonish.hozo.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by LongBui on 4/24/2017.
 */

public class FileUtils {

    public final static String OUTPUT_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "hozo";

    public static FileUtils fileUtils;

    public static FileUtils getInstance() {
        if (fileUtils == null) {
            fileUtils = new FileUtils();
            init();
        }
        return fileUtils;
    }

    public static boolean init() {
        File folder = new File(OUTPUT_DIR);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdirs();
        }
        return success;
    }

    // maybe delete all temp file after used
    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return true;
            }

            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }

        }
        return (path.delete());
    }

    public static String getHozoDirectory() {
        return OUTPUT_DIR + File.separator;
    }


}
