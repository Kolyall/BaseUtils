package com.github.kolyall.androidutils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class DiskUtil {

    /**
     * Creates a unique subdirectory of the designated app cache directory. Tries to use external but if not mounted, falls back on internal
     * storage.
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        return Environment.getExternalStorageState()
                .startsWith(Environment.MEDIA_MOUNTED) ? new File(context.getExternalCacheDir(), uniqueName) : new File(context
                .getCacheDir(), uniqueName);

    }

    public static String readFromAssets(Context context, String filename) {
        String json = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(context.getAssets().open(filename)));
            // do reading, usually loop until end of file reading
            StringBuilder sb = new StringBuilder();
            String mLine = reader.readLine();
            while (mLine != null) {
                sb.append(mLine); // process line
                mLine = reader.readLine();
            }
            reader.close();
            json = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
