package com.example.matth.finalapp.fragments.personnel;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.matth.finalapp.MenuActivity;
import com.example.matth.finalapp.R;
import com.example.matth.finalapp.objects.Personnel;

import java.util.ArrayList;
import java.util.List;


/**
 * A fragment with a Google +1 button.
 */
public class WaitersFragment extends Fragment implements View.OnClickListener {

    ListView listView;
    List<Personnel> personnelList;

    public WaitersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_waiters, container, false);
        setHasOptionsMenu(true);
        personnelList = new ArrayList<>();
        Personnel person = new Personnel("qqsd","qsdqs");
        person.setName("Michael");
        person.setJob_description("super ober");
        personnelList.add(person);

        System.out.println("the jo is " + person.getJob_description());

        ((MenuActivity) getActivity()).getSupportActionBar().setTitle("Personnel");

        listView = (ListView) rootView.findViewById(R.id.waiter_list);
        rootView.findViewById(R.id.floating_add_personnel_button).setOnClickListener(this);

        PersonnelAdapter adapter = new PersonnelAdapter(getContext(),personnelList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String itemValue = personnelList.get(position).getName();
                ((MenuActivity) getActivity()).changeToWaiterDetailFragment(itemValue);
            }
        });

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floating_add_personnel_button:
                ((MenuActivity) getActivity()).switchToFragmentSaveBack(new AddPersonnelFragment());

        }
    }

    private void getPersonnelList(){


    }

}
