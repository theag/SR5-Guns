package com.sr5guns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class TextPopupDialog extends DialogFragment {

    public static final String ARG_TITLE = "title";
    public static final String ARG_MESSAGE = "message";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getArguments().getInt(ARG_TITLE))
                .setMessage(getArguments().getString(ARG_MESSAGE))
                .setCancelable(true);
        return builder.show();
    }
}
