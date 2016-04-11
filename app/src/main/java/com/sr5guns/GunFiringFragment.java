package com.sr5guns;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.sr5guns.items.Arrays;
import com.sr5guns.items.Clip;
import com.sr5guns.items.Combat;
import com.sr5guns.items.Gun;
import com.sr5guns.items.Runner;
import com.sr5guns.views.ClipView;


public class GunFiringFragment extends GunFragment implements AdapterView.OnItemSelectedListener, SwipeRefreshLayout.OnRefreshListener {
    private static final String ARG_GUN_INDEX = "gun index";
    private static final String[][] gunTypeSkills = {{"Taser", "Hold-out", "Light pistol", "Heavy pistol", "Machine pistol", "Submachine Gun", "Assault rifle", "Sniper rifle", "Shotgun", "Special weapon"},
            {"Pistols", "Pistols", "Pistols", "Pistols", "Automatics|Pistols", "Automatics", "Automatics", "Longarms", "Longarms", "Exotic Ranged Weapon"}};

    private int gunIndex;
    private Spinner clipSpinner;
    private Spinner modeSpinner;
    private OnFragmentInteractionListener mListener;
    private int nextRecoilIncrese;
    private int nextBulletUsage;
    private SwipeRefreshLayout refresh;

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
        clipSpinner = (Spinner)view.findViewById(R.id.spinner_clips);
        clipSpinner.setOnItemSelectedListener(this);
        modeSpinner = (Spinner)view.findViewById(R.id.spinner_mode);
        modeSpinner.setOnItemSelectedListener(this);
        Switch sw = (Switch)view.findViewById(R.id.switch_smart_gun);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherClick(v);
            }
        });
        sw = (Switch)view.findViewById(R.id.switch_wireless);
        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherClick(v);
            }
        });
        Button btn = (Button)view.findViewById(R.id.btn_reset_recoil);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherClick(v);
            }
        });

        TextView tv = (TextView)view.findViewById(R.id.text_wound_penalty);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                woundPenaltyClick(v);
            }
        });

        btn = (Button)view.findViewById(R.id.btn_fire);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fire();
            }
        });

        refresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        refresh.setOnRefreshListener(this);

        if(gunIndex >= 0) {
            updateView(view);
        }
        return view;
    }

    private void fire() {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        Clip clip = (Clip)clipSpinner.getSelectedItem();
        Combat combat = Combat.get();

        combat.currentRecoil += nextRecoilIncrese;
        gun.setCurrent(clip);
        clip.fire(nextBulletUsage);

        ((ArrayAdapter<Clip>)clipSpinner.getAdapter()).notifyDataSetChanged();
        TextView tv = (TextView)getView().findViewById(R.id.text_current_recoil);
        tv.setText("" + combat.currentRecoil);

        updateGrid(getView());
    }

    private void woundPenaltyClick(View view) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath(MainActivity.DIALOG_WOUND_PENALTY);
        if (mListener != null) {
            mListener.onFragmentInteraction(builder.build());
        }
    }

    private void otherClick(View view) {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        Combat combat = Combat.get();
        switch(view.getId()) {
            case R.id.switch_smart_gun:
                combat.smartLinkOverLaser = ((Switch)view).isChecked();
                updateGrid(getView());
                break;
            case R.id.switch_wireless:
                combat.wireless = ((Switch)view).isChecked();
                updateGrid(getView());
                break;
            case R.id.btn_reset_recoil:
                combat.currentRecoil = 0;
                TextView tv = (TextView)getView().findViewById(R.id.text_current_recoil);
                tv.setText(""+combat.currentRecoil);
                updateGrid(getView());
                break;
        }
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

        ArrayAdapter<Clip> clipArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gun.clips);
        clipArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        clipSpinner.setAdapter(clipArrayAdapter);

        Combat combat = Combat.get();
        Switch sw = (Switch)view.findViewById(R.id.switch_smart_gun);
        sw.setChecked(combat.smartLinkOverLaser);
        sw = (Switch)view.findViewById(R.id.switch_wireless);
        sw.setChecked(combat.wireless);
        TextView tv = (TextView)view.findViewById(R.id.text_current_recoil);
        tv.setText(""+combat.currentRecoil);

        ArrayAdapter<String> modeArrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, gun.getFiringModes());
        modeArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(modeArrayAdapter);
        modeSpinner.setSelection(combat.firingMode);

        updateGrid(view);
    }

    @Override
    public int getGunIndex() {
        return gunIndex;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       updateGrid(getView());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void updateGrid(View view) {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        Clip clip = gun.clips.get(clipSpinner.getSelectedItemPosition());
        Combat combat = Combat.get();
        Runner runner = Runner.getInstance();

        view.findViewById(R.id.btn_fire).setEnabled(clip.getBulletCount() > 0);

        TextView tv = (TextView)view.findViewById(R.id.text_bullet_usage);
        int bulletUsage = 1;
        int defMod = 0;
        switch((String)modeSpinner.getSelectedItem()) {
            case "Semi Auto Burst":
            case "Burst Fire":
                bulletUsage += 2;
                defMod = -2;
                break;
            case "Long Burst":
            case "Full Auto (Simp)":
                bulletUsage += 5;
                defMod = -5;
                break;
            case "Full Auto (Cmpx)":
                bulletUsage += 9;
                defMod = -9;
                break;
            case "Surpressive Fire":
                bulletUsage +=  19;
                defMod = 1;
                break;
        }
        if(bulletUsage > clip.getBulletCount()) {
            if(defMod < 0) {
                defMod += bulletUsage - clip.getBulletCount();
            } else {
                defMod = 1 + bulletUsage - clip.getBulletCount();
            }
            bulletUsage = clip.getBulletCount();
        }
        tv.setText("" + bulletUsage);
        nextBulletUsage = bulletUsage;

        ClipView cv = (ClipView)view.findViewById(R.id.clipView);
        cv.setClip(clip, bulletUsage);

        tv = (TextView)view.findViewById(R.id.text_def_mod);
        if(defMod <= 0) {
            tv.setText(""+defMod);
        } else {
            String txt = "Duck or Cover";
            if(defMod > 1) {
                txt += "\nwidth " +(10 - (defMod-1)/2) +"m";
            }
            tv.setText(txt);
        }

        tv = (TextView)view.findViewById(R.id.text_recoil_increase);
        int recoil = 1 + ClipView.ceil(runner.strength/3f) + gun.getRecoilInt() + gun.getAccessoryBonuses("recoil comp", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug) - bulletUsage;
        if(recoil < 0) {
            tv.setText(""+(-recoil));
            nextRecoilIncrese = -recoil;
        } else {
            tv.setText("0");
            nextRecoilIncrese = 0;
        }

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
        dp += gun.getAccessoryBonuses("dice pool", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug) + combat.woundPenalty;
        if(recoil < 0 && defMod <= 0) {
            dp += recoil;
            dp -= combat.currentRecoil;
        }
        if(dp <= 0) {
            dp = 1;
        }
        tv.setText(""+dp);

        tv = (TextView)view.findViewById(R.id.text_accuracy);
        tv.setText(""+(gun.getAccuracy() +gun.getAccessoryBonuses("accuracy", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug)));

        tv = (TextView)view.findViewById(R.id.text_ap);
        tv.setText(""+(gun.getAP() +gun.getAccessoryBonuses("ap", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug) +clip.getAPModInt()));

        tv = (TextView)view.findViewById(R.id.text_damage);
        int damage = gun.getDamageInt() +gun.getAccessoryBonuses("damage", combat.smartLinkOverLaser, combat.wireless, runner.smartLinkWithAug) +clip.getDamageModInt();
        String damageType = gun.getDamageType().substring(0, 1);
        if(clip.getDamageType() != null) {
            damageType = clip.getDamageType().substring(0, 1);
        }
        if(gun.getDamageSubtype() != null) {
            damageType += "(" +gun.getDamageSubtype().charAt(0) +")";
        }
        if(clip.getDamageSubtype() != null) {
            damageType += "(" +clip.getDamageSubtype().charAt(0) +")";
        }
        tv.setText(damage +damageType);
    }

    @Override
    public void onRefresh() {
        updateView(getView());
        refresh.setRefreshing(false);
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
