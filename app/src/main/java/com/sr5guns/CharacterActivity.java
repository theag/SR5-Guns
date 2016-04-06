package com.sr5guns;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CharacterActivity extends AppCompatActivity {

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
        TableLayout tl = (TableLayout)findViewById(R.id.table_exotics);
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view;
        TextView tv;
        for(int i = 0; i < runner.getExoticSkillCount(); i++) {
            view = inflater.inflate(R.layout.table_row_exotic_ranged_weapon, null);
            tv = (TextView)view.findViewById(R.id.text_name);
            tv.setText(runner.getExoticSkill(i).name);

            et = (EditText)view.findViewById(R.id.edit_value);
            et.setText(""+runner.getExoticSkill(i).value);
            tl.addView(view);
        }
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
        TableLayout table = (TableLayout)findViewById(R.id.table_exotics);
        TableRow tr;
        for(int i = 0; i < table.getChildCount(); i++) {
            tr = (TableRow)table.getChildAt(i);
            et = (EditText)tr.findViewById(R.id.edit_value);
            runner.getExoticSkill(i).value = Integer.parseInt(et.getText().toString());
        }
        finish();
    }

}
