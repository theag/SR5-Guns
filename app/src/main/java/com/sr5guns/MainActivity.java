package com.sr5guns;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sr5guns.adapters.AccessoryAdapter;
import com.sr5guns.adapters.TabsPagerAdapter;
import com.sr5guns.dialogs.AddAccessoryDialog;
import com.sr5guns.dialogs.ChangeAmmoDialog;
import com.sr5guns.dialogs.NewGunDialog;
import com.sr5guns.dialogs.ReplaceAccessoryDialog;
import com.sr5guns.dialogs.TextPopupDialog;
import com.sr5guns.dialogs.WoundPenaltyDialog;
import com.sr5guns.items.Arrays;
import com.sr5guns.items.Clip;
import com.sr5guns.items.Combat;
import com.sr5guns.items.Gun;
import com.sr5guns.views.MountedAccessoriesView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GunFiringFragment.OnFragmentInteractionListener,
        GunDetailsFragment.OnFragmentInteractionListener, NewGunDialog.OnClickListener, AdapterView.OnItemSelectedListener,
        AddAccessoryDialog.OnClickListener, ReplaceAccessoryDialog.OnClickListener, ClipFragment.OnFragmentInteractionListener,
        ChangeAmmoDialog.OnClickListener, WoundPenaltyDialog.OnClickListener {

    public static final String DIALOG_NEW_GUN_1 = "new gun dialog 1";
    public static final String DIALOG_NEW_GUN_2 = "new gun dialog 2";
    public static final String DIALOG_TEXT_POPUP = "text popup dialog";
    public static final String ARG_VIEW_ID = "arg view id";
    public static final String DIALOG_ADD_ACCESSORY = "add accessory dialog";
    public static final String DIALOG_REPLACE_ACCESSORY = "repalce accessory dialog";
    public static final String ARG_MOUNT = "mount";
    public static final String ARG_OTHER_ACCESSORY_INDEX = "other accessory index";
    public static final String DIALOG_CHANGE_AMMO = "change ammo";
    public static final String ARG_CLIP_INDEX = "clip index";
    public static final String DIALOG_FIRST_GUN_1 = "first gun dialog 1";
    public static final String DIALOG_FIRST_GUN_2 = "first gun dialog 2";
    private static final int EDIT_RUNNER_REQUEST = 1;
    public static final String DIALOG_WOUND_PENALTY = "wound penalty dialog";

    TabsPagerAdapter tabsPagerAdapter;
    ViewPager viewPager;
    ArrayAdapter<Gun> gunArrayAdapter;
    Spinner gunSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Arrays gunArray = Arrays.getInstance();
        AssetManager assetManager = getAssets();
        try {
            gunArray.loadAccessoryTemplates(assetManager.open("accessories.txt"));
            gunArray.loadGunTemplates(assetManager.open("guns.txt"));
            gunArray.loadAmmoTemplates(assetManager.open("ammo.txt"));
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
        gunArray.load(getFilesDir());
        int gunIndex = Combat.load(getFilesDir());

        PagerTabStrip pagerTabStrip = (PagerTabStrip) findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColorResource(R.color.colorAccent);

        if(gunArray.guns.isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Warning")
                    .setMessage("You don't have any guns chummer, you'll need to have at least one.")
                    .setPositiveButton("Gimme a gun", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getFirstGun();
                        }
                    })
                    .setNegativeButton("I don't want a gun", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            pacifist();
                        }
                    })
                    .setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            pacifist();
                        }
                    });
            builder.create().show();
        } else {
            init();
            gunSpinner.setSelection(gunIndex);
        }

    }

    public void pacifist() {
        finish();
    }

    private void getFirstGun() {
        NewGunDialog frag = new NewGunDialog();
        frag.show(getSupportFragmentManager(), DIALOG_FIRST_GUN_1);
    }

    private void init() {

        gunSpinner = (Spinner) findViewById(R.id.spinner_guns);
        gunArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Arrays.getInstance().guns);
        gunArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gunSpinner.setAdapter(gunArrayAdapter);
        gunSpinner.setOnItemSelectedListener(this);

        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(tabsPagerAdapter);
        viewPager.setCurrentItem(0);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arrays.getInstance().save(getFilesDir());
        Combat.save(getFilesDir(), tabsPagerAdapter.getGunIndex());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.action_new:
                NewGunDialog frag = new NewGunDialog();
                frag.show(getSupportFragmentManager(), DIALOG_NEW_GUN_1);
                return true;
            case R.id.action_ammo:
                intent = new Intent(this, AmmoActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_character:
                intent = new Intent(this, CharacterActivity.class);
                startActivityForResult(intent, EDIT_RUNNER_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EDIT_RUNNER_REQUEST) {
            if (resultCode == RESULT_OK) {
                tabsPagerAdapter.setGunIndex(gunSpinner.getSelectedItemPosition());
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        DialogFragment frag;
        Bundle args;
        Gun gun = (Gun)gunSpinner.getSelectedItem();
        switch(uri.getLastPathSegment()) {
            case DIALOG_TEXT_POPUP:
                frag = new TextPopupDialog();
                args = new Bundle();
                switch(Integer.parseInt(uri.getQueryParameter(ARG_VIEW_ID))) {
                    case R.id.text_damage:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.damage);
                        args.putString(TextPopupDialog.ARG_MESSAGE, gun.getDamageLong());
                        break;
                    case R.id.text_accuracy:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.accuracy);
                        args.putString(TextPopupDialog.ARG_MESSAGE, gun.getAccuracyLong());
                        break;
                    case R.id.text_mode:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.mode);
                        args.putString(TextPopupDialog.ARG_MESSAGE, gun.getModesLong());
                        break;
                    case R.id.text_ammo:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.ammo);
                        args.putString(TextPopupDialog.ARG_MESSAGE, gun.getAmmoLong());
                        break;
                    case R.id.text_damage_mod:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.damage_modifier);
                        args.putString(TextPopupDialog.ARG_MESSAGE, gun.clips.get(Integer.parseInt(uri.getQueryParameter(ARG_CLIP_INDEX))).getDamageModLong());
                        break;
                }
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), DIALOG_TEXT_POPUP);
                break;
            case DIALOG_ADD_ACCESSORY:
                byte mount = Byte.parseByte(uri.getQueryParameter(ARG_MOUNT));
                if(mount == 0b1000) {
                    int index = Integer.parseInt(uri.getQueryParameter(ARG_OTHER_ACCESSORY_INDEX));
                    if(index >= 0 && gun.getOtherAccessories().get(index).permanent) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Error")
                                .setMessage("You remove or replace that accessory.");
                        builder.create().show();
                    } else {
                        frag = new AddAccessoryDialog();
                        args = new Bundle();
                        args.putByte(AddAccessoryDialog.ARG_MOUNT, mount);
                        args.putInt(AddAccessoryDialog.ARG_OTHER_INDEX, index);
                        frag.setArguments(args);
                        frag.show(getSupportFragmentManager(), DIALOG_ADD_ACCESSORY);
                    }
                }
                else if(gun.canMountOn(mount)) {
                    frag = new AddAccessoryDialog();
                    args = new Bundle();
                    args.putByte(AddAccessoryDialog.ARG_MOUNT, mount);
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), DIALOG_ADD_ACCESSORY);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error")
                            .setMessage("You can't mount there.");
                    builder.create().show();
                }
                break;
            case DIALOG_CHANGE_AMMO:
                frag = new ChangeAmmoDialog();
                args = new Bundle();
                args.putString(ChangeAmmoDialog.ARG_GUN_TYPE, gun.getType());
                args.putInt(ARG_CLIP_INDEX, Integer.parseInt(uri.getQueryParameter(ARG_CLIP_INDEX)));
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), DIALOG_CHANGE_AMMO);
                break;
            case DIALOG_WOUND_PENALTY:
                frag = new WoundPenaltyDialog();
                frag.show(getSupportFragmentManager(), DIALOG_WOUND_PENALTY);
            default:
                System.out.println(uri.getLastPathSegment());
                break;
        }
    }

    @Override
    public void onDialogClick(String tag, Bundle data) {
        Arrays arrays = Arrays.getInstance();
        DialogFragment frag;
        Bundle args;
        Gun gun = null;
        if(gunSpinner != null) {
            gun = (Gun) gunSpinner.getSelectedItem();
        }
        switch(tag) {
            case DIALOG_NEW_GUN_1:
                frag = new NewGunDialog();
                args = new Bundle();
                args.putInt(NewGunDialog.ARG_GUN_TYPE_INDEX, data.getInt(NewGunDialog.ARG_GUN_TYPE_INDEX));
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), DIALOG_NEW_GUN_2);
                break;
            case DIALOG_NEW_GUN_2:
                int index = arrays.addGun(data.getString(NewGunDialog.ARG_GUN_TEMPLATE_NAME));
                gunSpinner.setSelection(index);
                break;
            case DIALOG_FIRST_GUN_1:
                frag = new NewGunDialog();
                args = new Bundle();
                args.putInt(NewGunDialog.ARG_GUN_TYPE_INDEX, data.getInt(NewGunDialog.ARG_GUN_TYPE_INDEX));
                frag.setArguments(args);
                frag.show(getSupportFragmentManager(), DIALOG_FIRST_GUN_2);
                break;
            case DIALOG_FIRST_GUN_2:
                index = arrays.addGun(data.getString(NewGunDialog.ARG_GUN_TEMPLATE_NAME));
                init();
                gunSpinner.setSelection(index);
                break;
            case DIALOG_ADD_ACCESSORY:
                byte mount = data.getByte(AddAccessoryDialog.ARG_MOUNT);
                if(mount == 0b1000) {
                    int otherIndex = data.getInt(AddAccessoryDialog.ARG_OTHER_INDEX);
                    if(otherIndex < 0) {
                        gun.addAccessory(mount, arrays.getAccessoryTemplate(data.getString(AddAccessoryDialog.ARG_NAME)));
                        ListView lv = (ListView)findViewById(R.id.other_accessories);
                        AccessoryAdapter aa = (AccessoryAdapter)lv.getAdapter();
                        aa.notifyDataSetChanged();
                    } else {
                        frag = new ReplaceAccessoryDialog();
                        args = new Bundle();
                        args.putByte(ReplaceAccessoryDialog.ARG_MOUNT, mount);
                        args.putInt(ReplaceAccessoryDialog.ARG_OTHER_INDEX, otherIndex);
                        args.putString(ReplaceAccessoryDialog.ARG_OLD_NAME, gun.getOtherMountString(otherIndex));
                        args.putString(ReplaceAccessoryDialog.ARG_NAME, data.getString(AddAccessoryDialog.ARG_NAME));
                        frag.setArguments(args);
                        frag.show(getSupportFragmentManager(), DIALOG_REPLACE_ACCESSORY);
                    }
                } else if(gun.isMountEmpty(mount)) {
                    gun.addAccessory(mount, arrays.getAccessoryTemplate(data.getString(AddAccessoryDialog.ARG_NAME)));
                    MountedAccessoriesView mav = (MountedAccessoriesView)findViewById(R.id.mounted_accessories);
                    mav.invalidate();
                } else {
                    frag = new ReplaceAccessoryDialog();
                    args = new Bundle();
                    args.putByte(ReplaceAccessoryDialog.ARG_MOUNT, mount);
                    args.putString(ReplaceAccessoryDialog.ARG_OLD_NAME, gun.getMountString(mount));
                    args.putString(ReplaceAccessoryDialog.ARG_NAME, data.getString(AddAccessoryDialog.ARG_NAME));
                    frag.setArguments(args);
                    frag.show(getSupportFragmentManager(), DIALOG_REPLACE_ACCESSORY);
                }
                break;
            case DIALOG_REPLACE_ACCESSORY:
                mount = data.getByte(AddAccessoryDialog.ARG_MOUNT);
                if(mount == 0b1000) {
                    gun.replaceOtherAccessory(data.getInt(ReplaceAccessoryDialog.ARG_OTHER_INDEX), arrays.getAccessoryTemplate(data.getString(AddAccessoryDialog.ARG_NAME)));
                    ListView lv = (ListView)findViewById(R.id.other_accessories);
                    AccessoryAdapter aa = (AccessoryAdapter)lv.getAdapter();
                    aa.notifyDataSetChanged();
                } else {
                    gun.addAccessory(mount, arrays.getAccessoryTemplate(data.getString(AddAccessoryDialog.ARG_NAME)));
                    MountedAccessoriesView mav = (MountedAccessoriesView) findViewById(R.id.mounted_accessories);
                    mav.invalidate();
                }
                break;
            case DIALOG_CHANGE_AMMO:
                Clip clip = gun.clips.get(data.getInt(ARG_CLIP_INDEX));
                clip.returnAmmo();
                clip.setAmmo(arrays.getAmmo(gun.getType(), data.getString(ChangeAmmoDialog.ARG_AMMO_TYPE)));
                TextView tv = (TextView)findViewById(R.id.text_ammo_type);
                tv.setText(clip.getAmmoType());
                tv = (TextView)findViewById(R.id.text_damage_mod);
                tv.setText(clip.getDamageModShort());
                tv = (TextView)findViewById(R.id.text_ap_mod);
                tv.setText(clip.getAPMod());
                tv = (TextView)findViewById(R.id.text_in_clip);
                tv.setText(clip.getAmmoCountString());
                Spinner spinner = (Spinner)findViewById(R.id.spinner_clips);
                ArrayAdapter<Clip> adapter = (ArrayAdapter<Clip>)spinner.getAdapter();
                adapter.notifyDataSetChanged();
                break;
            case DIALOG_WOUND_PENALTY:
                Combat.get().woundPenalty = data.getInt(WoundPenaltyDialog.ARG_WOUND_PENALTY);
                tabsPagerAdapter.setGunIndex(gunSpinner.getSelectedItemPosition());
                break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tabsPagerAdapter.setGunIndex(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
