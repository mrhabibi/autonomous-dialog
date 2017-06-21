package com.mrhabibi.autonomousdialog;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.mrhabibi.autonomousdialog.utils.DialogUtils;
import com.mrhabibi.autonomousdialog.utils.FragmentPasser;
import com.mrhabibi.autonomousdialog.wrapper.DialogWrapper;

import java.util.HashMap;
import java.util.HashSet;

import static com.mrhabibi.autonomousdialog.DialogActivity.CANCELABLE_LABEL;
import static com.mrhabibi.autonomousdialog.DialogActivity.FRAGMENT_GETTER_ID_LABEL;
import static com.mrhabibi.autonomousdialog.DialogActivity.IDENTIFIER_LABEL;
import static com.mrhabibi.autonomousdialog.DialogActivity.PARAMS_LABEL;
import static com.mrhabibi.autonomousdialog.DialogActivity.THEME_RES_LABEL;
import static com.mrhabibi.autonomousdialog.DialogResult.REQUEST_DIALOG;

/**
 * Created by mrhabibi on 12/2/16.
 * This is the official AutonomousDialog builder class, experimental stuff because
 * Android AlertDialog can't keep the data persistency :(
 * and Android DialogFragment is not generic enough, and actually DialogFragment is a fragment,
 * that hosted by Activity to live, and also famous with IllegalStateException :(
 * This is the first class you need to build such simple / complex / very complex dialog
 * with layout and data persistency ability! cute stuff, just give a try!
 * If you find any bug, please let me know :)
 */

public class AutonomousDialog {

    public final static String TAG = "AutonomousDialog";

    /**
     * Used to keep dialog ids that will be dismissed remotely, for handling race condition while showing dialog
     */
    final static HashSet<String> dismissedDialogIds = new HashSet<>();

    /**
     * Used to keep shown dialog ids and ready status, for handling showing one dialog per id
     */
    final static HashMap<String, Boolean> shownDialogIds = new HashMap<>();

    /**
     * Builder instantiator for such simple dialog, with no callback and non singleton style
     *
     * @param context The God object
     * @return The builder
     */
    public static Builder builder(@NonNull Context context) {
        return new Builder(context);
    }

    /**
     * Builder instantiator for dialog with callback(s) and singleton style
     *
     * @param context    The God object
     * @param identifier The whatever identifier
     * @return The builder
     */
    public static Builder builder(@NonNull Context context, String identifier) {
        return new Builder(context, identifier);
    }

    /**
     * Used to dismiss dialog asyncrhonously from wherever you need
     *
     * @param context    The God object
     * @param identifier The same whatever identifier
     */
    public static void dismiss(@NonNull Context context, @NonNull String identifier) {
        if (shownDialogIds.containsKey(identifier) && !shownDialogIds.get(identifier)) {
            dismissedDialogIds.add(identifier);
        } else {
            Intent intent = new Intent(DialogActivity.DISMISS_ACTION);
            intent.putExtra(DialogActivity.IDENTIFIER_KEY, identifier);
            context.sendBroadcast(intent);
        }
    }

    /**
     * Used to reset shown and dismissed dialog for specific identifier
     *
     * @param context    The God object
     * @param identifier The same whatever identifier
     */
    public static void reset(@NonNull Context context, @NonNull String identifier) {
        dismiss(context, identifier);
        dismissedDialogIds.remove(identifier);
        shownDialogIds.remove(identifier);
    }

    public static class Builder {
        protected Context mContext;
        protected Fragment mFragment;
        protected boolean mCancelable;
        protected String mIdentifier;
        @StyleRes
        protected int mThemeRes;
        protected Bundle mParams;
        protected Intent mIntent;

        public Builder(@NonNull Context context) {
            this.mContext = context;
            this.mCancelable = true;
        }

        public Builder(@NonNull Context context, String identifier) {
            this.mContext = context;
            this.mIdentifier = identifier;
            this.mCancelable = true;
        }

        /**
         * Setter for the dialog content that using single fragment
         *
         * @param fragment The dialog fragment
         * @return The builder
         */
        public Builder setContent(@NonNull Fragment fragment) {
            this.mIntent = null;
            this.mFragment = fragment;
            return this;
        }

        /**
         * Setter for the dialog content that using custom activity w/o fragment inside
         *
         * @param activityClass The activity class extended from DialogActivity
         * @param fragment      The dialog fragment
         * @return The builder
         */
        public Builder setContent(@NonNull Class<? extends DialogActivity> activityClass, @Nullable Fragment fragment) {
            return setContent(new Intent(mContext, activityClass), fragment);
        }

        /**
         * Setter for the dialog content that using custom intent w/o fragment inside
         *
         * @param intent   The intent that has activity class extended from DialogActivity
         * @param fragment The dialog fragment
         * @return The builder
         */
        public Builder setContent(@NonNull Intent intent, @Nullable Fragment fragment) {
            try {
                if (!(DialogActivity.class.isAssignableFrom(Class.forName(intent.getComponent().getClassName())))) {
                    throw new IllegalStateException("Intent class for AutonomousDialog must inherit DialogActivity class!");
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            this.mIntent = intent;
            this.mFragment = fragment;
            return this;
        }

        /**
         * Setter for the dialog content that using alert dialog
         *
         * @param dialogWrapper The dialog wrapper
         * @return The builder
         */
        public Builder setContent(@NonNull DialogWrapper dialogWrapper) {
            this.mIntent = null;
            this.mFragment = dialogWrapper;
            return this;
        }

        /**
         * Setter for cancelable ability for the dialog
         *
         * @param cancelable Can be canceled
         * @return The builder
         */
        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        /**
         * Setter for custom theme for the activity dialog
         *
         * @param themeRes The theme resource id
         * @return The builder
         */
        public Builder setTheme(@StyleRes int themeRes) {
            this.mThemeRes = themeRes;
            return this;
        }

        /**
         * Setter for params for the dialog that will be returned to the callback
         *
         * @param params The passed params
         * @return The builder
         */
        public Builder setParams(Bundle params) {
            this.mParams = params;
            return this;
        }

        /**
         * Last method to show the dialog
         */
        public void show() {
            Context context = this.mContext;
            this.mContext = null;

            Fragment fragment = this.mFragment;
            this.mFragment = null;

            if (context == null) {
                throw new IllegalStateException("Context must not be null!");
            }

            /*
             * Make connection between fragment and activity
             */
            String fragmentGetterId = null;
            if (fragment != null) {
                fragmentGetterId = FragmentPasser.setFragment(fragment);
            }

            /*
             * Trying to extract Activity from ContextWrapper
             */
            if (!(context instanceof Activity) && context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            }

            /*
             * Give flags FLAG_ACTIVITY_NEW_TASK if the context is not an Activity
             * like Receiver, Service, ContextWrapper
             *
             * !!! IMPORTANT !!!
             * Note that if the context is not an Activity, it means that request code WILL NOT
             * be used, and ascendant activity WILL NOT trigger onDialogResult at all
             * (the dialog will be separated task too and has no connection with previous task)
             */
            int flags = 0;
            if (!(context instanceof Activity)) {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK;
            }

            /*
             * Set default intent if there's no intent set
             */
            if (mIntent == null) {
                mIntent = new Intent(context, DialogActivity.class);
            }

            /*
             * Check if dialog with this identifier has shown
             */
            boolean hasShown;
            if (mIdentifier != null && mIdentifier.length() > 0) {
                if (!shownDialogIds.containsKey(mIdentifier)) {

                    /*
                     * Register unready shown dialog identifier
                     */
                    shownDialogIds.put(mIdentifier, false);
                    hasShown = false;
                } else {
                    DialogUtils.log("Cancelling Initialization due to Duplication", mIdentifier);
                    hasShown = true;
                }
            } else {
                hasShown = false;
            }

            if (!hasShown) {
                DialogUtils.log("Initializing", mIdentifier);

                mIntent.putExtra(FRAGMENT_GETTER_ID_LABEL, fragmentGetterId);
                mIntent.putExtra(CANCELABLE_LABEL, mCancelable);
                mIntent.putExtra(IDENTIFIER_LABEL, mIdentifier);
                mIntent.putExtra(THEME_RES_LABEL, mThemeRes);
                mIntent.putExtra(PARAMS_LABEL, mParams);
                mIntent.addFlags(flags);

                if (context instanceof Activity) {
                    ActivityCompat.startActivityForResult(((Activity) context), mIntent, REQUEST_DIALOG, null);
                } else {
                    ContextCompat.startActivity(context, mIntent, null);
                }
            }
        }

    }

}
