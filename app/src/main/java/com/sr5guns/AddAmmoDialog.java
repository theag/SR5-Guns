package com.sr5guns;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class AddAmmoDialog extends DialogFragment {

    public static final String ARG_DO_NEXT = "do next";

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
    }

    private OnClickListener listener;

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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflated = inflater.inflate(R.layout.dialog_add_ammo, null);
        //TODO: fill in this view
        builder.setTitle("Add Ammo")
                .setView(inflated)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doClick(false);
                    }
                })
                .setNeutralButton(R.string.insert, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doClick(true);
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true);
        return builder.create();
    }

    private void doClick(boolean doNext) {
        if(listener != null) {
            Bundle data = new Bundle();
            data.putBoolean(ARG_DO_NEXT, doNext);
            listener.onDialogClick(getTag(), data);
        }
    }

}
