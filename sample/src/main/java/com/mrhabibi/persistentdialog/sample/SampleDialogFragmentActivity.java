package com.mrhabibi.persistentdialog.sample;

import com.mrhabibi.persistentdialog.DialogActivity;

/**
 * Created by mrhabibi on 4/28/17.
 */

public class SampleDialogFragmentActivity extends DialogActivity {

    @Override
    protected int injectContentViewRes() {
        return R.layout.activity_sample_dialog_fragment;
    }

    @Override
    protected int injectFragmentContainerRes() {
        return R.id.container;
    }
}
