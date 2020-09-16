package com.mrhabibi.autonomousdialog.utils;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mrhabibi.autonomousdialog.AutonomousDialog;

/**
 * Created by mrhabibi on 4/5/17.
 * Just Utility class for helping AutonomousDialog
 */

public class DialogUtils {

    public static int dpToPx(@NonNull Context context, int dp) {
        return (int) dpToPx(context, (float) dp);
    }

    public static float dpToPx(@NonNull Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static void log(String activity, @Nullable String identifier) {
        Log.d(AutonomousDialog.TAG, activity + " | " + (identifier != null ? identifier : "no identifier"));
    }
}
