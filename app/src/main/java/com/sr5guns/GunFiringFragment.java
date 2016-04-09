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
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.sr5guns.items.Arrays;
import com.sr5guns.items.Clip;
import com.sr5guns.items.Combat;
import com.sr5guns.items.Gun;
import com.sr5guns.items.Runner;

import java.lang.reflect.Array;


public class GunFiringFragment extends GunFragment implements AdapterView.OnItemSelectedListener {
    private static final String ARG_GUN_INDEX = "gun index";
    private static final String[][] gunTypeSkills = {{"Taser", "Hold-out", "Light pistol", "Heavy pistol", "Machine pistol", "Submachine Gun", "Assault rifle", "Sniper rifle", "Shotgun", "Special weapon"},
            {"Pistols", "Pistols", "Pistols", "Pistols", "Automatics|Pistols", "Automatics", "Automatics", "Longarms", "Longarms", "Exotic Ranged Weapon"}};

    private int gunIndex;

    private OnFragmentInteractionListener mListener;

    public GunFiringFragment() {
        gunIndex = -1;
    }

    public static GunFiringFragment newInstance(int gunIndex) {
        GunFiringFragment fragment = new GunFiringFragment();
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gun_firing, container, false);

        if(gunIndex >= 0) {
            updateView(view);
        }
        return view;
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

    private void updateView(View view) {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        Spinner spinner = (Spinner)view.findViewById(R.id.spinner_clips);
        ArrayAdapter<Clip> clipArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gun.clips);
        clipArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(clipArrayAdapter);
        spinner.setOnItemSelectedListener(this);

        Combat combat = Combat.getCombat(gun);
        Switch sw = (Switch)view.findViewById(R.id.switch_smart_gun);
        sw.setChecked(combat.smartLinkOverLaser);
        sw = (Switch)view.findViewById(R.id.switch_wireless);
        sw.setChecked(combat.wireless);
        TextView tv = (TextView)view.findViewById(R.id.text_current_recoil);
        tv.setText(""+combat.currentRecoil);

        spinner = (Spinner)view.findViewById(R.id.spinner_mode);
        ArrayAdapter<String> modeArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gun.getFiringModes());
        modeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(modeArrayAdapter);
        spinner.setSelection(combat.firingMode);

        Runner runner = Runner.getInstance();

        //TODO: do recoil increase, bullet usage
        tv = (TextView)view.findViewById(R.id.text_wound_penalty);
        tv.setText(""+combat.woundPenalty);
        tv = (TextView)view.findViewById(R.id.text_dice_pool);
        int dp = runner.agility;
        int index = 0;
        for(; index < gunTypeSkills[0].length; index++) {
            if(gun.getType().compareTo(gunTypeSkills[0][index]) == 0) {
                break;
            }
        }
        switch(gunTypeSkills[1][index]) {
            case "Pistols":
                dp += runner.pistols;
                break;
            case "Automatics":
                dp += runner.automatics;
                break;
            case "Longarms":
                dp += runner.longarms;
                break;
            case "Exotic Ranged Weapon":
                dp += runner.getExoticSkill(gun.getName());
                break;
        }
        dp += gun.getAccessoryBonuses("dice pool", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug);
        tv.setText(""+dp);
        tv = (TextView)view.findViewById(R.id.text_accuracy);
        tv.setText(""+(gun.getAccuracy() +gun.getAccessoryBonuses("dice pool", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug)));
    }

    @Override
    public int getGunIndex() {
        return gunIndex;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //TODO: check if spinner is clips or firing mode
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        Clip clip = gun.clips.get(position);
        Combat combat = Combat.getCombat(gun);
        Runner runner = Runner.getInstance();
        TextView tv = (TextView)view.findViewById(R.id.text_ap);
        tv.setText(""+(gun.getAP() +gun.getAccessoryBonuses("ap", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug) +clip.getAPModInt()));
        //TODO: do damage
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
