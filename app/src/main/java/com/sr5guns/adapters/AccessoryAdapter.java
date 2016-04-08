package com.sr5guns.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.sr5guns.items.Gun;
import com.sr5guns.items.GunAccessory;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class AccessoryAdapter extends BaseAdapter implements ListAdapter {

    private final Gun gun;
    private final Context context;

    public AccessoryAdapter(Context context, Gun gun) {
        super();
        this.gun = gun;
        this.context = context;
    }

    @Override
    public int getCount() {
        return gun.getOtherAccessories().size()+1;
    }

    @Override
    public GunAccessory getItem(int position) {
        if(position < gun.getOtherAccessories().size()) {
            return gun.getOtherAccessories().get(position);
        } else {
            return GunAccessory.getAdd();
        }
    }

    public boolean isAdd(int position) {
        return position == gun.getOtherAccessories().size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(android.R.layout.simple_list_item_1, null);
        }

        TextView tv = (TextView)view.findViewById(android.R.id.text1);
        tv.setText(getItem(position).toString());

        return view;
    }
}
