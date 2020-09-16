package com.mrhabibi.autonomousdialog.wrapper;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.mrhabibi.autonomousdialog.DialogBuilder;
import com.mrhabibi.autonomousdialog.DialogCallback;

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

    public void setResultCode(int resultCode) {
        mResultCode = resultCode;
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
