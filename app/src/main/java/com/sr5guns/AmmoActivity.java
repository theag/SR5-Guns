package com.sr5guns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class AmmoActivity extends AppCompatActivity {

    AmmoListAdapter ammoListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammo);

        ListView lv = (ListView)findViewById(R.id.list_ammo);
        ammoListAdapter = new AmmoListAdapter(this);
        lv.setAdapter(ammoListAdapter);
    }

    public void buttonPress(View view) {
        Arrays arrays = Arrays.getInstance();
        switch(view.getId()) {
            case R.id.btn_add_ammo:
                break;
            case R.id.btn_remove:
                TextView position = (TextView)((View)view.getParent()).findViewById(R.id.text_position);
                arrays.ammo.remove(ammoListAdapter.getItem(Integer.parseInt(position.getText().toString())));
                ammoListAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_buy:
                position = (TextView)((View)view.getParent()).findViewById(R.id.text_position);
                ammoListAdapter.getItem(Integer.parseInt(position.getText().toString())).count += 10;
                ammoListAdapter.notifyDataSetChanged();
                break;
        }
    }

}
