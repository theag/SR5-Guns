package com.sr5guns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class Arrays {

    private static Arrays instance = null;

    public static Arrays getInstance() {
        if(instance == null) {
            instance = new Arrays();
        }
        return instance;
    }

    public static boolean isLoaded() {
        return instance != null;
    }

    private ArrayList<Gun.Template> gunTemplates;
    private ArrayList<GunAccessory.Template> accessoryTemplates;
    private ArrayList<Ammo.Template> ammoTemplates;
    //TODO: move this to character very eventually
    public ArrayList<Gun> guns;
    public ArrayList<Ammo> ammo;

    private Arrays() {
        guns = new ArrayList<>();
        gunTemplates = new ArrayList<>();
        accessoryTemplates = new ArrayList<>();
        ammoTemplates = new ArrayList<>();
        ammo = new ArrayList<>();
    }

    public void loadGunTemplates(InputStream open) throws IOException {
        gunTemplates.clear();
        BufferedReader inFile = new BufferedReader(new InputStreamReader(open));
        String line = inFile.readLine();
        StringTokenizer tokens;
        Gun.Template template;
        int count;
        byte mount;
        String name;
        while(line != null) {
            tokens = new StringTokenizer(line, "#");
            template = new Gun.Template(tokens.nextToken(), tokens.nextToken(), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()), tokens.nextToken(), nullConvert(tokens.nextToken()), Integer.parseInt(tokens.nextToken()), Byte.parseByte(tokens.nextToken(), 2), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()), tokens.nextToken(), Byte.parseByte(tokens.nextToken(), 2), Integer.parseInt(tokens.nextToken()));
            count = 0;
            while(tokens.hasMoreTokens()) {
                mount = Byte.parseByte(tokens.nextToken(), 2);
                name = tokens.nextToken();
                if(mount > 1) {
                    template.addAccessory(mount, getAccessoryTemplate(name), Boolean.parseBoolean(tokens.nextToken()));
                } else {
                    template.addAccessory(getAccessoryTemplate(name), Boolean.parseBoolean(tokens.nextToken()), count);
                    count++;
                }
            }
            gunTemplates.add(template);
            line = inFile.readLine();
        }
        inFile.close();
    }

    public GunAccessory.Template getAccessoryTemplate(String name) {
        for(GunAccessory.Template template : accessoryTemplates) {
            if(template.name.compareTo(name) == 0) {
                return template;
            }
        }
        return null;
    }

    public void loadAccessoryTemplates(InputStream open) throws IOException {
        accessoryTemplates.clear();
        BufferedReader inFile = new BufferedReader(new InputStreamReader(open));
        String line = inFile.readLine();
        StringTokenizer tokens;
        GunAccessory.Template template;
        while(line != null) {
            tokens = new StringTokenizer(line, "#");
            template = new GunAccessory.Template(Boolean.parseBoolean(tokens.nextToken()), tokens.nextToken(), emptyConvert(tokens.nextToken()), Byte.parseByte(tokens.nextToken(), 2), Integer.parseInt(tokens.nextToken()), Integer.parseInt(tokens.nextToken()));
            for(int i = 0; i < template.statsAffected[0].length; i++) {
                template.statsAffected[0][i] = tokens.nextToken();
                template.values[0][i] = Integer.parseInt(tokens.nextToken());
            }
            for(int i = 0; i < template.statsAffected[1].length; i++) {
                template.statsAffected[1][i] = tokens.nextToken();
                template.values[1][i] = Integer.parseInt(tokens.nextToken());
            }
            accessoryTemplates.add(template);
            line = inFile.readLine();
        }
        inFile.close();
    }

    public void loadAmmoTemplates(InputStream open) throws IOException {
        ammoTemplates.clear();
        BufferedReader inFile = new BufferedReader(new InputStreamReader(open));
        String line = inFile.readLine();
        StringTokenizer tokens;
        Ammo.Template template;
        while(line != null) {
            tokens = new StringTokenizer(line, "#");
            template = new Ammo.Template(tokens.nextToken(), Integer.parseInt(tokens.nextToken()), nullConvert(tokens.nextToken()), nullConvert(tokens.nextToken()), Integer.parseInt(tokens.nextToken()));
            ammoTemplates.add(template);
            line = inFile.readLine();
        }
        inFile.close();
    }

    private String nullConvert(String s) {
        if(s.compareTo("null") == 0) {
            return null;
        } else {
            return s;
        }
    }

    private String emptyConvert(String s) {
        if(s.compareTo("null") == 0) {
            return "";
        } else {
            return s;
        }
    }

    public String[] getTemplateNameArray(String type) {
        ArrayList<String> arr = new ArrayList<>();
        for(Gun.Template template : gunTemplates) {
            if(template.type.compareTo(type) == 0) {
                arr.add(template.name);
            }
        }
        String[] rv = new String[arr.size()];
        arr.toArray(rv);
        return rv;
    }

    public String[] getGunNameArray() {
        return new String[0];
    }

    public int addGun(int templateIndex) {
        guns.add(new Gun(gunTemplates.get(templateIndex)));
        return guns.size() - 1;
    }

    public int addGun(String templateName) {
        Gun.Template selected = null;
        for(Gun.Template template : gunTemplates) {
            if(template.name.compareTo(templateName) == 0) {
                selected = template;
                break;
            }
        }
        guns.add(new Gun(selected));
        return guns.size() - 1;
    }

    public void clearGuns() {
        guns.clear();
    }

    public String[] getAccessoryTemplateNameArray(byte mount) {
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Nothing");
        for(GunAccessory.Template template : accessoryTemplates) {
            if((template.mount & mount) > 0 && template.available) {
                arr.add(template.name);
            }
        }
        String[] rv = new String[arr.size()];
        arr.toArray(rv);
        return rv;
    }

    public Ammo getAmmo(String gunType, String name) {
        for(Ammo a : ammo) {
            if(a.gunType.compareTo(gunType) == 0 && a.getName().compareTo(name) == 0) {
                return a;
            }
        }
        return null;
    }

    public Ammo.Template getAmmoTemplate(String name) {
        for(Ammo.Template template : ammoTemplates) {
            if(template.name.compareTo(name) == 0) {
                return template;
            }
        }
        return null;
    }

    public boolean canRemoveAmmo(Ammo ammo) {
        if(ammo.count > 0) {
            return false;
        }
        for(Gun gun : guns) {
            for(Clip clip : gun.clips) {
                if(clip.isUsing(ammo)) {
                    return false;
                }
            }
        }
        return true;
    }
}
