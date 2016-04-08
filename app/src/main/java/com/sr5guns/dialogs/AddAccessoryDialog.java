package com.sr5guns.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.sr5guns.items.Arrays;
import com.sr5guns.items.Gun;

/**
 * Created by nbp184 on 2016/03/31.
 */
public class AddAccessoryDialog extends DialogFragment {

    public static final String ARG_MOUNT = "mount";
    public static final String ARG_NAME = "name";
    public static final String ARG_OTHER_INDEX = "other index";

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
    }

    private OnClickListener listener;
    private String[] items;
    private byte mount;
    private int otherIndex;

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
        otherIndex = getArguments().getInt(ARG_OTHER_INDEX, -1);
        items = Arrays.getInstance().getAccessoryTemplateNameArray(mount);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add " + Gun.getMountName(mount) +" Accessory")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doClick(which);
                    }
                })
                .setCancelable(true);
        return builder.create();
    }

    private void doClick(int which) {
        if(listener != null) {
            Bundle data = new Bundle();
            data.putByte(ARG_MOUNT, mount);
            data.putString(ARG_NAME, items[which]);
            if(mount == 0b1000) {
                data.putInt(ARG_OTHER_INDEX, otherIndex);
            }
            listener.onDialogClick(getTag(), data);
        }
    }

}
