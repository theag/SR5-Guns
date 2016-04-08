package com.sr5guns;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.sr5guns.items.Arrays;
import com.sr5guns.items.Clip;
import com.sr5guns.items.Gun;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClipFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClipFragment extends GunFragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private static final String ARG_GUN_INDEX = "gun index";

    private int gunIndex;
    private Spinner clipSpinner;
    private ArrayAdapter<Clip> clipArrayAdapter;

    private OnFragmentInteractionListener mListener;

    public ClipFragment() {
        gunIndex = -1;
    }

    public static ClipFragment newInstance(int gunIndex) {
        ClipFragment fragment = new ClipFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_GUN_INDEX, gunIndex);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            gunIndex = getArguments().getInt(ARG_GUN_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clip, container, false);

        Button btn = (Button)view.findViewById(R.id.btn_add_clip);
        btn.setOnClickListener(this);

        btn = (Button)view.findViewById(R.id.btn_delete);
        btn.setOnClickListener(this);

        btn = (Button)view.findViewById(R.id.btn_change_ammo_type);
        btn.setOnClickListener(this);

        btn = (Button)view.findViewById(R.id.btn_reload);
        btn.setOnClickListener(this);

        RadioButton radioButton = (RadioButton)view.findViewById(R.id.radio_current);
        radioButton.setOnClickListener(this);

        TextView tv = (TextView)view.findViewById(R.id.text_damage_mod);
        tv.setOnClickListener(this);

        if(gunIndex >= 0) {
            updateView(view);
        }
        return view;
    }

    private void updateView(View view) {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        clipSpinner = (Spinner)view.findViewById(R.id.spinner_clips);
        clipArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gun.clips);
        clipArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clipSpinner.setAdapter(clipArrayAdapter);
        clipSpinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void setGunIndex(int gunIndex) {
        this.gunIndex = gunIndex;
        if(gunIndex >= 0 && getView() != null) {
            updateView(getView());
        }
    }

    @Override
    public int getGunIndex() {
        return gunIndex;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        View top = getView();
        if(top != null) {
            Clip clip = (Clip)clipSpinner.getSelectedItem();
            TextView tv = (TextView)top.findViewById(R.id.text_ammo_type);
            tv.setText(clip.getAmmoType());
            tv = (TextView)top.findViewById(R.id.text_damage_mod);
            tv.setText(clip.getDamageModShort());
            tv = (TextView)top.findViewById(R.id.text_ap_mod);
            tv.setText(clip.getAPMod());
            tv = (TextView)top.findViewById(R.id.text_in_clip);
            tv.setText(clip.getAmmoCountString());
            if(Arrays.getInstance().guns.get(gunIndex).isCurrent(clip)) {
                RadioButton rb = (RadioButton)top.findViewById(R.id.radio_current);
                rb.setChecked(true);
                Button btn = (Button)top.findViewById(R.id.btn_delete);
                btn.setEnabled(false);
            } else {
                RadioButton rb = (RadioButton)top.findViewById(R.id.radio_current);
                rb.setChecked(false);
                Button btn = (Button)top.findViewById(R.id.btn_delete);
                btn.setEnabled(true);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Arrays arrays = Arrays.getInstance();
        Gun gun = arrays.guns.get(gunIndex);
        Clip clip = (Clip)clipSpinner.getSelectedItem();
        Uri.Builder builder;
        switch(v.getId()) {
            case R.id.btn_add_clip:
                gun.clips.add(new Clip(gun.getClipType(), gun.getAmmoCount(), arrays.getAmmo(gun.getType(), "Regular")));
                clipArrayAdapter.notifyDataSetChanged();
                clipSpinner.setSelection(gun.clips.size() - 1);
                break;
            case R.id.btn_delete:
                clip.returnAmmo();
                gun.clips.remove(clip);
                clipArrayAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_change_ammo_type:
                builder = new Uri.Builder();
                builder.appendPath(MainActivity.DIALOG_CHANGE_AMMO);
                builder.appendQueryParameter(MainActivity.ARG_CLIP_INDEX, ""+clipSpinner.getSelectedItemPosition());
                mListener.onFragmentInteraction(builder.build());
                break;
            case R.id.btn_reload:
                clip.reload();
                TextView tv = (TextView)getView().findViewById(R.id.text_in_clip);
                tv.setText(clip.getAmmoCountString());
                break;
            case R.id.radio_current:
                RadioButton rb = (RadioButton)v;
                if(rb.isChecked()) {
                    gun.setCurrent(clip);
                    Button btn = (Button)getView().findViewById(R.id.btn_delete);
                    btn.setEnabled(false);
                }
                break;
            case R.id.text_damage_mod:
                if(clip.isDamageInteresting()) {
                    builder = new Uri.Builder();
                    builder.appendPath(MainActivity.DIALOG_TEXT_POPUP);
                    builder.appendQueryParameter(MainActivity.ARG_VIEW_ID, ""+R.id.text_damage_mod);
                    builder.appendQueryParameter(MainActivity.ARG_CLIP_INDEX, ""+clipSpinner.getSelectedItemPosition());
                    mListener.onFragmentInteraction(builder.build());
                }
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
