package com.sr5guns;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GunDetailsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GunDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GunDetailsFragment extends GunFragment implements MountedAccessoriesView.OnTapListener {
    private static final String ARG_GUN_INDEX = "gun index";

    private int gunIndex;

    private OnFragmentInteractionListener mListener;

    public GunDetailsFragment() {
        gunIndex = -1;
    }

    public static GunDetailsFragment newInstance(int gunIndex) {
        GunDetailsFragment fragment = new GunDetailsFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gun_details, container, false);
        if(gunIndex >= 0) {
            updateView(view);
        }
        TextView tv = (TextView)view.findViewById(R.id.text_damage);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTextPressed(v);
            }
        });
        tv = (TextView)view.findViewById(R.id.text_accuracy);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTextPressed(v);
            }
        });
        tv = (TextView)view.findViewById(R.id.text_mode);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTextPressed(v);
            }
        });
        tv = (TextView)view.findViewById(R.id.text_ammo);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTextPressed(v);
            }
        });

        MountedAccessoriesView mav = (MountedAccessoriesView)view.findViewById(R.id.mounted_accessories);
        mav.setListener(this);
        return view;
    }

    private void updateView(View view) {
        Gun gun = Arrays.getInstance().guns.get(gunIndex);
        //EditText et = (EditText) view.findViewById(R.id.text_name);
        //et.setText(gun.getName());
        TextView tv = (TextView)view.findViewById(R.id.text_damage);
        tv.setText(gun.getDamageShort());
        tv = (TextView)view.findViewById(R.id.text_accuracy);
        tv.setText(gun.getAccuracyShort());
        tv = (TextView)view.findViewById(R.id.text_ap);
        tv.setText(gun.getAPString());
        tv = (TextView)view.findViewById(R.id.text_mode);
        tv.setText(gun.getModesShort());
        tv = (TextView)view.findViewById(R.id.text_recoil);
        tv.setText(gun.getRecoil());
        tv = (TextView)view.findViewById(R.id.text_ammo);
        tv.setText(gun.getAmmoShort());
        MountedAccessoriesView mav = (MountedAccessoriesView)view.findViewById(R.id.mounted_accessories);
        mav.setGunIndex(gunIndex);
    }

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

    public void onTextPressed(View view) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath(MainActivity.DIALOG_TEXT_POPUP);
        builder.appendQueryParameter(MainActivity.ARG_VIEW_ID, ""+view.getId());
        if (mListener != null) {
            mListener.onFragmentInteraction(builder.build());
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
    public void onTapAccessory(byte mount) {
        Uri.Builder builder = new Uri.Builder();
        builder.appendPath(MainActivity.DIALOG_ADD_ACCESSORY);
        builder.appendQueryParameter(MainActivity.ARG_MOUNT, "" + mount);
        if (mListener != null) {
            mListener.onFragmentInteraction(builder.build());
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
