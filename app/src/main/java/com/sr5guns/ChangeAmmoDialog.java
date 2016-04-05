package com.sr5guns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Thea on 2016-04-02.
 */
public class ChangeAmmoDialog extends DialogFragment {

    public static final String ARG_AMMO_TYPE = "ammo type";
    public static final String ARG_GUN_TYPE = "gun type";

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
    }

    private OnClickListener listener;
    private String[] items;
    private int clipIndex;

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
        items = Arrays.getInstance().getAmmoNames(getArguments().getString(ARG_GUN_TYPE));
        clipIndex = getArguments().getInt(MainActivity.ARG_CLIP_INDEX);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Change Ammo")
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
            data.putString(ARG_AMMO_TYPE, items[which]);
            data.putInt(MainActivity.ARG_CLIP_INDEX, clipIndex);
            listener.onDialogClick(getTag(), data);
        }
    }

}
