package com.mrhabibi.persistentdialog.wrapper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.mrhabibi.persistentdialog.DialogBuilder;
import com.mrhabibi.persistentdialog.DialogCallback;

/**
 * Created by mrhabibi on 12/2/16.
 * The very basic class to wrap the dialog and let the activity extract the dialog datas via
 * interface DialogBuilder
 */
public abstract class DialogWrapper extends Fragment implements DialogInterface, DialogCallback {

    public static final String RESULT_CODE_STATE = "resultCode";

    protected int mResultCode;
    /**
     * This saves the built dialog, the final version, and combined with interface DialogInterface
     * to access the dialog function. Used for custom view that needs to interact with built dialog
     * instead of dialog builder
     */
    private DialogInterface mDialogInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mResultCode = savedInstanceState.getInt(RESULT_CODE_STATE);
        }
    }

    /**
     * Used by activity to set the built dialog
     *
     * @param dialog The built dialog
     */
    public void onDialogShown(AlertDialog dialog) {
        this.mDialogInterface = dialog;
    }

    /**
     * Used to dismiss built dialog
     */
    @Override
    public final void dismiss() {
        mDialogInterface.dismiss();
    }

    /**
     * Used to cancel built dialog
     */
    @Override
    public final void cancel() {
        mDialogInterface.cancel();
    }

    /**
     * Called when activity is building dialog, collecting any dialog information and show it to
     * the user
     *
     * @param dialogBuilder The dialog builder
     */
    public abstract void onBuildDialog(DialogBuilder dialogBuilder);

    @Override
    public void onBundleResponses(Bundle responses) {
    }

    @Override
    public int getResultCode() {
        return mResultCode;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RESULT_CODE_STATE, mResultCode);
    }

    public abstract static class Builder<T extends DialogWrapper> {
        protected Bundle arguments;

        public Builder() {
            this.arguments = new Bundle();
        }

        public abstract T build();

        public Bundle getArguments() {
            return arguments;
        }
    }
}
