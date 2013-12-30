package com.mini.findmeapp.NavigationDrawer;

public interface NavDrawerItem {
    public int getId();
    public String getLabel();
    public String getSublabel();
    public int getType();
    public boolean isEnabled();
    public boolean updateActionBarTitle();
}
