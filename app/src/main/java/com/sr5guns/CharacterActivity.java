package com.sr5guns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class CharacterActivity extends AppCompatActivity {

    private ExoticListAdapter exoticListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character);

        Runner runner = Runner.getInstance();

        CheckBox chk = (CheckBox)findViewById(R.id.chk_smart_link_aug);
        chk.setChecked(runner.smartLinkWithAug);
        EditText et = (EditText)findViewById(R.id.edit_agility);
        et.setText(""+runner.agility);
        et = (EditText)findViewById(R.id.edit_strength);
        et.setText(""+runner.strength);
        et = (EditText)findViewById(R.id.edit_automatics);
        et.setText(""+runner.automatics);
        et = (EditText)findViewById(R.id.edit_longarms);
        et.setText(""+runner.longarms);
        et = (EditText)findViewById(R.id.edit_pistols);
        et.setText(""+runner.pistols);
        exoticListAdapter = new ExoticListAdapter(this);
        ListView lv = (ListView)findViewById(R.id.list_exotics);
        lv.setAdapter(exoticListAdapter);
    }

    public void saveRunner(View view) {
        Runner runner = Runner.getInstance();

        CheckBox chk = (CheckBox)findViewById(R.id.chk_smart_link_aug);
        runner.smartLinkWithAug = chk.isChecked();
        EditText et = (EditText)findViewById(R.id.edit_agility);
        runner.agility = Integer.parseInt(et.getText().toString());
        et = (EditText)findViewById(R.id.edit_strength);
        runner.strength = Integer.parseInt(et.getText().toString());
        et = (EditText)findViewById(R.id.edit_automatics);
        runner.automatics = Integer.parseInt(et.getText().toString());
        et = (EditText)findViewById(R.id.edit_longarms);
        runner.longarms = Integer.parseInt(et.getText().toString());
        et = (EditText)findViewById(R.id.edit_pistols);
        runner.pistols = Integer.parseInt(et.getText().toString());
        exoticListAdapter.save();
        finish();
    }

}
