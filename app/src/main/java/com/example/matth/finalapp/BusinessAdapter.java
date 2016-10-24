package com.example.matth.finalapp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matth.finalapp.objects.Business;

/**
 * Created by matth on 24/10/2016.
 */

public class BusinessAdapter extends ArrayAdapter<Business> {
    Context context;
    Business[] businesses;

    public BusinessAdapter(Context context, Business[] resource) {
        super(context, R.layout.list_business, resource);
        this.context = context;
        this.businesses = resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        convertView = inflater.inflate(R.layout.list_business, parent, false);
        TextView name = (TextView) convertView.findViewById(R.id.textView1);
        ImageView button = (ImageView) convertView.findViewById(R.id.business_edit);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast toast = Toast.makeText(context, "you pressed it!", Toast.LENGTH_SHORT);
                toast.show();
                ((MenuActivity) context).changeToBusinessDetailFragment(businesses[position]);
            }
        });
        name.setText(businesses[position].getName());

        return convertView;
    }
}
