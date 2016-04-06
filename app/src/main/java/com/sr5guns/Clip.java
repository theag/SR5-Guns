package com.sr5guns;

import java.util.StringTokenizer;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class Clip {

    public static Clip load(String input, String delimiter, String gunType) {
        StringTokenizer tokens = new StringTokenizer(input, delimiter);
        Clip rv = new Clip(tokens.nextToken(), Integer.parseInt(tokens.nextToken()), Arrays.getInstance().getAmmo(gunType, tokens.nextToken()));
        rv.bulletCount = Integer.parseInt(tokens.nextToken());
        return rv;
    }

    public final String type;
    public final int size;
    private Ammo ammo;
    private int bulletCount;
    public boolean isCurrent;

    public Clip(String type, int size, Ammo ammo) {
        this.type = type;
        this.size = size;
        this.ammo = ammo;
        this.bulletCount = 0;
        isCurrent = false;
    }

    public String toString() {
        String rv = ammo.getName() +" (" +type +")";
        if(isCurrent) {
            rv += " Current";
        }
        return rv;
    }

    public void reload() {
        int need = size - bulletCount;
        if(need <= ammo.count) {
            bulletCount += need;
            ammo.count -= need;
        } else {
            bulletCount += ammo.count;
            ammo.count = 0;
        }
    }

    public String getDamageModShort() {
        return ammo.getDamageModShort();
    }

    public boolean isUsing(Ammo ammo) {
        return this.ammo == ammo;
    }

    public String getAPMod() {
        return ammo.getAPModShort();
    }

    public String getAmmoCountString() {
        return bulletCount +"/" +size;
    }

    public void returnAmmo() {
        ammo.count += bulletCount;
        bulletCount = 0;
    }

    public Ammo getAmmo() {
        return ammo;
    }

    public String getAmmoType() {
        return ammo.getName();
    }

    public void setAmmo(Ammo ammo) {
        this.ammo = ammo;
    }

    public boolean isDamageInteresting() {
        return ammo.isDamageInteresting();
    }

    public String getDamageModLong() {
        return ammo.getDamageModLong();
    }

    public String save(String delimiter) {
        return type +delimiter +size +delimiter +ammo.getName() +delimiter +bulletCount;
    }
}
