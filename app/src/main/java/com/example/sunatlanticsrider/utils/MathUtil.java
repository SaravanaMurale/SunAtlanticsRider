package com.example.sunatlanticsrider.utils;

import android.content.Context;
import android.graphics.Typeface;

public class MathUtil {

    public static boolean validatePassword(String password) {
        if (password.length() <= 6) {

            // ToastUtils.getInstance(SignUpActivity.this).showLongToast(R.string.short_password);
            return false;
        }
        return true;
    }

    public static boolean passwordMatch(String password, String confirmPassword) {

        if (!password.equals(confirmPassword)) {
            return false;
        }


        return true;

    }

    public static boolean validateMobile(String mobile) {

        if (mobile.length() < 10) {
            return false;
        }
        return true;
    }

    public static Typeface getOctinPrisonFont(Context context) {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");

        return typeface;

    }

    public static Double stringToDouble(String stringVal) {

        Double d = Double.parseDouble(stringVal);

        return d;
    }

}
