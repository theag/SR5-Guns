package com.sr5guns;

import java.util.ArrayList;

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

    public static final class Skill {
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
    }

}
