package com.example.matth.finalapp.fragments.personnel;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Personnel;
import com.google.android.gms.plus.model.people.Person;

import java.util.List;

/**
 * Created by michael on 23/10/2016.
 */

public class PersonnelAdapter extends BaseAdapter {

    Context context;
    List<Personnel> personnelList;

    public PersonnelAdapter(Context context,List<Personnel> personnelList) {
        this.personnelList = personnelList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return personnelList.size();
    }

    @Override
    public Object getItem(int position) {
        return personnelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return personnelList.indexOf(getItem(position));
    }


    private class ViewHolder{
        ImageView profile_img;
        TextView  name;
        TextView job_description;
        //TextView status;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_personnel, null);
            holder = new ViewHolder();

            holder.name = (TextView) convertView
                    .findViewById(R.id.personnel_list_name);
            holder.profile_img = (ImageView) convertView
                    .findViewById(R.id.personnel_list_profile_pic);
           // holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.job_description = (TextView) convertView
                    .findViewById(R.id.personnel_list_job_description);

            Personnel row_pos = personnelList.get(position);

            //holder.profile_img.setImageResource(row_pos.getProfile_pic_id());
            holder.name.setText(row_pos.getName());
            //holder.status.setText(row_pos.getStatus());
            holder.job_description.setText(row_pos.getJob_description());
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }
}
