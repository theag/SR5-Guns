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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class AddAmmoDialog extends DialogFragment implements AdapterView.OnItemSelectedListener {

    public interface OnClickListener {
        void onAddAmmo(boolean doNext);
    }

    private OnClickListener listener;
    private Spinner gunTypes;
    private Spinner ammoTypes;

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

        Arrays arrays = Arrays.getInstance();

        ammoTypes = (Spinner)inflated.findViewById(R.id.spinner_ammo_types);

        gunTypes = (Spinner)inflated.findViewById(R.id.spinner_gun_types);
        ArrayAdapter<String> guntypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrays.getGunTypes());
        guntypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gunTypes.setAdapter(guntypeAdapter);
        gunTypes.setOnItemSelectedListener(this);

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
            Arrays arrays = Arrays.getInstance();
            arrays.ammo.add(new Ammo((Ammo.Template)ammoTypes.getSelectedItem(), (String)gunTypes.getSelectedItem(), 0));
            listener.onAddAmmo(doNext);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Arrays arrays = Arrays.getInstance();
        ArrayAdapter<Ammo.Template> ammoTypeAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, arrays.getFreeAmmoTemplates((String)gunTypes.getSelectedItem()));
        ammoTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ammoTypes.setAdapter(ammoTypeAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
