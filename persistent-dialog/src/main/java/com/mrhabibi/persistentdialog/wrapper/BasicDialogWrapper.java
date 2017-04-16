package com.mrhabibi.persistentdialog.wrapper;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.mrhabibi.persistentdialog.DialogBuilder;

/**
 * Created by mrhabibi on 12/12/16.
 * Extension of DialogWrapper, this is the dialog wrapper with basic functionality of ordinary
 * Android AlertDialog
 */
public class BasicDialogWrapper extends DialogWrapper {

    /**
     * Constants for bundling the choice responses
     */

    public static final String SINGLE_CHOICE_SELECTED_OPTION = "singleChoiceSelectedOption";
    public static final String MULTI_CHOICE_SELECTED_OPTIONS = "multiChoiceSelectedOptions";

    public static final String TITLE_LABEL = "title";
    public static final String MESSAGE_LABEL = "message";
    public static final String POSITIVE_TEXT_LABEL = "positiveText";
    public static final String NEGATIVE_TEXT_LABEL = "negativeText";
    public static final String NEUTRAL_TEXT_LABEL = "neutralText";
    public static final String SINGLE_CHOICE_OPTIONS_LABEL = "singleChoiceOptions";
    public static final String SINGLE_CHOICE_SELECTED_OPTION_LABEL = "singleChoiceSelectedOption";
    public static final String MULTI_CHOICE_OPTIONS_LABEL = "multiChoiceOptions";
    public static final String MULTI_CHOICE_SELECTED_OPTIONS_LABEL = "multiChoiceSelectedOptions";
    public static final String PLAIN_CHOICE_OPTIONS_LABEL = "plainChoiceOptions";

    /**
     * The Texts
     */

    protected CharSequence mTitle;
    protected CharSequence mMessage;

    /**
     * The Buttons
     */

    protected CharSequence mPositiveText;
    protected CharSequence mNegativeText;
    protected CharSequence mNeutralText;

    /**
     * The Choices
     */

    protected CharSequence[] mSingleChoiceOptions;
    protected int mSingleChoiceSelectedOption;

    protected CharSequence[] mMultiChoiceOptions;
    protected boolean[] mMultiChoiceSelectedOptions;

    protected CharSequence[] mPlainChoiceOptions;

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putCharSequence(TITLE_LABEL, mTitle);
        outState.putCharSequence(MESSAGE_LABEL, mMessage);
        outState.putCharSequence(POSITIVE_TEXT_LABEL, mPositiveText);
        outState.putCharSequence(NEGATIVE_TEXT_LABEL, mNegativeText);
        outState.putCharSequence(NEUTRAL_TEXT_LABEL, mNeutralText);
        outState.putCharSequenceArray(SINGLE_CHOICE_OPTIONS_LABEL, mSingleChoiceOptions);
        outState.putInt(SINGLE_CHOICE_SELECTED_OPTION_LABEL, mSingleChoiceSelectedOption);
        outState.putCharSequenceArray(MULTI_CHOICE_OPTIONS_LABEL, mMultiChoiceOptions);
        outState.putBooleanArray(MULTI_CHOICE_SELECTED_OPTIONS_LABEL, mMultiChoiceSelectedOptions);
        outState.putCharSequenceArray(PLAIN_CHOICE_OPTIONS_LABEL, mPlainChoiceOptions);
    }

    private void extractBundleStates(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(TITLE_LABEL)) {
                this.mTitle = bundle.getCharSequence(TITLE_LABEL);
            }
            if (bundle.containsKey(MESSAGE_LABEL)) {
                this.mMessage = bundle.getCharSequence(MESSAGE_LABEL);
            }
            if (bundle.containsKey(POSITIVE_TEXT_LABEL)) {
                this.mPositiveText = bundle.getCharSequence(POSITIVE_TEXT_LABEL);
            }
            if (bundle.containsKey(NEGATIVE_TEXT_LABEL)) {
                this.mNegativeText = bundle.getCharSequence(NEGATIVE_TEXT_LABEL);
            }
            if (bundle.containsKey(NEUTRAL_TEXT_LABEL)) {
                this.mNeutralText = bundle.getCharSequence(NEUTRAL_TEXT_LABEL);
            }
            if (bundle.containsKey(SINGLE_CHOICE_OPTIONS_LABEL)) {
                this.mSingleChoiceOptions = bundle.getCharSequenceArray(SINGLE_CHOICE_OPTIONS_LABEL);
            }
            if (bundle.containsKey(SINGLE_CHOICE_SELECTED_OPTION_LABEL)) {
                this.mSingleChoiceSelectedOption = bundle.getInt(SINGLE_CHOICE_SELECTED_OPTION_LABEL);
            }
            if (bundle.containsKey(MULTI_CHOICE_OPTIONS_LABEL)) {
                this.mMultiChoiceOptions = bundle.getCharSequenceArray(MULTI_CHOICE_OPTIONS_LABEL);
            }
            if (bundle.containsKey(MULTI_CHOICE_SELECTED_OPTIONS_LABEL)) {
                this.mMultiChoiceSelectedOptions = bundle.getBooleanArray(MULTI_CHOICE_SELECTED_OPTIONS_LABEL);
            }
            if (bundle.containsKey(PLAIN_CHOICE_OPTIONS_LABEL)) {
                this.mPlainChoiceOptions = bundle.getCharSequenceArray(PLAIN_CHOICE_OPTIONS_LABEL);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        extractBundleStates(getArguments());
        extractBundleStates(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onBuildDialog(DialogBuilder dialogBuilder) {
        if (mTitle != null && mTitle.length() > 0) {
            dialogBuilder.setTitle(mTitle);
        }
        if (mMessage != null && mMessage.length() > 0) {
            dialogBuilder.setMessage(mMessage);
        }

        if (mPositiveText != null && mPositiveText.length() > 0) {
            dialogBuilder.setPositiveButton(mPositiveText);
        }
        if (mNegativeText != null && mNegativeText.length() > 0) {
            dialogBuilder.setNegativeButton(mNegativeText);
        }
        if (mNeutralText != null && mNeutralText.length() > 0) {
            dialogBuilder.setNeutralButton(mNeutralText);
        }

        if (mSingleChoiceOptions != null && mSingleChoiceOptions.length > 0) {

            dialogBuilder.setSingleChoiceItems(mSingleChoiceOptions, mSingleChoiceSelectedOption);
            dialogBuilder.setSingleChoiceOverridingListener(new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which) {
                    /*
                     * Saving the choice
                     */
                    mSingleChoiceSelectedOption = which;

                    /*
                     * if has positive button in single choice, dismiss dialog when item selected
                     */
                    if (!positiveButtonExist()) {
                        dialogInterface.dismiss();
                    }
                }
            });
        }
        if (mMultiChoiceOptions != null && mMultiChoiceOptions.length > 0) {
            dialogBuilder.setMultiChoiceItems(mMultiChoiceOptions, mMultiChoiceSelectedOptions);
            dialogBuilder.setMultiChoiceOverridingListener(new OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int which, boolean checked) {
                    /*
                     * Saving the choices
                     */
                    mMultiChoiceSelectedOptions[which] = checked;
                }
            });
        }
        if (mPlainChoiceOptions != null && mPlainChoiceOptions.length > 0) {
            dialogBuilder.setItems(mPlainChoiceOptions);
        }
    }

    @Override
    public void onBundleResponses(Bundle responses) {
        super.onBundleResponses(responses);
        /*
         * Bundling the choices response
         */
        if (mSingleChoiceOptions != null && mSingleChoiceOptions.length > 0) {
            responses.putInt(SINGLE_CHOICE_SELECTED_OPTION, mSingleChoiceSelectedOption);
        }
        if (mMultiChoiceOptions != null && mMultiChoiceOptions.length > 0) {
            responses.putBooleanArray(MULTI_CHOICE_SELECTED_OPTIONS, mMultiChoiceSelectedOptions);
        }
    }

    /**
     * Check if is dialog having positive button
     *
     * @return Has positive button
     */
    private boolean positiveButtonExist() {
        return mPositiveText != null && mPositiveText.length() > 0;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static class Builder extends DialogWrapper.Builder<BasicDialogWrapper> {

        public Builder title(CharSequence title) {
            arguments.putCharSequence(TITLE_LABEL, title);
            return this;
        }

        public Builder message(CharSequence message) {
            arguments.putCharSequence(MESSAGE_LABEL, message);
            return this;
        }

        public Builder positiveText(CharSequence positiveText) {
            arguments.putCharSequence(POSITIVE_TEXT_LABEL, positiveText);
            return this;
        }

        public Builder negativeText(CharSequence negativeText) {
            arguments.putCharSequence(NEGATIVE_TEXT_LABEL, negativeText);
            return this;
        }

        public Builder neutralText(CharSequence neutralText) {
            arguments.putCharSequence(NEUTRAL_TEXT_LABEL, neutralText);
            return this;
        }

        public Builder singleChoiceOptions(CharSequence[] singleChoiceOptions) {
            arguments.putCharSequenceArray(SINGLE_CHOICE_OPTIONS_LABEL, singleChoiceOptions);
            return this;
        }

        public Builder singleChoiceSelectedOption(int singleChoiceSelectedOption) {
            arguments.putInt(SINGLE_CHOICE_SELECTED_OPTION_LABEL, singleChoiceSelectedOption);
            return this;
        }

        public Builder multiChoiceOptions(CharSequence[] multiChoiceOptions) {
            arguments.putCharSequenceArray(MULTI_CHOICE_OPTIONS_LABEL, multiChoiceOptions);
            return this;
        }

        public Builder multiChoiceSelectedOptions(boolean[] multiChoiceSelectedOptions) {
            arguments.putBooleanArray(MULTI_CHOICE_SELECTED_OPTIONS_LABEL, multiChoiceSelectedOptions);
            return this;
        }

        public Builder plainChoiceOptions(CharSequence[] plainChoiceOptions) {
            arguments.putCharSequenceArray(PLAIN_CHOICE_OPTIONS_LABEL, plainChoiceOptions);
            return this;
        }

        @Override
        public BasicDialogWrapper build() {
            BasicDialogWrapper dialogWrapper = new BasicDialogWrapper();
            dialogWrapper.setArguments(arguments);
            return dialogWrapper;
        }
    }
}
