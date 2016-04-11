package com.sr5guns.items;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by nbp184 on 2016/03/30.
 */
public class Gun {

    public static String getMountName(byte mount) {
        if(mount == 0b100) {
            return "Top";
        } else if(mount == 0b010) {
            return "Barrel";
        } else if(mount == 0b001) {
            return "Under-barrel";
        } else if(mount == 0b1000) {
            return "Not Mounted";
        } else {
            return "Other";
        }
    }

    public static Gun load(String input) {
        StringTokenizer tokens = new StringTokenizer(input, Arrays.recordSep);
        Gun rv = new Gun(Arrays.getInstance().getGunTemplate(tokens.nextToken()), Integer.parseInt(tokens.nextToken()));
        String line;
        for(int i = 0; i < rv.mountedAccessories.length; i++) {
            line = tokens.nextToken();
            if(line.compareTo(Arrays.nullChar) != 0) {
                rv.mountedAccessories[i] = GunAccessory.load(line, Arrays.unitSep);
            }
        }
        while(tokens.hasMoreTokens()) {
            line = tokens.nextToken();
            if(line.compareTo(Arrays.groupSep) == 0) {
                break;
            }
            rv.otherAccessories.add(GunAccessory.load(line, Arrays.unitSep));
        }
        while(tokens.hasMoreTokens()) {
            rv.clips.add(Clip.load(tokens.nextToken(), Arrays.unitSep, rv.template.type));
        }
        rv.clips.get(rv.currentClip).isCurrent = true;
        return rv;
    }

    private final Template template;
    private final GunAccessory[] mountedAccessories;  //top barrel underbarrel
    private final ArrayList<GunAccessory> otherAccessories;
    public final ArrayList<Clip> clips;
    private int currentClip;

    public Gun(Template template) {
        this.template = template;
        mountedAccessories = new GunAccessory[3];
        System.arraycopy(template.mountedAccessories, 0, mountedAccessories, 0, 3);
        otherAccessories = new ArrayList<>();
        for(GunAccessory accessory : template.otherAccessories) {
            otherAccessories.add(accessory);
        }
        clips = new ArrayList<>();
        Arrays arrays = Arrays.getInstance();
        Ammo ammo = arrays.getAmmo(template.type, "Regular");
        if (ammo == null) {
            ammo = new Ammo(arrays.getAmmoTemplate("Regular"), template.type, template.ammoCount);
            arrays.ammo.add(ammo);
        } else {
            ammo.count += template.ammoCount;
        }
        Clip clip = new Clip(template.ammoContainer, template.ammoCount, ammo);
        clip.reload();
        clips.add(clip);
        currentClip = 0;
        clip.isCurrent = true;
    }

    private Gun(Template template, int currentClip) {
        this.template = template;
        mountedAccessories = new GunAccessory[3];
        otherAccessories = new ArrayList<>();
        clips = new ArrayList<>();
        this.currentClip = currentClip;
    }

    @Override
    public String toString() {
        return template.toString();
    }

    public String getName() {
        return template.name;
    }

    public String getDamageShort() {
        String rv = "" +template.damage +template.damageType.charAt(0);
        if(template.damageSubtype != null) {
            rv += "(" +template.damageSubtype.charAt(0) +")";
        }
        return rv;
    }

    public String getAccuracyShort() {
        String rv = ""+template.accuracy;
        int a = template.accuracy;
        int al = 0;
        int asg = 0;
        int aw = template.accuracy;
        for(GunAccessory accessory : mountedAccessories) {
            if(accessory != null) {
                switch(accessory.getName()) {
                    case "Laser sight":
                        al = accessory.getBonus("accuracy");
                        break;
                    case "Smart gun":
                        asg = accessory.getBonus("accuracy");
                        break;
                    default:
                        a += accessory.getBonus("accuracy");
                        aw += accessory.getWirelessBonus("accuracy");
                        break;
                }
            }
        }
        for(GunAccessory accessory : otherAccessories) {
            if(accessory != null) {
                switch(accessory.getName()) {
                    case "Smart gun":
                        asg = accessory.getBonus("accuracy");
                        break;
                    default:
                        a += accessory.getBonus("accuracy");
                        aw += accessory.getWirelessBonus("accuracy");
                        break;
                }
            }
        }
        if(al != 0) {
            rv += " (" +(a + al) +")";
        }
        if(asg != 0) {
            rv += " (" +(a + asg) +")";
        }
        if(al == 0 && asg == 0 && a != template.accuracy) {
            rv += " (" +a +")";
        }
        if(aw != template.accuracy) {
            rv += "/(" +aw +")";
        }
        return rv;
    }

    public String getAPString() {
        return ""+template.ap;
    }

    public String getModesShort() {
        String rv = "";
        if((template.modes & 8) > 0) {
            if(!rv.isEmpty()) {
                rv += "/";
            }
            rv += "SS";
        }
        if((template.modes & 4) > 0) {
            if(!rv.isEmpty()) {
                rv += "/";
            }
            rv += "SA";
        }
        if((template.modes & 2) > 0) {
            if(!rv.isEmpty()) {
                rv += "/";
            }
            rv += "BF";
        }
        if((template.modes & 1) > 0) {
            if(!rv.isEmpty()) {
                rv += "/";
            }
            rv += "FA";
        }
        return rv;
    }

    public String getRecoil() {
        return "" +template.recoil;
    }

    public String getAmmoShort() {
        return template.ammoCount +"(" +template.ammoContainer.charAt(0) +")";
    }

    public String getDamageLong() {
        String rv = template.damage +" " +template.damageType;
        if(template.damageSubtype != null) {
            rv += " (" +template.damageSubtype +")";
        }
        return rv;
    }

    public String getAccuracyLong() {
        String rv = ""+template.accuracy;
        int a = template.accuracy;
        String as = "";
        int al = 0;
        int asg = 0;
        int aw = template.accuracy;
        String aws = "";
        int b;
        for(GunAccessory accessory : mountedAccessories) {
            if(accessory != null) {
                switch(accessory.getName()) {
                    case "Laser sight":
                        al = accessory.getBonus("accuracy");
                        break;
                    case "Smart gun":
                        asg = accessory.getBonus("accuracy");
                        break;
                    default:
                        b = accessory.getBonus("accuracy");
                        if(b != 0) {
                            a += b;
                            as += ", " +accessory.getName().toLowerCase();
                        }
                        b = accessory.getWirelessBonus("accuracy");
                        if(b != 0) {
                            aw += b;
                            aws += ", " +accessory.getName().toLowerCase();
                        }
                        break;
                }
            }
        }
        for(GunAccessory accessory : otherAccessories) {
            if(accessory != null) {
                switch(accessory.getName()) {
                    case "Smart gun":
                        asg = accessory.getBonus("accuracy");
                        break;
                    default:
                        b = accessory.getBonus("accuracy");
                        if(b != 0) {
                            a += b;
                            as += ", " +accessory.getName().toLowerCase();
                        }
                        b = accessory.getWirelessBonus("accuracy");
                        if(b != 0) {
                            aw += b;
                            aws += ", " +accessory.getName().toLowerCase();
                        }
                        break;
                }
            }
        }
        if(al != 0) {
            rv += "\n" +(a +al) +" with laser sight" +as;
        }
        if(asg != 0) {
            rv += "\n" +(a +asg) +" with smart gun" +as;
        }
        if(al == 0 && asg == 0 && a != template.accuracy) {
           rv += "\n" +a +" with " +as.substring(2);
        }
        if(aw != template.accuracy) {
            rv += "\n" +a +" with wireless " +as.substring(2);
        }
        return rv;
    }

    public String getModesLong() {
        String rv = "";
        if((template.modes & 8) > 0) {
            if(!rv.isEmpty()) {
                rv += "\n";
            }
            rv += "Single Shot";
        }
        if((template.modes & 4) > 0) {
            if(!rv.isEmpty()) {
                rv += "\n";
            }
            rv += "Semi-Automatic";
        }
        if((template.modes & 2) > 0) {
            if(!rv.isEmpty()) {
                rv += "\n";
            }
            rv += "Burst Fire";
        }
        if((template.modes & 1) > 0) {
            if(!rv.isEmpty()) {
                rv += "\n";
            }
            rv += "Fully Automatic";
        }
        return rv;
    }

    public String getAmmoLong() {
        return template.ammoCount +" in a " +template.ammoContainer;
    }

    public boolean canHaveTopMount() {
        return (template.mounts & 0b100) == 0b100;
    }

    public String getTopMountString() {
        if(mountedAccessories[0] != null) {
            return mountedAccessories[0].getName();
        } else {
            return "Empty";
        }
    }

    public boolean isTopMountPermanent() {
        if(mountedAccessories[0] != null) {
            return mountedAccessories[0].permanent;
        } else {
            return false;
        }
    }

    public boolean canHaveBarrelMount() {
        return (template.mounts & 0b010) == 0b010;
    }

    public String getBarrelMountString() {
        if(mountedAccessories[1] != null) {
            return mountedAccessories[1].getName();
        } else {
            return "Empty";
        }
    }

    public boolean isBarrelMountPermanent() {
        if(mountedAccessories[1] != null) {
            return mountedAccessories[1].permanent;
        } else {
            return false;
        }
    }

    public boolean canHaveUnderMount() {
        return (template.mounts & 0b001) == 0b001;
    }

    public String getUnderMountString() {
        if(mountedAccessories[2] != null) {
            return mountedAccessories[2].getName();
        } else {
            return "Empty";
        }
    }

    public boolean isUnderMountPermanent() {
        if(mountedAccessories[2] != null) {
            return mountedAccessories[2].permanent;
        } else {
            return false;
        }
    }

    public boolean canMountOn(byte mount) {
        boolean rv = false;
        if((mount & template.mounts) > 0) {
            rv = true;
            if((mount & 0b100) > 0 && mountedAccessories[0] != null && mountedAccessories[0].permanent) {
                rv = false;
            }
            if((mount & 0b010) > 0 && mountedAccessories[1] != null && mountedAccessories[1].permanent) {
                rv = false;
            }
            if((mount & 0b001) > 0 && mountedAccessories[2] != null && mountedAccessories[2].permanent) {
                rv = false;
            }
        }
        return rv;
    }

    public boolean isMountEmpty(byte mount) {
        if(mount == 0b100) {
            return mountedAccessories[0] == null;
        } else if(mount == 0b010) {
            return mountedAccessories[1] == null;
        } else if(mount == 0b001) {
            return mountedAccessories[2] == null;
        } else {
            return true;
        }
    }

    public void addAccessory(byte mount, GunAccessory.Template accessoryTemplate) {
        GunAccessory ga = null;
        if(accessoryTemplate != null) {
            ga = new GunAccessory(accessoryTemplate, false);
        }
        if(mount == 0b100) {
            mountedAccessories[0] = ga;
        } else if(mount == 0b010) {
            mountedAccessories[1] = ga;
        } else if(mount == 0b001) {
            mountedAccessories[2] = ga;
        } else if(mount == 0b1000 && ga != null) {
            otherAccessories.add(ga);
        }
    }

    public String getMountString(byte mount) {
        if(mount == 0b100) {
            return mountedAccessories[0].getName();
        } else if(mount == 0b010) {
            return mountedAccessories[1].getName();
        } else if(mount == 0b001) {
            return mountedAccessories[2].getName();
        } else {
            return null;
        }
    }

    public ArrayList<GunAccessory> getOtherAccessories() {
        return otherAccessories;
    }

    public String getOtherMountString(int index) {
        return otherAccessories.get(index).getName();
    }

    public void replaceOtherAccessory(int index, GunAccessory.Template accessoryTemplate) {
        if(accessoryTemplate == null) {
            otherAccessories.remove(index);
        } else {
            otherAccessories.set(index, new GunAccessory(accessoryTemplate, false));
        }
    }

    public String getType() {
        return template.type;
    }

    public boolean isCurrent(Clip clip) {
        return clips.get(currentClip) == clip;
    }

    public int getAmmoCount() {
        return template.ammoCount;
    }

    public void setCurrent(Clip clip) {
        clips.get(currentClip).isCurrent = false;
        currentClip = clips.indexOf(clip);
        clip.isCurrent = true;
    }

    public String getClipType() {
        return template.ammoContainer;
    }

    public String save() {
        String rv = template.name + Arrays.recordSep +currentClip;
        for(GunAccessory accessory : mountedAccessories) {
            rv += Arrays.recordSep;
            if(accessory == null) {
                rv += Arrays.nullChar;
            } else {
                rv += accessory.save(Arrays.unitSep);
            }
        }
        for(GunAccessory accessory : otherAccessories) {
            rv += Arrays.recordSep +accessory.save(Arrays.unitSep);
        }
        rv += Arrays.recordSep + Arrays.groupSep;
        for(Clip clip : clips) {
            rv += Arrays.recordSep +clip.save(Arrays.unitSep);
        }
        return rv;
    }

    public String[] getFiringModes() {
        int count = 0;
        if((template.modes & 0b1000) > 0) {
            count += 1;
        }
        if((template.modes & 0b0100) > 0) {
            count += 2;
        }
        if((template.modes & 0b0010) > 0) {
            count += 2;
        }
        if((template.modes & 0b0001) > 0) {
            count += 3;
        }
        String[] rv = new String[count];
        count = 0;
        if((template.modes & 0b1000) > 0) {
            rv[count++] = "Single Shot";
        }
        if((template.modes & 0b0100) > 0) {
            rv[count++] = "Semi Auto";
            rv[count++] = "Semi Auto Burst";
        }
        if((template.modes & 0b0010) > 0) {
            rv[count++] = "Burst Fire";
            rv[count++] = "Long Burst";
        }
        if((template.modes & 0b0001) > 0) {
            rv[count++] = "Full Auto (Simp)";
            rv[count++] = "Full Auto (Cmpx)";
            rv[count++] = "Surpressive Fire";
        }
        return rv;
    }

    public int getAccessoryBonuses(String stat, boolean smartLinkOverLaser, boolean wireless, boolean smartLinkWithAug) {
        int bonus = 0;
        int smartlink = 0;
        int laser = 0;
        for(GunAccessory accessory : mountedAccessories) {
            if(accessory != null) {
                switch (accessory.getName()) {
                    case "Laser sight":
                        laser += accessory.getBonus(stat);
                        if (wireless) {
                            laser += accessory.getWirelessBonus(stat);
                        }
                        break;
                    case "Smart gun":
                        smartlink += accessory.getBonus(stat);
                        if (wireless) {
                            if(smartLinkWithAug) {
                                smartlink += 2*accessory.getWirelessBonus(stat);
                            } else {
                                smartlink += accessory.getWirelessBonus(stat);
                            }
                        }
                        break;
                    default:
                        bonus += accessory.getBonus(stat);
                        if (wireless) {
                            bonus += accessory.getWirelessBonus(stat);
                        }
                }
            }
        }
        for(GunAccessory accessory : otherAccessories) {
            switch (accessory.getName()) {
                case "Laser sight":
                    laser += accessory.getBonus(stat);
                    if (wireless) {
                        laser += accessory.getWirelessBonus(stat);
                    }
                    break;
                case "Smart gun":
                    smartlink += accessory.getBonus(stat);
                    if (wireless) {
                        if(smartLinkWithAug) {
                            smartlink += 2*accessory.getWirelessBonus(stat);
                        } else {
                            smartlink += accessory.getWirelessBonus(stat);
                        }
                    }
                    break;
                default:
                    bonus += accessory.getBonus(stat);
                    if (wireless) {
                        bonus += accessory.getWirelessBonus(stat);
                    }
            }
        }
        if(laser > 0 && smartlink > 0) {
            if(smartLinkOverLaser) {
                bonus += smartlink;
            } else {
                bonus += laser;
            }
        } else if(laser > 0) {
            bonus += laser;
        } else if(smartlink > 0) {
            bonus += smartlink;
        }
        return bonus;
    }

    public int getAccuracy() {
        return template.accuracy;
    }

    public int getAP() {
        return template.ap;
    }

    public int getDamageInt() {
        return template.damage;
    }

    public String getDamageType() {
        return template.damageType;
    }

    public String getDamageSubtype() {
        return template.damageSubtype;
    }

    public int getRecoilInt() {
        return template.recoil;
    }

    public static final class Template {
        public final String name;
        public final String type;
        public final int accuracy;
        public final int damage;
        public final String damageType;
        public final String damageSubtype;
        public final int ap;
        public final byte modes; //SS SA BF FA
        public final int recoil;
        public final int ammoCount;
        public final String ammoContainer;
        public final byte mounts; //top barrel underbarrel
        public final GunAccessory[] mountedAccessories;
        public final GunAccessory[] otherAccessories;

        public Template(String name, String type, int accuracy, int damage, String damageType, String damageSubtype, int ap, byte modes, int recoil, int ammoCount, String ammoContainer, byte mounts, int otherAccessoryCount) {
            this.name = name;
            this.type = type;
            this.accuracy = accuracy;
            this.damage = damage;
            this.damageType = damageType;
            this.damageSubtype = damageSubtype;
            this.ap = ap;
            this.modes = modes;
            this.recoil = recoil;
            this.ammoCount = ammoCount;
            this.ammoContainer = ammoContainer;
            this.mounts = mounts;
            mountedAccessories = new GunAccessory[3];
            otherAccessories = new GunAccessory[otherAccessoryCount];
        }

        @Override
        public String toString() {
            return name +" (" +type +")";
        }

        public void addAccessory(byte mount, GunAccessory.Template template, boolean permanent) {
            switch(mount) {
                case 8:
                    mountedAccessories[0] = new GunAccessory(template, permanent);
                    break;
                case 4:
                    mountedAccessories[1] = new GunAccessory(template, permanent);
                    break;
                case 2:
                    mountedAccessories[2] = new GunAccessory(template, permanent);
                    break;
            }
        }

        public void addAccessory(GunAccessory.Template template, boolean permanent, int count) {
            otherAccessories[count] = new GunAccessory(template, permanent);
        }
    }
}
