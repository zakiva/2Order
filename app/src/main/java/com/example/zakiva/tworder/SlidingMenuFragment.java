package com.example.zakiva.tworder;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zakiva on 12/2/15.
 */
public class SlidingMenuFragment extends Fragment implements ExpandableListView.OnChildClickListener {

    private ExpandableListView sectionListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        List<Section> sectionList = createMenu();

        View view = inflater.inflate(R.layout.slidingmenu_fragment, container, false);
        this.sectionListView = (ExpandableListView) view.findViewById(R.id.slidingmenu_view);
        this.sectionListView.setGroupIndicator(null);

        SectionListAdapter sectionListAdapter = new SectionListAdapter(this.getActivity(), sectionList);
        this.sectionListView.setAdapter(sectionListAdapter);

        this.sectionListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                return true;
            }
        });

        this.sectionListView.setOnChildClickListener(this);

        int count = sectionListAdapter.getGroupCount();
        for (int position = 0; position < count; position++) {
            this.sectionListView.expandGroup(position);
        }

        return view;
    }

    private List<Section> createMenu() {
        List<Section> sectionList = new ArrayList<Section>();

        Section oDemoSection = new Section("My Business");
        oDemoSection.addSectionItem(101, "Orders", "black");
        oDemoSection.addSectionItem(102, "Orders History", "black");
        oDemoSection.addSectionItem(103, "Customers", "black");

        Section oGeneralSection = new Section("General");
        oGeneralSection.addSectionItem(201, "Settings", "black");
        oGeneralSection.addSectionItem(202, "Rate App", "black");
        oGeneralSection.addSectionItem(203, "Log Out", "black");
        oGeneralSection.addSectionItem(204, "Quit", "black");

        sectionList.add(oDemoSection);
        sectionList.add(oGeneralSection);
        return sectionList;
    }
/*
    static Activity mActivity;

    @Override
    public void onAttach(Context  /  Activity activity) {  there is a problem with it
        super.onAttach(activity);
        mActivity = (Activity) activity;
    }
*/
    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
                                int groupPosition, int childPosition, long id) {
        switch ((int)id) {
            case 101:
                //TODO
                break;
            case 102:
                ((business_orders__screen)getActivity()).history_clicked();
                //Intent i = new Intent(mActivity, business_orders_history.class);
                //startActivity(i);
                break;
            case 103:
                ((business_orders__screen)getActivity()).customers_clicked();
                break;
            case 201:
                //TODO
                break;
            case 202:
                //TODO
                break;
            case 203:
            ((business_orders__screen)getActivity()).OnLogOutClick();
                break;
            case 204:
                ((business_orders__screen)getActivity()).slidingMenu.toggle();
                break;
        }

        return false;
    }
}
