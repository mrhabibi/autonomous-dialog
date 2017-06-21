package com.mrhabibi.autonomousdialog;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by mrhabibi on 12/7/16.
 * This is the dialog result processor, it has to be called when activity that receiving the result
 * This class will decide is the dialog result is clicking positive button, negative button, etc,
 * and also will parse the params, responses, and other dialog params that passed
 */

public class DialogResult {

    /**
     * Dialog activity request
     */
    public final static int REQUEST_DIALOG = 8800;

    /**
     * Dialog activity result
     */
    public final static int RESULT_DIALOG_SINGLE_CHOICE = 8801;
    public final static int RESULT_DIALOG_MULTI_CHOICES = 8802;
    public final static int RESULT_DIALOG_PLAIN_CHOICE = 8803;

    public final static int RESULT_DIALOG_POSITIVE_BUTTON = 8804;
    public final static int RESULT_DIALOG_NEGATIVE_BUTTON = 8805;
    public final static int RESULT_DIALOG_NEUTRAL_BUTTON = 8806;

    public final static int RESULT_DIALOG_CANCELLED = 0;

    private Intent mData;
    private int mResultCode;

    /**
     * DialogResult's constructor
     *
     * @param resultCode Activity result code
     * @param data       Activity result intent data
     */
    public DialogResult(int resultCode, Intent data) {
        this.mData = data;
        this.mResultCode = resultCode;
    }

    /**
     * The result is if positive button pressed
     *
     * @param identifier The identifier
     * @return Is if clicked
     */
    public boolean isPositive(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_POSITIVE_BUTTON;
    }

    /**
     * The result is if negative button pressed
     *
     * @param identifier The identifier
     * @return Is if clicked
     */
    public boolean isNegative(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_NEGATIVE_BUTTON;
    }

    /**
     * The result is if neutral button pressed
     *
     * @param identifier The identifier
     * @return Is if clicked
     */
    public boolean isNeutral(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_NEUTRAL_BUTTON;
    }

    /**
     * The result is if plain choice item pressed
     *
     * @param identifier The identifier
     * @return Is if clicked
     */
    public boolean isPlainChoice(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_PLAIN_CHOICE;
    }

    /**
     * The result is if single choice item pressed
     *
     * @param identifier The identifier
     * @return Is if clicked
     */
    public boolean isSingleChoice(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_SINGLE_CHOICE;
    }

    /**
     * The result is if multi choice item pressed
     *
     * @param identifier The identifier
     * @return Is if clicked
     */
    public boolean isMultiChoices(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_MULTI_CHOICES;
    }

    /**
     * The result is if dialog cancelled
     *
     * @param identifier The identifier
     * @return Is if cancelled
     */
    public boolean isCancelled(String identifier) {
        return isValid(identifier) && mResultCode == RESULT_DIALOG_CANCELLED;
    }

    /**
     * The result is if identifier valid
     *
     * @param identifier The identifier
     * @return Is if valid
     */
    public boolean isValid(String identifier) {
        return mData != null && mData.getStringExtra("id") != null && mData.getStringExtra("id").equals(identifier);
    }

    /**
     * The result is if identifier and custom result code are valid
     *
     * @param identifier The identifier
     * @param resultCode The custom result code
     * @return Is if valid
     */
    public boolean isValid(String identifier, int resultCode) {
        return isValid(identifier) && this.mResultCode == resultCode;
    }

    /**
     * Params getter from data intent
     *
     * @return bundle of params
     */
    public Bundle getParams() {
        if (mData == null) return null;
        return mData.getBundleExtra("params");
    }

    /**
     * Responses getter from data intent
     *
     * @return bundle of responses
     */
    public Bundle getResponses() {
        if (mData == null) return null;
        return mData.getBundleExtra("responses");
    }

    /**
     * `which` getter from data intent, `which` is index of a list
     *
     * @return `which` index
     */
    public int getWhich() {
        if (mData == null) return 0;
        return mData.getIntExtra("which", 0);
    }

    /**
     * `checked` getter from data intent, `checked` is value of an item in list
     *
     * @return `checked` value
     */
    public boolean getChecked() {
        return mData != null && mData.getBooleanExtra("checked", false);
    }

    /**
     * Get pure intent data from activity result
     *
     * @return The intent data
     */
    public Intent getData() {
        return mData;
    }
}
