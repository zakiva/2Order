package com.example.zakiva.tworder;

import java.util.ArrayList;

/**
 * Created by Nir Sade on 12/10/2015.
 */
public class customer_list_group {
    private String mTitle;
    private ArrayList<String> mArrayChildren;
    private String itemKey;
    private boolean editFlag;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getItemKey(){
        return itemKey;
    }

    public void setItemKey(String key){
        itemKey = key;
    }

    public ArrayList<String> getArrayChildren() {
        return mArrayChildren;
    }

    public void setArrayChildren(ArrayList<String> arrayChildren) {
        mArrayChildren = arrayChildren;
    }
}
