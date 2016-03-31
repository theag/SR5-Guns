package com.sr5guns;

import android.support.v4.app.Fragment;

/**
 * Created by nbp184 on 2016/03/30.
 */
public abstract class GunFragment extends Fragment {

    public abstract void setGunIndex(int gunIndex);

    public abstract int getGunIndex();
}
