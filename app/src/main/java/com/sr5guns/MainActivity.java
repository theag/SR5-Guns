package com.sr5guns;

import android.app.AlertDialog;
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

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements GunFiringFragment.OnFragmentInteractionListener,
        GunDetailsFragment.OnFragmentInteractionListener, NewGunDialog.OnClickListener, AdapterView.OnItemSelectedListener,
        AddAccessoryDialog.OnClickListener, ReplaceAccessoryDialog.OnClickListener, ClipFragment.OnFragmentInteractionListener {

    private static final String DIALOG_NEW_GUN_1 = "new gun dialog 1";
    private static final String DIALOG_NEW_GUN_2 = "new gun dialog 2";
    public static final String DIALOG_TEXT_POPUP = "text popup dialog";
    public static final String ARG_VIEW_ID = "arg view id";
    public static final String DIALOG_ADD_ACCESSORY = "add accessory dialog";
    private static final String DIALOG_REPLACE_ACCESSORY = "repalce accessory dialog";
    public static final String ARG_MOUNT = "mount";
    public static final String ARG_OTHER_ACCESSORY_INDEX = "other accessory index";

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
        if(gunArray.guns.isEmpty()) {
            gunArray.addGun(0);
        }

        gunSpinner = (Spinner)findViewById(R.id.spinner_guns);
        gunArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gunArray.guns);
        gunArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gunSpinner.setAdapter(gunArrayAdapter);
        gunSpinner.setOnItemSelectedListener(this);

        tabsPagerAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(tabsPagerAdapter);
        viewPager.setCurrentItem(0);

        PagerTabStrip pagerTabStrip = (PagerTabStrip)findViewById(R.id.pagerTabStrip);
        pagerTabStrip.setTabIndicatorColorResource(R.color.colorAccent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Arrays.getInstance().save(getFilesDir());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_new:
                NewGunDialog frag = new NewGunDialog();
                frag.show(getSupportFragmentManager(), DIALOG_NEW_GUN_1);
                return true;
            case R.id.action_ammo:
                Intent intent = new Intent(this, AmmoActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
                        args.putString(TextPopupDialog.ARG_MESSAGE, Arrays.getInstance().guns.get(gunSpinner.getSelectedItemPosition()).getDamageLong());
                        break;
                    case R.id.text_accuracy:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.accuracy);
                        args.putString(TextPopupDialog.ARG_MESSAGE, Arrays.getInstance().guns.get(gunSpinner.getSelectedItemPosition()).getAccuracyLong());
                        break;
                    case R.id.text_mode:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.mode);
                        args.putString(TextPopupDialog.ARG_MESSAGE, Arrays.getInstance().guns.get(gunSpinner.getSelectedItemPosition()).getModesLong());
                        break;
                    case R.id.text_ammo:
                        args.putInt(TextPopupDialog.ARG_TITLE, R.string.ammo);
                        args.putString(TextPopupDialog.ARG_MESSAGE, Arrays.getInstance().guns.get(gunSpinner.getSelectedItemPosition()).getAmmoLong());
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
        Gun gun = (Gun)gunSpinner.getSelectedItem();
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
