package com.sr5guns.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by nbp184 on 2016/03/31.
 */
public class ReplaceAccessoryDialog extends DialogFragment {

    public static final String ARG_MOUNT = "mount";
    public static final String ARG_OLD_NAME = "old name";
    public static final String ARG_NAME = "name";
    public static final String ARG_OTHER_INDEX = "other index";

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
    }

    private OnClickListener listener;
    private String name;
    private int index;
    private byte mount;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(activity instanceof OnClickListener) {
            listener = (OnClickListener)activity;
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mount = getArguments().getByte(ARG_MOUNT);
        name = getArguments().getString(ARG_NAME);
        index = getArguments().getInt(ARG_OTHER_INDEX, -1);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Warning")
                .setMessage("You already have a " + getArguments().getString(ARG_OLD_NAME) + " mounted there, replace it with " + name + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doClick();
                    }
                })
                .setNegativeButton("No", null);
        return builder.create();
    }

    private void doClick() {
        if(listener != null) {
            Bundle data = new Bundle();
            data.putByte(ARG_MOUNT, mount);
            data.putString(ARG_NAME, name);
            data.putInt(ARG_OTHER_INDEX, index);
            listener.onDialogClick(getTag(), data);
        }
    }

}
