package com.example.zakiva.tworder;

import java.util.ArrayList;


class business_list_group  {
    private String mTitle;
    private ArrayList<String> mArrayChildren;
    private int rating;
    private String itemKey;
    private boolean editFlag;


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setEditFlag(boolean flag){
        editFlag = flag;
    }

    public boolean getEditFlag(){
        return editFlag;
    }

    public String getItemKey(){
        return itemKey;
    }

    public void setItemKey(String key){
        itemKey = key;
    }

    public int getUrgent(){
        return rating;
    }

    public void setUrgent(int n){
        rating = n;
    }

    public ArrayList<String> getArrayChildren() {
        return mArrayChildren;
    }

    public void setArrayChildren(ArrayList<String> arrayChildren) {
        mArrayChildren = arrayChildren;
    }
}

