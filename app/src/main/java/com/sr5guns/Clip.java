package com.sr5guns;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class Clip {

    public final String type;
    public final int size;
    private Ammo ammo;
    private int bulletCount;

    public Clip(String type, int size, Ammo ammo) {
        this.type = type;
        this.size = size;
        this.ammo = ammo;
        this.bulletCount = 0;
    }

    public String toString() {
        return ammo.getName() +" (" +type +")";
    }

    public boolean reload() {
        int need = size - bulletCount;
        if(need < ammo.count) {
            bulletCount += need;
            ammo.count -= need;
            return true;
        } else {
            return false;
        }
    }

    public String getDamageModShort() {
        return ammo.getDamageModShort();
    }

    public boolean isUsing(Ammo ammo) {
        return this.ammo == ammo;
    }
}
