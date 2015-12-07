package com.example.zakiva.tworder;

import java.util.ArrayList;

/**
 * Created by zakiva on 12/7/15.
 */
public class business_customer_group {

    private String mTitle;
    private ArrayList<String> mArrayChildren;
    private String itemKey;


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

