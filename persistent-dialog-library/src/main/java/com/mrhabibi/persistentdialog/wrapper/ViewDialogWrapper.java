package com.mrhabibi.persistentdialog.wrapper;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.mrhabibi.persistentdialog.DialogBuilder;
import com.mrhabibi.persistentdialog.DialogCallback;
import com.mrhabibi.persistentdialog.utils.DialogUtils;

/**
 * Created by mrhabibi on 12/13/16.
 * Extension of BasicDialogWrapper, same like BasicDialogWrapper but with extra view, if you want
 * to implement custom view in the dialog, extend this class, and this class will handle the view
 * saving state lifecycle too!
 */
public abstract class ViewDialogWrapper extends BasicDialogWrapper {

    public static final String SAVED_VIEW_LABEL = "savedViewState";
    Bundle mSavedViewState;
    View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            /*
             * Bring back the saved view state
             */
            mSavedViewState = savedInstanceState.getBundle(SAVED_VIEW_LABEL);
        }
    }

    @Override
    public void onBuildDialog(DialogBuilder dialogBuilder) {

        /*
         * Create the view first
         */
        mView = getView();

        /*
         * Check if is having saved state, and restore it immediately after view created
         */
        if (mSavedViewState != null) {
            onRestoreViewState();
            onRestoreViewState(mSavedViewState);
        }

        /*
         * Set the dialog view
         */
        dialogBuilder.setView(isUsingPadding() ? makeContainer(mView) : mView);
        super.onBuildDialog(dialogBuilder);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        /*
         * Save all the view states
         */
        Bundle savedViewState = new Bundle();
        onSaveViewState(savedViewState);
        outState.putBundle(SAVED_VIEW_LABEL, savedViewState);
    }

    @NonNull
    public abstract View getView();

    @Override
    public void onBundleResponses(Bundle responses) {
        if (mView != null && mView instanceof DialogCallback) {
            ((DialogCallback) mView).onBundleResponses(responses);
        } else {
            super.onBundleResponses(responses);
        }

        /*
         * Save all the view states first before bundling responses
         */
        Bundle savedViewState = new Bundle();
        onSaveViewState(savedViewState);

        /*
         * Call and pass the saved view state to onBundleResponses
         */
        onBundleResponses(responses, savedViewState);
    }

    /**
     * onBundleResponses with extra saved view state
     *
     * @param responses      The responses data
     * @param savedViewState The saved view state
     */
    protected void onBundleResponses(Bundle responses, Bundle savedViewState) {
    }

    /**
     * Collecting the saved view state using passed bundle
     *
     * @param outState Passed bundle
     */
    protected void onSaveViewState(Bundle outState) {
        onSaveViewState();
    }

    /**
     * Bring back the saved view state using passed bundle
     *
     * @param savedViewState Passed bundle
     */
    protected void onRestoreViewState(@Nullable Bundle savedViewState) {
        onRestoreViewState();
    }

    /**
     * Collecting the saved view state using other way
     */
    protected void onSaveViewState() {
    }

    /**
     * Bring back the saved view state using other way
     */
    protected void onRestoreViewState() {
    }

    /**
     * Override this to determine if is the view will use right and left padding,
     * the default is true
     *
     * @return Is using view padding
     */
    protected boolean isUsingPadding() {
        return true;
    }

    /**
     * Create container for the view to make right and left padding using FrameLayout Container
     *
     * @param view The dialog view
     * @return The Container
     */
    private FrameLayout makeContainer(View view) {
        FrameLayout container = new FrameLayout(getContext());
        /*
         * Give padding as much as the title's
         */
        container.setPadding(
                DialogUtils.dpToPx(getContext(), 24),
                DialogUtils.dpToPx(getContext(), 8),
                DialogUtils.dpToPx(getContext(), 24),
                DialogUtils.dpToPx(getContext(), 8));
        container.addView(view);
        return container;
    }

    @Override
    public int getResultCode() {
        if (mView instanceof DialogCallback) {
            return ((DialogCallback) mView).getResultCode();
        }
        return super.getResultCode();
    }

    public abstract static class Builder extends BasicDialogWrapper.Builder {
    }
}
