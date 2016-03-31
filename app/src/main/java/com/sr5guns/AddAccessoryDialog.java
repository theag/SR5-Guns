package com.sr5guns;

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
public class AddAccessoryDialog extends DialogFragment {

    public static final String ARG_MOUNT = "mount";
    public static final String ARG_NAME = "name";

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
    }

    private OnClickListener listener;
    private String[] items;
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
        items = Arrays.getInstance().getAccessoryTemplateNameArray(mount);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add " +Gun.getMountName(mount) +" Accessory")
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
            listener.onDialogClick(getTag(), data);
        }
    }

}
