package com.sr5guns.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.sr5guns.MainActivity;
import com.sr5guns.items.Arrays;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class NewGunDialog extends DialogFragment {

    public static final String ARG_GUN_TYPE_INDEX = "gun type ndex";
    public static final String ARG_GUN_TEMPLATE_NAME = "gun tempalte name";
    private static final String[] gunTypes = {"Taser", "Hold-out", "Light pistol", "Heavy pistol", "Machine pistol", "Submachine Gun", "Assault rifle", "Sniper rifle", "Shotgun", "Special weapon"};

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
        void pacifist();
    }

    private OnClickListener listener;
    private String[] items;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if(getArguments() != null && getArguments().getInt(ARG_GUN_TYPE_INDEX, -1) > 0) {
            int index = getArguments().getInt(ARG_GUN_TYPE_INDEX);
            items = Arrays.getInstance().getTemplateNameArray(gunTypes[index]);
            builder.setTitle("New " + gunTypes[index])
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doGunClick(which);
                        }
                    })
                    .setCancelable(true);
        } else {
            builder.setTitle("New Gun")
                    .setItems(gunTypes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doTypeClick(which);
                        }
                    })
                    .setCancelable(true);
        }
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if(listener != null && (getTag().compareTo(MainActivity.DIALOG_FIRST_GUN_1) == 0 || getTag().compareTo(MainActivity.DIALOG_FIRST_GUN_2) == 0)) {
            listener.pacifist();
        }
    }

    private void doGunClick(int which) {
        if(listener != null) {
            Bundle data = new Bundle();
            data.putString(ARG_GUN_TEMPLATE_NAME, items[which]);
            listener.onDialogClick(getTag(), data);
        }
    }

    private void doTypeClick(int which) {
        if(listener != null) {
            Bundle data = new Bundle();
            data.putInt(ARG_GUN_TYPE_INDEX, which);
            listener.onDialogClick(getTag(), data);
        }
    }
}
