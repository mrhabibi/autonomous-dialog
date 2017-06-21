package com.mrhabibi.autonomousdialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ListAdapter;

/**
 * Created by mrhabibi on 12/5/16.
 * This is actually like Android AlertDialog.Bulder, but this is limited mode of AutonomousDialog,
 * used to pass dialog data from dialog wrapper to activity
 */

public class DialogBuilder {

    private AlertDialog.Builder mAlertDialogBuilder;

    /**
     * Single choice stuff
     */
    private CharSequence[] mSingleChoiceItems;
    private int mSingleChoiceCheckedItem;
    private DialogInterface.OnClickListener mSingleChoiceOverridingListener;

    /**
     * Multi choice stuff
     */
    private CharSequence[] mMultiChoiceItems;
    private boolean[] mMultiChoiceCheckedItems;
    private DialogInterface.OnMultiChoiceClickListener mMultiChoiceOverridingListener;

    /**
     * Plain choice stuff
     */
    private CharSequence[] mPlainChoiceItems;
    private DialogInterface.OnClickListener mPlainChoiceOverridingListener;

    /**
     * Button stuff
     */
    private CharSequence mPositiveText;
    private CharSequence mNegativeText;
    private CharSequence mNeutralText;
    private OnOverridingButtonClickListener mPositiveOverridingListener;
    private OnOverridingButtonClickListener mNegativeOverridingListener;
    private OnOverridingButtonClickListener mNeutralOverridingListener;

    /**
     * Appearance stuff
     */
    private DialogInterface.OnDismissListener mDismissOverridingListener;

    public DialogBuilder(@NonNull Context context) {
        this.mAlertDialogBuilder = new AlertDialog.Builder(context);
    }

    public DialogBuilder(@NonNull Context context, @StyleRes int themeResId) {
        this.mAlertDialogBuilder = new AlertDialog.Builder(context, themeResId);
    }

    /**
     * Single choice methods
     */

    public DialogBuilder setSingleChoiceItems(CharSequence[] items, int checkedItem) {
        this.mSingleChoiceItems = items;
        this.mSingleChoiceCheckedItem = checkedItem;
        this.mAlertDialogBuilder.setSingleChoiceItems(items, checkedItem, null);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setSingleChoiceItemsExpansion(DialogInterface.OnClickListener listener) {
        this.mAlertDialogBuilder.setSingleChoiceItems(this.mSingleChoiceItems, this.mSingleChoiceCheckedItem, listener);
        return this;
    }

    CharSequence[] getSingleChoiceItems() {
        return mSingleChoiceItems;
    }

    public DialogInterface.OnClickListener getSingleChoiceOverridingListener() {
        return mSingleChoiceOverridingListener;
    }

    public DialogBuilder setSingleChoiceOverridingListener(DialogInterface.OnClickListener listener) {
        this.mSingleChoiceOverridingListener = listener;
        return this;
    }

    /**
     * Multi choice methods
     */

    public DialogBuilder setMultiChoiceItems(CharSequence[] items, boolean[] checkedItems) {
        this.mMultiChoiceItems = items;
        this.mMultiChoiceCheckedItems = checkedItems;
        this.mAlertDialogBuilder.setMultiChoiceItems(items, checkedItems, null);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setMultiChoiceItemsExpansion(DialogInterface.OnMultiChoiceClickListener listener) {
        this.mAlertDialogBuilder.setMultiChoiceItems(this.mMultiChoiceItems, this.mMultiChoiceCheckedItems, listener);
        return this;
    }

    CharSequence[] getMultiChoiceItems() {
        return mMultiChoiceItems;
    }

    public DialogInterface.OnMultiChoiceClickListener getMultiChoiceOverridingListener() {
        return mMultiChoiceOverridingListener;
    }

    public DialogBuilder setMultiChoiceOverridingListener(DialogInterface.OnMultiChoiceClickListener listener) {
        this.mMultiChoiceOverridingListener = listener;
        return this;
    }

    /**
     * Plain choice methods
     */

    public DialogBuilder setItems(CharSequence[] items) {
        this.mPlainChoiceItems = items;
        this.mAlertDialogBuilder.setItems(items, null);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setItemsExpansion(DialogInterface.OnClickListener listener) {
        this.mAlertDialogBuilder.setItems(this.mPlainChoiceItems, listener);
        return this;
    }

    CharSequence[] getPlainChoiceItems() {
        return mPlainChoiceItems;
    }

    public DialogInterface.OnClickListener getPlainChoiceOverridingListener() {
        return mPlainChoiceOverridingListener;
    }

    public DialogBuilder setPlainChoiceOverridingListener(DialogInterface.OnClickListener listener) {
        this.mPlainChoiceOverridingListener = listener;
        return this;
    }

    /**
     * Positive button methods
     */

    public DialogBuilder setPositiveButton(CharSequence text) {
        this.mPositiveText = text;
        this.mAlertDialogBuilder.setPositiveButton(text.toString().toUpperCase(), null);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setPositiveButtonExpansion(DialogInterface.OnClickListener listener) {
        this.mAlertDialogBuilder.setPositiveButton(this.mPositiveText, listener);
        return this;
    }

    CharSequence getPositiveText() {
        return mPositiveText;
    }

    public OnOverridingButtonClickListener getPositiveOverridingListener() {
        return mPositiveOverridingListener;
    }

    public DialogBuilder setPositiveOverridingListener(OnOverridingButtonClickListener listener) {
        this.mPositiveOverridingListener = listener;
        return this;
    }

    /**
     * Negative button methods
     */

    public DialogBuilder setNegativeButton(CharSequence text) {
        this.mNegativeText = text;
        this.mAlertDialogBuilder.setNegativeButton(text.toString().toUpperCase(), null);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setNegativeButtonExpansion(DialogInterface.OnClickListener listener) {
        this.mAlertDialogBuilder.setNegativeButton(this.mNegativeText, listener);
        return this;
    }

    CharSequence getNegativeText() {
        return mNegativeText;
    }

    public OnOverridingButtonClickListener getNegativeOverridingListener() {
        return mNegativeOverridingListener;
    }

    public DialogBuilder setNegativeOverridingListener(OnOverridingButtonClickListener listener) {
        this.mNegativeOverridingListener = listener;
        return this;
    }

    /**
     * Neutral button methods
     */

    public DialogBuilder setNeutralButton(CharSequence text) {
        this.mNeutralText = text;
        this.mAlertDialogBuilder.setNeutralButton(text.toString().toUpperCase(), null);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setNeutralButtonExpansion(DialogInterface.OnClickListener listener) {
        this.mAlertDialogBuilder.setNeutralButton(this.mNeutralText, listener);
        return this;
    }

    CharSequence getNeutralText() {
        return mNeutralText;
    }

    public OnOverridingButtonClickListener getNeutralOverridingListener() {
        return mNeutralOverridingListener;
    }

    public DialogBuilder setNeutralOverridingListener(OnOverridingButtonClickListener listener) {
        this.mNeutralOverridingListener = listener;
        return this;
    }

    /**
     * Appearance methods
     */

    public DialogBuilder setOnDismissOverridingListener(DialogInterface.OnDismissListener listener) {
        this.mDismissOverridingListener = listener;
        this.mAlertDialogBuilder.setOnDismissListener(listener);
        return this;
    }

    /**
     * This is used by activity to expand the listener for activity resulting purpose
     *
     * @param listener The expansion listener
     * @return The Builder
     */
    DialogBuilder setOnDismissListenerExpansion(DialogInterface.OnDismissListener listener) {
        this.mAlertDialogBuilder.setOnDismissListener(listener);
        return this;
    }

    public DialogInterface.OnDismissListener getDismissOverridingListener() {
        return mDismissOverridingListener;
    }

    /**
     * Other overriden methods
     */

    public DialogBuilder setTitle(CharSequence title) {
        this.mAlertDialogBuilder.setTitle(title);
        return this;
    }

    public DialogBuilder setMessage(CharSequence message) {
        this.mAlertDialogBuilder.setMessage(message);
        return this;
    }

    public DialogBuilder setView(View view) {
        this.mAlertDialogBuilder.setView(view);
        return this;
    }

    public DialogBuilder setAdapter(ListAdapter adapter) {
        this.mAlertDialogBuilder.setAdapter(adapter, null);
        return this;
    }

    /**
     * Final touch of building dialog
     *
     * @return Built dialog
     */
    AlertDialog create() {
        return this.mAlertDialogBuilder.create();
    }

    public interface OnOverridingButtonClickListener {
        void onClick(View view, DialogInterface dialog);
    }
}
