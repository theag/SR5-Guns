package com.sr5guns;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class Ammo {

    private final Template template;
    public final String gunType;
    public int count;

    public Ammo(Template template, String gunType, int count) {
        this.template = template;
        this.gunType = gunType;
        this.count = count;
    }

    public String listDisplay() {
        return template.name +" (" +gunType +"): " +count;
    }

    public String getName() {
        return template.name;
    }

    public String getDamageModShort() {
        String rv = "";
        if(template.damage > 0) {
            rv += "+";
        }
        rv += template.damage;
        if(template.damageType != null) {
            rv += template.damageType;
        }
        if(template.damageSubType != null) {
            rv += "(" +template.damageSubType +")";
        }
        if(rv.compareTo("0") == 0) {
            return "--";
        }
        return rv;
    }

    public String getAPModShort() {
        if(template.ap == 0) {
            return "--";
        } else if(template.ap > 0) {
            return "+"+template.ap;
        } else {
            return ""+template.ap;
        }
    }

    public static final class Template {
        public final String name;
        public final int damage;
        public final String damageType;
        public final String damageSubType;
        public final int ap;

        public Template(String name, int damage, String damageType, String damageSubType, int ap) {
            this.name = name;
            this.damage = damage;
            this.damageType = damageType;
            this.damageSubType = damageSubType;
            this.ap = ap;
        }

        public String toString() {
            return name;
        }
    }

}
