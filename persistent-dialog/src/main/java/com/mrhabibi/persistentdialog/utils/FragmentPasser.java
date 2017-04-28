package com.mrhabibi.persistentdialog.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.mrhabibi.persistentdialog.wrapper.DialogWrapper;

import java.util.HashMap;
import java.util.UUID;

import static com.mrhabibi.persistentdialog.DialogActivity.ALERTDIALOG_ID_PREFIX;
import static com.mrhabibi.persistentdialog.DialogActivity.DIALOGFRAGMENT_ID_PREFIX;

/**
 * Created by mrhabibi on 11/30/16.
 * Utility class that used to keep and get passed fragment from PersistentDialog builder to created
 * DialogActivity, you know, fragment can't be passed via intent, that's why this class is made.
 * One time usage, it means that it just keeps the fragment just once, and once the fragment
 * fetched, it will be removed from the map, because it may cause leak if it's still kept staticly
 */
public class FragmentPasser {

    private static final HashMap<String, Fragment> passedFragment = new HashMap<>();

    @Nullable
    public static Fragment getFragment(String getterId) {
        if (getterId != null && passedFragment.containsKey(getterId)) {
            Fragment fragment = passedFragment.get(getterId);
            passedFragment.remove(getterId);
            return fragment;
        }
        return null;
    }

    public static String setFragment(@NonNull Fragment fragment) {
        String prefix = fragment instanceof DialogWrapper ? ALERTDIALOG_ID_PREFIX : DIALOGFRAGMENT_ID_PREFIX;
        String fragmentGetterId = prefix + UUID.randomUUID().toString();
        passedFragment.put(fragmentGetterId, fragment);

        return fragmentGetterId;
    }

}
