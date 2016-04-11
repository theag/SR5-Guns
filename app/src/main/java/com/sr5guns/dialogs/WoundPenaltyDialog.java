package com.sr5guns.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import com.sr5guns.R;
import com.sr5guns.items.Combat;

/**
 * Created by nbp184 on 2016/04/11.
 */
public class WoundPenaltyDialog extends DialogFragment {

    public static final String ARG_WOUND_PENALTY = "wound penalty";

    public interface OnClickListener {
        void onDialogClick(String tag, Bundle data);
    }

    private OnClickListener listener;
    private NumberPicker woundPenalty;

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
        View inflated = inflater.inflate(R.layout.dialog_wound_pentalty, null);

        woundPenalty = (NumberPicker)inflated.findViewById(R.id.numberPicker);
        woundPenalty.setMaxValue(10);
        woundPenalty.setMinValue(0);
        woundPenalty.setDisplayedValues(new String[]{"0", "-1", "-2", "-3", "-4", "-5", "-6", "-7", "-8", "-9", "-10"});
        woundPenalty.setValue(-Combat.get().woundPenalty);

        builder.setTitle("Wound Penalty")
                .setView(inflated)
                .setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doClick();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(true);
        return builder.create();
    }

    private void doClick() {
        if(listener != null) {
            Bundle data = new Bundle();
            data.putInt(WoundPenaltyDialog.ARG_WOUND_PENALTY, -woundPenalty.getValue());
            listener.onDialogClick(getTag(), data);
        }
    }

}