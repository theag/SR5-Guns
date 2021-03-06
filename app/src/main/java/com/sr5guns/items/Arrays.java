package com.sr5guns.items;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class Arrays {

    private static Arrays instance = null;
    public static String fileSep = ""+((char)28);
    public static String groupSep = ""+((char)29);
    public static String nullChar = ""+((char)0);
    public static String recordSep = ""+((char)30);
    public static String unitSep = ""+((char)31);

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
        while(line.startsWith("//")) {
            line = inFile.readLine();
        }
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
        if(selected.type.compareTo("Special weapon") == 0 && !Runner.getInstance().hasExoticSkill(selected.name)) {
            Runner.getInstance().addExoticSkill(selected.name);
        }
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

    public ArrayList<String> getGunTypes() {
        ArrayList<String> rv = new ArrayList<>();
        for(Gun gun : guns) {
            if(!rv.contains(gun.getType())) {
                rv.add(gun.getType());
            }
        }
        return rv;
    }

    public ArrayList<Ammo.Template> getFreeAmmoTemplates(String gunType) {
        ArrayList<Ammo.Template> rv = new ArrayList<>();
        boolean foundTemplate;
        for(Ammo.Template template : ammoTemplates) {
            foundTemplate = false;
            for(Ammo a : ammo) {
                if(a.getName().compareTo(template.name) == 0 && a.gunType.compareTo(gunType) == 0) {
                    foundTemplate = true;
                    break;
                }
            }
            if(!foundTemplate) {
                rv.add(template);
            }
        }
        return rv;
    }

    public String[] getAmmoNames(String gunType) {
        ArrayList<String> arr = new ArrayList<>();
        for (Ammo a : ammo) {
            if (a.gunType.compareTo(gunType) == 0) {
                arr.add(a.getName());
            }
        }
        String[] rv = new String[arr.size()];
        arr.toArray(rv);
        return rv;
    }

    public void save(File dir) {
        try {
            PrintWriter outFile = new PrintWriter(new File(dir, "saveFile.txt"));
            outFile.println(Runner.getInstance().save());
            for(Ammo a : ammo) {
                outFile.println(a.save());
            }
            outFile.println(fileSep);
            for(Gun gun : guns) {
                outFile.println(gun.save());
            }
            outFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace(System.out);
        }
    }

    public void load(File dir) {
        ammo.clear();
        guns.clear();
        try {
            BufferedReader inFile = new BufferedReader(new FileReader(new File(dir, "saveFile.txt")));
            Runner.loadRunner(inFile.readLine());
            String line = inFile.readLine();
            while(line != null && line.compareTo(fileSep) != 0) {
                ammo.add(Ammo.load(line));
                line = inFile.readLine();
            }
            line = inFile.readLine();
            while(line != null) {
                guns.add(Gun.load(line));
                line = inFile.readLine();
            }
            inFile.close();
        } catch (IOException e) {
            guns.clear();
            ammo.clear();
            e.printStackTrace(System.out);
        }

    }

    public Gun.Template getGunTemplate(String name) {
        for(Gun.Template template : gunTemplates) {
            if(template.name.compareTo(name) == 0) {
                return template;
            }
        }
        return null;
    }
}
