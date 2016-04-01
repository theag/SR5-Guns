package com.sr5guns;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private static final String[] tabNames = {"Firing", "Details", "Clips"};
    private int gunIndex;

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
        gunIndex = 0;
    }

    public void setGunIndex(int gunIndex) {
        this.gunIndex = gunIndex;
        this.notifyDataSetChanged();
    }

    @Override
    public GunFragment getItem(int position) {
        switch(position) {
            case 0:
                return GunFiringFragment.newInstance(gunIndex);
            case 1:
                return GunDetailsFragment.newInstance(gunIndex);
            case 2:
                return ClipFragment.newInstance(gunIndex);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNames.length;
    }

    @Override
    public int getItemPosition(Object object) {
        if(object instanceof GunDetailsFragment) {
            GunDetailsFragment frag = (GunDetailsFragment)object;
            frag.setGunIndex(gunIndex);
        }
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position];
    }

}
