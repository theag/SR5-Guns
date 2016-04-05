package com.sr5guns;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by Thea on 2016-04-02.
 */
public class ExoticListAdapter extends BaseAdapter implements ListAdapter {

    private Runner runner;
    private Context context;
    private EditText[] values;

    public ExoticListAdapter(Context context) {
        runner = Runner.getInstance();
        this.context = context;
        values = new EditText[runner.getExoticSkillCount()];
    }

    @Override
    public int getCount() {
        return runner.getExoticSkillCount();
    }

    @Override
    public Runner.Skill getItem(int position) {
        return runner.getExoticSkill(position);
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

        Runner.Skill skill = getItem(position);

        TextView tv = (TextView)view.findViewById(R.id.text_name);
        tv.setText(skill.name);

        EditText et = (EditText)view.findViewById(R.id.edit_value);
        et.setText(""+skill.value);
        values[position] = et;

        return view;
    }

    public void save() {
        for(int i = 0; i < runner.getExoticSkillCount(); i++) {
            runner.getExoticSkill(i).value = Integer.parseInt(values[i].getText().toString());
        }
    }

}
