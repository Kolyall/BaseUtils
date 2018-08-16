package by.kolyall.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by Nick Unuchek on 25.07.2017.
 */

public class ServiceUtils {
    public static boolean isServiceRunning(Context context, Class cls) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
