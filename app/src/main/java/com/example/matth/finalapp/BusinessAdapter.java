package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matth.finalapp.objects.Business;

/**
 * Created by matth on 24/10/2016.
 */

public class BusinessAdapter extends ArrayAdapter<Business> {
    Context context;
    Business[] businesses;
    int selected = -1;

    public BusinessAdapter(Context context, Business[] resource) {
        super(context, R.layout.list_business, resource);
        this.context = context;
        this.businesses = resource;
    }

    @Override
    public View getView(final int posi, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_business, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        CheckBox cb = (CheckBox) convertView.findViewById(R.id.checkBox1);
        final int position = posi;
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //boolean isChecked = cb.isChecked();
                //Do something here.
                Toast toast = Toast.makeText(context, "you pressed a checkbox", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        //cb.setChecked(position==selected);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                notifyDataSetChanged();
            }
        });
/*
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    selected =  position;
                }
                else{
                    selected = -1;
                }
                notifyDataSetChanged();
            }
        });*/
        name.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, "you pressed a textview", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
        name.setText(businesses[position].getName());
        if (businesses[position].getActive() == true) {
            cb.setChecked(true);
        } else {
            cb.setChecked(false);
        }
        return convertView;
    }
}
