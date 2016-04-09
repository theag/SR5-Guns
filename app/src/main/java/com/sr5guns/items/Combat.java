package com.sr5guns.items;

/**
 * Created by Thea on 2016-04-09.
 */
public class Combat {

    private static Combat current = null;

    public static Combat getCombat(Gun gun) {
        if(current == null || current.gun != gun) {
            current = new Combat(gun);
        }
        return current;
    }

    public final Gun gun;
    public boolean smartLinkOverLaser;
    public boolean wireless;
    public int currentRecoil;
    public int firingMode;
    public int woundPenalty;

    public Combat(Gun gun) {
        this.gun = gun;
        smartLinkOverLaser = true;
        wireless = true;
        currentRecoil = 0;
        firingMode = 0;
        woundPenalty = 0;
    }

}
