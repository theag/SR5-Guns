package com.sr5guns.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.sr5guns.R;
import com.sr5guns.items.Ammo;
import com.sr5guns.items.Arrays;

/**
 * Created by nbp184 on 2016/04/01.
 */
public class AmmoListAdapter extends BaseAdapter implements ListAdapter {

    private Arrays arrays;
    private Context context;

    public AmmoListAdapter(Context context) {
        arrays = Arrays.getInstance();
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrays.ammo.size();
    }

    @Override
    public Ammo getItem(int position) {
        return arrays.ammo.get(position);
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
            view = inflater.inflate(R.layout.list_item_ammo, null);
        }

        Ammo ammo = getItem(position);

        TextView tv = (TextView)view.findViewById(R.id.text);
        tv.setText(ammo.listDisplay());
        tv = (TextView)view.findViewById(R.id.text_position);
        tv.setText(""+position);

        ImageButton btn = (ImageButton)view.findViewById(R.id.btn_remove);
        if(arrays.canRemoveAmmo(ammo)) {
            btn.setEnabled(true);
        } else {
            btn.setEnabled(false);
        }

        return view;
    }
}
