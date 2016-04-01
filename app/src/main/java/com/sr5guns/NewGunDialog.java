package com.sr5guns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class NewGunDialog extends DialogFragment {

    public static final String ARG_GUN_TYPE_INDEX = "gun type ndex";
    public static final String ARG_GUN_TEMPLATE_NAME = "gun tempalte name";
    private static final String[][] gunTypesSkills = {{"Taser", "Hold-out", "Light pistol", "Heavy pistol", "Machine pistol", "Submachine Gun", "Assault rifle", "Sniper rifle", "Shotgun", "Special weapon"},
            {"Pistols", "Pistols", "Pistols", "Pistols", "Automatics|Pistols", "Automatics", "Automatics", "Longarms", "Longarms", "Exotic Ranged Weapon"}};

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
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
            items = Arrays.getInstance().getTemplateNameArray(gunTypesSkills[0][index]);
            builder.setTitle("New " +gunTypesSkills[0][index])
                    .setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doGunClick(which);
                        }
                    })
                    .setCancelable(true);
        } else {
            builder.setTitle("New Gun")
                    .setItems(gunTypesSkills[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            doTypeClick(which);
                        }
                    })
                    .setCancelable(true);
        }
        return builder.create();
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