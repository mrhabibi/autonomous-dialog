package com.mrhabibi.persistentdialog.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.TypedValue;

import com.mrhabibi.persistentdialog.PersistentDialog;

/**
 * Created by mrhabibi on 4/5/17.
 * Just Utility class for helping PersistentDialog
 */

public class DialogUtils {

    public static int dpToPx(@NonNull Context context, int dp) {
        return (int) dpToPx(context, (float) dp);
    }

    public static float dpToPx(@NonNull Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void log(String activity, @Nullable String identifier) {
        Log.d(PersistentDialog.TAG, activity + " | " + (identifier != null ? identifier : "no identifier"));
    }
}
