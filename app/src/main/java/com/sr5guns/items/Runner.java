package com.sr5guns.items;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by Thea on 2016-04-02.
 */
public class Runner {

    private static Runner instance = null;

    public static Runner getInstance() {
        if(instance == null) {
            instance = new Runner();
        }
        return instance;
    }

    public static void loadRunner(String line) {
        if(instance == null) {
            instance = new Runner();
        }
        StringTokenizer tokens = new StringTokenizer(line, Arrays.recordSep);
        instance.smartLinkWithAug = Boolean.parseBoolean(tokens.nextToken());
        instance.agility = Integer.parseInt(tokens.nextToken());
        instance.strength = Integer.parseInt(tokens.nextToken());
        instance.automatics = Integer.parseInt(tokens.nextToken());
        instance.longarms = Integer.parseInt(tokens.nextToken());
        instance.pistols = Integer.parseInt(tokens.nextToken());
        while(tokens.hasMoreTokens()) {
            instance.exotics.add(Skill.load(tokens.nextToken()));
        }
    }

    public boolean smartLinkWithAug;
    public int agility;
    public int strength;
    public int automatics;
    public int longarms;
    public int pistols;
    private final ArrayList<Skill> exotics;

    public Runner() {
        smartLinkWithAug = false;
        agility = 1;
        strength = 1;
        automatics = 0;
        longarms = 0;
        pistols = 0;
        exotics = new ArrayList<>();
        for(Gun gun : Arrays.getInstance().guns) {
            if(gun.getType().compareTo("Special weapon") == 0) {
                exotics.add(new Skill(gun.getName()));
            }
        }
    }

    public String save() {
        String rv = smartLinkWithAug +Arrays.recordSep +agility +Arrays.recordSep +strength +Arrays.recordSep +automatics +Arrays.recordSep +longarms +Arrays.recordSep +pistols;
        for(Skill skill : exotics) {
            rv += Arrays.recordSep +skill.save();
        }
        return rv;
    }

    public int getExoticSkillCount() {
        return exotics.size();
    }

    public Skill getExoticSkill(int position) {
        return exotics.get(position);
    }

    public boolean hasExoticSkill(String name) {
        for(Skill skill : exotics) {
            if(skill.name.compareTo(name) == 0) {
                return true;
            }
        }
        return false;
    }

    public void addExoticSkill(String name) {
        exotics.add(new Skill(name));
    }

    public int getExoticSkill(String name) {
        for(Skill skill : exotics) {
            if(skill.name.compareTo(name) == 0) {
                return skill.value;
            }
        }
        return 0;
    }

    public static final class Skill {
        public static Skill load(String line) {
            StringTokenizer tokens = new StringTokenizer(line, Arrays.unitSep);
            return new Skill(tokens.nextToken(), Integer.parseInt(tokens.nextToken()));
        }

        public String name;
        public int value;

        public Skill(String name) {
            this.name = name;
            value = 0;
        }

        public Skill(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String save() {
            return name +Arrays.unitSep +value;
        }
    }

}
