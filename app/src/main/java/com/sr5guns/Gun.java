package com.sr5guns;

import java.util.ArrayList;

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
        } else {
            return "Other";
        }
    }

    private final Template template;
    private final GunAccessory[] mountedAccessories;  //top barrel underbarrel
    private final ArrayList<GunAccessory> otherAccessories;

    public Gun(Template template) {
        this.template = template;
        mountedAccessories = new GunAccessory[3];
        System.arraycopy(template.mountedAccessories, 0, mountedAccessories, 0, 3);
        otherAccessories = new ArrayList<>();
        for(GunAccessory accessory : template.otherAccessories) {
            otherAccessories.add(accessory);
        }
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
            rv += " (" +template.damageSubtype.charAt(0) +")";
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
        if(mount == 0b100) {
            mountedAccessories[0] = new GunAccessory(accessoryTemplate, false);
        } else if(mount == 0b010) {
            mountedAccessories[1] = new GunAccessory(accessoryTemplate, false);
        } else if(mount == 0b001) {
            mountedAccessories[2] = new GunAccessory(accessoryTemplate, false);
        } else if(mount == 0) {
            otherAccessories.add(new GunAccessory(accessoryTemplate, false));
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
