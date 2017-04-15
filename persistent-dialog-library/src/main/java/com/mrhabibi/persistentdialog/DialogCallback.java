package com.mrhabibi.persistentdialog;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by mrhabibi on 1/9/17.
 * This interface used for passing activity result code and dialog responses as a callback
 */

public interface DialogCallback {

    /**
     * Called when dialog will already be destroyed, the responses bundle wrapped, and will be
     * sent to the activity result
     *
     * @param responses Passed responses bundle
     */
    void onBundleResponses(Bundle responses);

    /**
     * Get passed result to activity for callback
     *
     * @return The result code
     */
    int getResultCode();

}
