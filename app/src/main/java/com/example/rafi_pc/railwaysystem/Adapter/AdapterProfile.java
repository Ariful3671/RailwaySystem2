package com.example.rafi_pc.railwaysystem.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rafi_pc.railwaysystem.R;

/**
 * Created by Arif on 25-04-18.
 */

public class AdapterProfile extends BaseAdapter {

    String option[];
    String value[];
    int icon[];
    int right_arrow[];
    Context context;
    LayoutInflater inflater;

    public AdapterProfile(String[] option, String[] value, int[] icon, int[] right_arrow, Context context) {
        this.option = option;
        this.value = value;
        this.icon = icon;
        this.right_arrow = right_arrow;
        this.context = context;
    }

    @Override
    public int getCount() {
        return option.length;
    }

    @Override
    public Object getItem(int position) {
        return option[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        View listView = view;
        if (view==null)
        {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            listView = inflater.inflate(R.layout.list_view_profile_item, null);

            SharedPreferences sharedPreferences = context.getSharedPreferences("profileinfo", Context.MODE_PRIVATE);

            TextView text_view_option = (TextView) listView.findViewById(R.id.text_view_option);
            TextView text_view_value = (TextView) listView.findViewById(R.id.text_view_value);

            if (position == 0 && !sharedPreferences.getString("name", "").equals("")) {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("name", "").toString());
            } else if (position == 1 && !sharedPreferences.getString("phone", "").equals("")) {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("phone", "").toString());
            } else if (position == 2 && !sharedPreferences.getString("birthday", "").equals("")) {
                text_view_option.setText(option[position]);
                String birthday = sharedPreferences.getString("birthday", "").toString();
                text_view_value.setText(birthday);
            } else if (position == 3 && !sharedPreferences.getString("age", "").equals("")) {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("age", "").toString());
            }else if (position == 4 && !sharedPreferences.getString("gender", "").equals("")) {
                text_view_option.setText(option[position]);
                text_view_value.setText(sharedPreferences.getString("gender", "").toString());
            }else{
                text_view_option.setText(option[position]);
                text_view_value.setText(value[position]);
            }
            text_view_option.setCompoundDrawablesWithIntrinsicBounds(icon[position], 0, 0, 0);
            text_view_value.setCompoundDrawablesWithIntrinsicBounds(0, 0, right_arrow[0], 0);
        }
        return listView;
    }
}

