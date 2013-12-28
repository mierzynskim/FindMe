package com.mini.findmeapp.NavigationDrawer;

import android.widget.BaseAdapter;

public class NavDrawerActivityConfiguration {

    private int mainLayout;
    private int drawerShadow;
    private int drawerLayoutId;
    private int leftDrawerId;
    private int rightDrawerId;
    private int[] actionMenuItemsToHideWhenDrawerOpen;
    private NavDrawerItem[] navItemsLeft;
    private NavDrawerItem[] navItemsRight;
    private int drawerOpenDesc;
    private int drawerCloseDesc;
    private BaseAdapter baseAdapterLeft;
    private BaseAdapter baseAdapterRight;

    public int getMainLayout() {
        return mainLayout;
    }

    public void setMainLayout(int mainLayout) {
        this.mainLayout = mainLayout;
    }

    public int getDrawerShadow() {
        return drawerShadow;
    }

    public void setDrawerShadow(int drawerShadow) {
        this.drawerShadow = drawerShadow;
    }

    public int getDrawerLayoutId() {
        return drawerLayoutId;
    }

    public void setDrawerLayoutId(int drawerLayoutId) {
        this.drawerLayoutId = drawerLayoutId;
    }

    public int getLeftDrawerId() {
        return leftDrawerId;
    }

    public void setLeftDrawerId(int leftDrawerId) {
        this.leftDrawerId = leftDrawerId;
    }
    
	public int getRightDrawerId() {
		return rightDrawerId;
	}

	public void setRightDrawerId(int rightDrawerId) {
		this.rightDrawerId = rightDrawerId;
	}

    public int[] getActionMenuItemsToHideWhenDrawerOpen() {
        return actionMenuItemsToHideWhenDrawerOpen;
    }

    public void setActionMenuItemsToHideWhenDrawerOpen(
            int[] actionMenuItemsToHideWhenDrawerOpen) {
        this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
    }

    public NavDrawerItem[] getNavItemsLeft() {
        return navItemsLeft;
    }

    public void setNavItemsLeft(NavDrawerItem[] navItems) {
        this.navItemsLeft = navItems;
    }
    
    public NavDrawerItem[] getNavItemsRight() {
        return navItemsRight;
    }

    public void setNavItemsRight(NavDrawerItem[] navItems) {
        this.navItemsRight = navItems;
    }

    public int getDrawerOpenDesc() {
        return drawerOpenDesc;
    }

    public void setDrawerOpenDesc(int drawerOpenDesc) {
        this.drawerOpenDesc = drawerOpenDesc;
    }

    public int getDrawerCloseDesc() {
        return drawerCloseDesc;
    }

    public void setDrawerCloseDesc(int drawerCloseDesc) {
        this.drawerCloseDesc = drawerCloseDesc;
    }

    public BaseAdapter getBaseAdapterLeft() {
        return baseAdapterLeft;
    }

    public void setBaseAdapterLeft(BaseAdapter baseAdapter) {
        this.baseAdapterLeft = baseAdapter;
    }
    
    public BaseAdapter getBaseAdapterRight() {
        return baseAdapterRight;
    }

    public void setBaseAdapterRight(BaseAdapter baseAdapter) {
        this.baseAdapterRight = baseAdapter;
    }
    
    


}
