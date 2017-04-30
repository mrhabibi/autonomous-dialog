package com.mrhabibi.persistentdialog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mrhabibi.persistentdialog.utils.DialogUtils;
import com.mrhabibi.persistentdialog.utils.FragmentPasser;
import com.mrhabibi.persistentdialog.wrapper.DialogWrapper;

/**
 * Created by mrhabibi on 12/2/16.
 * This activity is used by dialog as a host, so, the dialog will have its own activity to host
 * itself, this activity has two modes, dialog fragment mode and alert dialog mode, dialog fragment
 * mode will use whatever fragment and show it with dialog layout, and alert dialog mode will
 * use dialog wrapper to build alert dialog, and both of them support data persistency as well
 */
public class DialogActivity extends AppCompatActivity {

    /**
     * Constants for broadcast dismisser to identify correct dialog to be dismissed
     */
    public static final String IDENTIFIER_KEY = "identifier";
    /**
     * This is the broadcast dismisser to dismiss dialog from whereever
     */
    public static final String DISMISS_ACTION = "dismissAction";

    /**
     * Constant for the fragment in FragmentManager
     */
    public static final String FRAGMENT_TAG = "fragmentTag";

    public static final String FRAGMENT_GETTER_ID_LABEL = "fragmentGetterId";
    public static final String CANCELABLE_LABEL = "cancelable";
    public static final String IDENTIFIER_LABEL = "identifier";
    public static final String THEME_RES_LABEL = "themeRes";
    public static final String PARAMS_LABEL = "params";
    public static final String WILL_REBORN_LABEL = "willReborn";

    public static final String ALERTDIALOG_ID_PREFIX = "AlertDialog_";
    public static final String DIALOGFRAGMENT_ID_PREFIX = "DialogFragment_";

    protected String mFragmentGetterId;
    protected boolean mCancelable;
    protected String mIdentifier;
    @StyleRes
    protected int mThemeRes;
    protected Bundle mParams;

    /**
     * Flag to indicate that dialog will be built again after config changes
     */
    protected boolean mReborn;

    protected boolean mFirstCreation;

    protected Fragment mCurrentFragment;
    protected AlertDialog mCurrentDialog;

    private BroadcastReceiver mActionReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DISMISS_ACTION) && intent.getStringExtra(IDENTIFIER_KEY).equals(mIdentifier)) {
                /*
                 * Remove the noted pending dismissed dialog id
                 */
                DialogUtils.log("Dismissing Remotely ", mIdentifier);
                finish();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        mFirstCreation = savedInstanceState == null;
        extractBundleStates(getIntent().getExtras());

        /*
         * Set activity theme
         */
        if (!isAlertDialog() && mThemeRes > 0) {
            setTheme(mThemeRes);
        }

        super.onCreate(savedInstanceState);

        if (!isAlertDialog()) {
            setContentView(R.layout.activity_dialog);
        }
        /*
         * Set the cancelable behaviour
         */
        setFinishOnTouchOutside(mCancelable);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();

        /*
         * Bring the fragment to live
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mFirstCreation) {
            mCurrentFragment = FragmentPasser.getFragment(mFragmentGetterId);
        } else {
            mCurrentFragment = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
        }

        /*
         * Check if the fragment has expired
         */
        if (mFragmentGetterId != null && mCurrentFragment == null) {
            DialogUtils.log("Dismissing due to Expired Session ", mIdentifier);
            finish();
            return;
        }

        if (mFirstCreation) {
            PersistentDialog.shownDialogIds.put(mIdentifier, true);

            /*
             * Handle race condition when calling show & dismiss together
             */
            if (PersistentDialog.dismissedDialogIds.contains(mIdentifier)) {
                DialogUtils.log("Dismissing due to Race Condition ", mIdentifier);
                PersistentDialog.dismissedDialogIds.remove(mIdentifier);
                finish();
                return;
            }
        }

        if (mCurrentFragment != null && isAlertDialog()) {

            /*
             * Alert Dialog mode
             */
            if (mFirstCreation) {
                fragmentManager.beginTransaction()
                        .add(mCurrentFragment, FRAGMENT_TAG)
                        .commit();
                fragmentManager.executePendingTransactions();
            }

            /*
             * Build the alert dialog from fragment
             */
            buildAlertDialog((DialogWrapper) mCurrentFragment);
        } else {

            /*
             * Dialog Fragment mode
             */
            if (mFirstCreation && mCurrentFragment != null) {
                if (findViewById(injectFragmentContainerRes()) == null) {
                    throw new IllegalStateException("Fragment container resource id not found, did you forget to override injectFragmentContainerRes() in your activity?");
                }
                fragmentManager.beginTransaction()
                        .replace(injectFragmentContainerRes(), mCurrentFragment, FRAGMENT_TAG)
                        .commit();
                fragmentManager.executePendingTransactions();
            }
        }

        onFragmentAttached(mCurrentFragment);
    }

    protected void onFragmentAttached(Fragment fragment) {
    }

    @IdRes
    protected int injectFragmentContainerRes() {
        return R.id.fragment_container;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isFinishing()) {

            /*
             * Unregister shown dialog identifier
             */
            if (mIdentifier != null && mIdentifier.length() > 0) {
                PersistentDialog.shownDialogIds.remove(mIdentifier);
            }

        } else {

            if (mCurrentDialog != null) {

                /*
                 * Handle alert dialog leak when config changes
                 */
                mReborn = true;
            }
        }

        /*
         * Dismiss the dialog too
         */
        if (mCurrentDialog != null) {
            mCurrentDialog.dismiss();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
         * Register the broadcaster
         */
        registerReceiver(mActionReceiver, new IntentFilter(DISMISS_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();

        /*
         * Unregister the broadcaster
         */
        unregisterReceiver(mActionReceiver);
    }

    @Override
    public void finish() {
        /*
         * Set the result and responses for Dialog Fragment mode
         */
        if (!isAlertDialog()) {
            final Intent intent = makeBasicIntent();
            setResult(DialogResult.RESULT_DIALOG_CANCELLED, intent);
            setCallback(intent);
        }
        super.finish();
    }

    /**
     * Build the alert dialog from given fragment
     */
    private void buildAlertDialog(final DialogWrapper dialogWrapper) {
        final DialogBuilder builder = new DialogBuilder(this, mThemeRes > 0 ? mThemeRes : R.style.PersistentAlertDialog);

        /*
         * Collect alert dialog datas
         */
        dialogWrapper.onBuildDialog(builder);

        final Intent intent = makeBasicIntent();

        setResult(DialogResult.RESULT_DIALOG_CANCELLED, intent);

        /*
         * Manipulate the listeners for activity resulting and intent data bundling
         */
        if (builder.getSingleChoiceItems() != null) {
            builder.setSingleChoiceItemsExpansion(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent.putExtra("which", i);
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_SINGLE_CHOICE);
                    if (builder.getSingleChoiceOverridingListener() != null) {
                        builder.getSingleChoiceOverridingListener().onClick(dialogInterface, i);
                    }
                }
            });
        }

        if (builder.getMultiChoiceItems() != null) {
            builder.setMultiChoiceItemsExpansion(new DialogInterface.OnMultiChoiceClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                    intent.putExtra("which", i);
                    intent.putExtra("checked", b);
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_MULTI_CHOICES);
                    if (builder.getMultiChoiceOverridingListener() != null) {
                        builder.getMultiChoiceOverridingListener().onClick(dialogInterface, i, b);
                    }
                }
            });
        }

        if (builder.getPlainChoiceItems() != null) {
            builder.setItemsExpansion(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent.putExtra("which", i);
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_PLAIN_CHOICE);
                    if (builder.getPlainChoiceOverridingListener() != null) {
                        builder.getPlainChoiceOverridingListener().onClick(dialogInterface, i);
                    }
                }
            });
        }

        if (builder.getPositiveText() != null) {
            builder.setPositiveButtonExpansion(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent.putExtra("which", i);
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_POSITIVE_BUTTON);
                }
            });
        }

        if (builder.getNegativeText() != null) {
            builder.setNegativeButtonExpansion(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent.putExtra("which", i);
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_NEGATIVE_BUTTON);
                }
            });
        }

        if (builder.getNeutralText() != null) {
            builder.setNeutralButtonExpansion(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    intent.putExtra("which", i);
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_NEUTRAL_BUTTON);
                }
            });
        }

        /*
         * Bundling the responses and add it to intent data before the dialog dismissed
         */
        builder.setOnDismissListenerExpansion(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                /*
                 * Check if the activity is really going to be destroyed
                 */
                if (!mReborn) {

                    setCallback(intent);

                    if (builder.getDismissOverridingListener() != null) {
                        builder.getDismissOverridingListener().onDismiss(dialogInterface);
                    }

                    /*
                     * At last, finish the host too
                     */
                    finish();
                } else {

                    /*
                     * Reset the reborn indicator
                     */
                    mReborn = false;
                }
            }
        });

        mCurrentDialog = builder.create();

        mCurrentDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {

                /*
                 * Pass the built dialog to fragment to be used in action asynchronously
                 */
                dialogWrapper.onDialogShown(mCurrentDialog);

                /*
                 * Override button's method for alert dialog that has other things to do before
                 * the dialog is dismissed
                 */
                if (builder.getPositiveOverridingListener() != null) {
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_POSITIVE_BUTTON);
                    mCurrentDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builder.getPositiveOverridingListener().onClick(view, dialogInterface);
                        }
                    });
                }
                if (builder.getNegativeOverridingListener() != null) {
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_NEGATIVE_BUTTON);
                    mCurrentDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builder.getNegativeOverridingListener().onClick(view, dialogInterface);
                        }
                    });
                }
                if (builder.getNeutralOverridingListener() != null) {
                    dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_NEUTRAL_BUTTON);
                    mCurrentDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            builder.getNeutralOverridingListener().onClick(view, dialogInterface);
                        }
                    });
                }
            }
        });

        /*
         * Set the cancel result when alert dialog cancelled, with no intent data
         */
        mCurrentDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                dialogWrapper.setResultCode(DialogResult.RESULT_DIALOG_CANCELLED);
            }
        });

        /*
         * Set the alert dialog host
         */
        mCurrentDialog.setOwnerActivity(this);

        /*
         * Cancelable for alert dialog mode
         */
        mCurrentDialog.setCancelable(mCancelable);

        /*
         * Last thing, show the final alert dialog
         */
        mCurrentDialog.show();
    }

    /**
     * Bundling the basic intent datas
     *
     * @return Basic intent datas
     */
    protected Intent makeBasicIntent() {
        if (mParams == null) {
            mParams = new Bundle();
        }

        Intent intent = new Intent();
        intent.putExtra("id", mIdentifier);
        intent.putExtra("params", mParams);
        return intent;
    }

    /**
     * Add responses and/or custom result code to callback intent
     *
     * @param intent Passed intent
     */
    private void setCallback(Intent intent) {

        /*
         * If the fragment has DialogCallback
         */
        if (mCurrentFragment != null && mCurrentFragment instanceof DialogCallback) {

            Bundle responses = new Bundle();

            DialogCallback dialogCallback = (DialogCallback) mCurrentFragment;

            /*
             * Collecting alert dialog response bundle
             */
            dialogCallback.onBundleResponses(responses);

            intent.putExtra("responses", responses);

            /*
             * Reset result code
             */
            int resultCode = dialogCallback.getResultCode();
            setResult(resultCode, intent);
        }
    }

    /**
     * Pass the activity result for nested PersistentDialog
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {

        /*
         * Just destroy the activity, it doesn't have backstack tho
         */
        if (mCancelable) {
            finish();
        }
    }

    private void extractBundleStates(Bundle bundle) {
        if (bundle != null) {
            if (bundle.containsKey(FRAGMENT_GETTER_ID_LABEL)) {
                mFragmentGetterId = bundle.getString(FRAGMENT_GETTER_ID_LABEL);
            }
            if (bundle.containsKey(CANCELABLE_LABEL)) {
                mCancelable = bundle.getBoolean(CANCELABLE_LABEL);
            }
            if (bundle.containsKey(IDENTIFIER_LABEL)) {
                mIdentifier = bundle.getString(IDENTIFIER_LABEL);
            }
            if (bundle.containsKey(THEME_RES_LABEL)) {
                mThemeRes = bundle.getInt(THEME_RES_LABEL);
            }
            if (bundle.containsKey(PARAMS_LABEL)) {
                mParams = bundle.getBundle(PARAMS_LABEL);
            }
        }
    }

    protected boolean isAlertDialog() {
        return mFragmentGetterId != null && mFragmentGetterId.startsWith(ALERTDIALOG_ID_PREFIX);
    }

    protected boolean isDialogFragment() {
        return mFragmentGetterId != null && mFragmentGetterId.startsWith(DIALOGFRAGMENT_ID_PREFIX);
    }

    protected boolean isDialogActivity() {
        return mFragmentGetterId == null;
    }
}
