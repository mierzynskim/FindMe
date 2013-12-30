package com.mini.findmeapp.NavigationDrawer;

import android.content.Context;

public class NavMenuItem implements NavDrawerItem {

    public static final int ITEM_TYPE = 1 ;

    private int id ;
    private String label; 
    private String subLabel;
    private int icon ;
    private boolean updateActionBarTitle ;

    private NavMenuItem() {
    }

    public static NavMenuItem create( int id, String label, String subLabel, String icon, boolean updateActionBarTitle, Context context ) {
        NavMenuItem item = new NavMenuItem();
        item.setId(id);
        item.setLabel(label);
        item.setSubLabel(subLabel);
        item.setIcon(context.getResources().getIdentifier( icon, "drawable", context.getPackageName()));
        item.setUpdateActionBarTitle(updateActionBarTitle);
        return item;
    }
    
    @Override
    public int getType() {
        return ITEM_TYPE;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean updateActionBarTitle() {
        return this.updateActionBarTitle;
    }

    public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
        this.updateActionBarTitle = updateActionBarTitle;
    }


	public void setSubLabel(String subLabel) {
		this.subLabel = subLabel;
	}

	@Override
	public String getSublabel() {
		return this.subLabel;
	}

    
}
