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
import android.widget.Spinner;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ClipFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ClipFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ClipFragment extends GunFragment implements AdapterView.OnItemSelectedListener {
    private static final String ARG_GUN_INDEX = "gun index";

    private int gunIndex;
    private Spinner clipSpinner;

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
        if(gunIndex >= 0) {
            updateView(view);
        }
        return view;
    }

    private void updateView(View view) {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        clipSpinner = (Spinner)view.findViewById(R.id.spinner_clips);
        ArrayAdapter<Clip> clipArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gun.clips);
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
            TextView tv = (TextView)top.findViewById(R.id.text_damage_mod);
            tv.setText(clip.getDamageModShort());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
