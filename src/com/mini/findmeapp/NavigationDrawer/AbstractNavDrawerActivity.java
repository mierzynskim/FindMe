package com.mini.findmeapp.NavigationDrawer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mini.findmeapp.R;

public abstract class AbstractNavDrawerActivity extends FragmentActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    
    private ListView mDrawerListLeft;
    private ListView mDrawerListRight;
    
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private CharSequence mSubtitle = "";
    
    private NavDrawerActivityConfiguration navConf ;
    
    protected abstract NavDrawerActivityConfiguration getNavDrawerConfiguration();
    
    protected abstract void onNavItemSelected( int id );
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navConf = getNavDrawerConfiguration();
        
        setContentView(navConf.getMainLayout()); 
        
        mTitle = mDrawerTitle = getTitle();
        
        mSubtitle = getActionBar().getSubtitle();
        refreshDrawer();
    }
    
    protected void refreshDrawer() {
        navConf = getNavDrawerConfiguration();
        
        mDrawerLayout = (DrawerLayout) findViewById(navConf.getDrawerLayoutId());
        mDrawerListLeft = (ListView) findViewById(navConf.getLeftDrawerId());
        mDrawerListLeft.setAdapter(navConf.getBaseAdapterLeft());
        mDrawerListLeft.setOnItemClickListener(new DrawerLeftItemClickListener());
        
        
        mDrawerListRight = (ListView) findViewById(navConf.getRightDrawerId());
        mDrawerListRight.setAdapter(navConf.getBaseAdapterRight());
        mDrawerListRight.setOnItemClickListener(new DrawerRightItemClickListener());
        
        this.initDrawerShadow();
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        
        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getDrawerIcon(),
                navConf.getDrawerOpenDesc(),
                navConf.getDrawerCloseDesc()
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                getActionBar().setSubtitle(mSubtitle);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                getActionBar().setSubtitle(null);
                invalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    //TODO Change inefficient arrays to lists
    public void redrawLeftMenu(NavDrawerItem[] menu)
    {
    	navConf.setNavItemsLeft(menu);
    	navConf.setBaseAdapterLeft(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
		refreshDrawer();
		navConf.getBaseAdapterLeft().notifyDataSetChanged();
    }
    
    public void appendToLeftMenu(NavDrawerItem[] menu)
    {
    	NavDrawerItem[] newMenu = new NavDrawerItem[menu.length + navConf.getNavItemsLeft().length];
    	System.arraycopy(menu, 0, newMenu, 0, menu.length);
    	System.arraycopy(navConf.getNavItemsLeft(), 0, newMenu, menu.length, navConf.getNavItemsLeft().length);
    	navConf.setNavItemsLeft(newMenu);
    	navConf.setBaseAdapterLeft(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, newMenu ));
		refreshDrawer();
		navConf.getBaseAdapterLeft().notifyDataSetChanged();
    }
    
    public void redrawRightMenu(NavDrawerItem[] menu)
    {
    	navConf.setNavItemsRight(menu);
    	navConf.setBaseAdapterRight(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
		refreshDrawer();
		navConf.getBaseAdapterRight().notifyDataSetChanged();
    }
    
    public void appendToRightMenu(NavDrawerItem[] menu)
    {
    	NavDrawerItem[] newMenu = new NavDrawerItem[menu.length + navConf.getNavItemsRight().length];
    	System.arraycopy(menu, 0, newMenu, 0, menu.length);
    	System.arraycopy(navConf.getNavItemsLeft(), 0, newMenu, menu.length, navConf.getNavItemsLeft().length);
    	navConf.setNavItemsRight(newMenu);
    	navConf.setBaseAdapterRight(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, newMenu ));
		refreshDrawer();
		navConf.getBaseAdapterRight().notifyDataSetChanged();
    }
    
    
    protected void initDrawerShadow() {
        mDrawerLayout.setDrawerShadow(navConf.getDrawerShadow(), GravityCompat.START);
    }
    
    protected int getDrawerIcon() {
        return R.drawable.ic_drawer;
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if ( navConf.getActionMenuItemsToHideWhenDrawerOpen() != null ) {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerListLeft);
            for( int iItem : navConf.getActionMenuItemsToHideWhenDrawerOpen()) {
                menu.findItem(iItem).setVisible(!drawerOpen);
            }
            
            boolean drawerOpenRight = mDrawerLayout.isDrawerOpen(mDrawerListRight);
            for( int iItem : navConf.getActionMenuItemsToHideWhenDrawerOpen()) {
                menu.findItem(iItem).setVisible(!drawerOpenRight);
            }

        }
        return super.onPrepareOptionsMenu(menu);
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        else {
            return false;
        }
    }
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerListLeft)) {
                this.mDrawerLayout.closeDrawer(this.mDrawerListLeft);
            }
            else {
                this.mDrawerLayout.openDrawer(this.mDrawerListLeft);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
    
    protected DrawerLayout getDrawerLayout() {
        return mDrawerLayout;
    }

    protected ActionBarDrawerToggle getDrawerToggle() {
        return mDrawerToggle;
    }
   
    private class DrawerLeftItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectLeftItem(position);
        }
    }
    
    private class DrawerRightItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectRightItem(position);
        }
    }
    
    public void selectLeftItem(int position) {
        NavDrawerItem selectedItem = navConf.getNavItemsLeft()[position];
        
        this.onNavItemSelected(selectedItem.getId());
        mDrawerListLeft.setItemChecked(position, true);
        
        if ( selectedItem.updateActionBarTitle()) {
            setTitle(selectedItem.getLabel());
            setSubtitle(selectedItem.getSublabel());
        }
        
        if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerListLeft)) {
            mDrawerLayout.closeDrawer(mDrawerListLeft);
        }
    }
    
    public void selectRightItem(int position) {
        NavDrawerItem selectedItem = navConf.getNavItemsRight()[position];
        
        this.onNavItemSelected(selectedItem.getId());
        mDrawerListRight.setItemChecked(position, true);
        
        if ( selectedItem.updateActionBarTitle()) {
            setTitle(selectedItem.getLabel());
            setSubtitle(selectedItem.getSublabel());
        }
        
        if ( this.mDrawerLayout.isDrawerOpen(this.mDrawerListRight)) {
            mDrawerLayout.closeDrawer(mDrawerListRight);
        }
    }
    
    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }
    
    public void setSubtitle(CharSequence subtitle) {
        mSubtitle = subtitle;
        getActionBar().setSubtitle(subtitle);
    }
    
}