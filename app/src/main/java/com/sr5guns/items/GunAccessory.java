package com.sr5guns.items;

import java.util.StringTokenizer;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class GunAccessory {

    private static GunAccessory add = null;

    public static GunAccessory getAdd() {
        if(add == null) {
            add = new GunAccessory(new Template(false, "Add New", "", (byte)0, 0, 0), false);
        }
        return add;
    }

    public static GunAccessory load(String input, String delimiter) {
        StringTokenizer tokens = new StringTokenizer(input, delimiter);
        GunAccessory rv = new GunAccessory(Arrays.getInstance().getAccessoryTemplate(tokens.nextToken()), Boolean.parseBoolean(tokens.nextToken()));
        rv.using = Boolean.parseBoolean(tokens.nextToken());
        rv.wireless = Boolean.parseBoolean(tokens.nextToken());
        return rv;
    }

    private final Template template;
    public final boolean permanent;
    public boolean using;
    public boolean wireless;

    public GunAccessory(Template template, boolean permanant) {
        this.template = template;
        this.permanent = permanant;
        using = true;
        wireless = true;
    }

    public int getBonus(String statName) {
        for(int i = 0; i < template.statsAffected[0].length; i++) {
            if(template.statsAffected[0][i].compareTo(statName) == 0) {
                return template.values[0][i];
            }
        }
        return 0;
    }

    public int getWirelessBonus(String statName) {
        for(int i = 0; i < template.statsAffected[1].length; i++) {
            if(template.statsAffected[1][i].compareTo(statName) == 0) {
                return template.values[1][i];
            }
        }
        return 0;
    }

    public String getName() {
        return template.name;
    }

    public String toString() {
        String rv = template.name;
        if(permanent) {
            rv += " (P)";
        }
        return rv;
    }

    public String save(String delimiter) {
        return template.name +delimiter +permanent +delimiter +using +delimiter +wireless +delimiter;
    }

    public static final class Template {
        public final boolean available;
        public final String name;
        public final String description;
        public final byte mount; //top barrel under none
        public final String[][] statsAffected;
        public final int[][] values;

        public Template(boolean available, String name, String description, byte mount, int statCount, int wirelessCount) {
            this.available = available;
            this.name = name;
            this.description = description;
            this.mount = mount;
            statsAffected = new String[2][];
            statsAffected[0] = new String[statCount];
            statsAffected[1] = new String[wirelessCount];
            values = new int[2][];
            values[0] = new int[statCount];
            values[1] = new int[wirelessCount];
        }

    }

}
