package com.example.sunatlanticsrider.utils;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

public class PermissionUtils {

    public static boolean useRunTimePermissions() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1;
    }

    public static boolean hasPermission(Activity activity, String permission) {
        if (useRunTimePermissions()) {
            //Version Above LOLLIPOP_MR1 required runtime permissions
            return activity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;

        }
        //Below that no need runtime permissions
        return true;
    }

    public static void requestPermissions(Activity activity, String[] permission, int requestCode) {
        if (useRunTimePermissions()) {
            activity.requestPermissions(permission, requestCode);
        }
    }

}
