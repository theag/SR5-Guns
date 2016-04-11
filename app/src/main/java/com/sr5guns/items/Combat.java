package com.sr5guns.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by Thea on 2016-04-09.
 */
public class Combat {

    private static Combat instance = null;

    public static Combat get() {
        if(instance == null) {
            instance = new Combat();
        }
        return instance;
    }

    public static void save(File dir, int gunIndex) {
        if(instance != null) {
            try {
                PrintWriter outFile = new PrintWriter(new File(dir, "saveCombat.txt"));
                outFile.println(gunIndex);
                outFile.println(instance.saveStr());
                outFile.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace(System.out);
            }
        }
    }

    public static int load(File dir) {
        instance = new Combat();
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(new File(dir, "saveCombat.txt")));
            int rv = Integer.parseInt(inFile.readLine());
            StringTokenizer tokens = new StringTokenizer(inFile.readLine(), Arrays.unitSep);
            inFile.close();
            instance.smartLinkOverLaser = Boolean.parseBoolean(tokens.nextToken());
            instance.wireless = Boolean.parseBoolean(tokens.nextToken());
            instance.currentRecoil = Integer.parseInt(tokens.nextToken());
            instance.firingMode = Integer.parseInt(tokens.nextToken());
            instance.woundPenalty = Integer.parseInt(tokens.nextToken());
            return rv;
        } catch (IOException e) {
            e.printStackTrace(System.out);
            return 0;
        }
    }

    public boolean smartLinkOverLaser;
    public boolean wireless;
    public int currentRecoil;
    public int firingMode;
    public int woundPenalty;

    public Combat() {
        smartLinkOverLaser = true;
        wireless = true;
        currentRecoil = 0;
        firingMode = 0;
        woundPenalty = 0;
    }

    private String saveStr() {
        return smartLinkOverLaser +Arrays.unitSep +wireless +Arrays.unitSep +currentRecoil +Arrays.unitSep +firingMode +Arrays.unitSep +woundPenalty;
    }
}
