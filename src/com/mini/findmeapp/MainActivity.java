package com.mini.findmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.mini.findmeapp.NavigationDrawer.AbstractNavDrawerActivity;
import com.mini.findmeapp.NavigationDrawer.NavDrawerActivityConfiguration;
import com.mini.findmeapp.NavigationDrawer.NavDrawerAdapter;
import com.mini.findmeapp.NavigationDrawer.NavDrawerItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuSection;


public class MainActivity extends AbstractNavDrawerActivity {

	private String mUserId = "abc";
    private ServiceProxy mServiceProxy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();
        //setContentView(R.layout.activity_main);
        
		//Wystartowanie serwisu
		mServiceProxy = new ServiceProxy(this, mUserId);
		mServiceProxy.StartService();
		
		recieveIntent();

    }
    
    private void recieveIntent()
    {
		Intent intent = getIntent();
		
		mUserId = intent.getStringExtra(LoginActivity.USER_ID);
		Toast.makeText(this, mUserId, Toast.LENGTH_LONG).show();
    }
    
    
    //Przy zniszczeniu aplikacji dodatkowo zatrzymujemy serwis
    @Override
    protected void onDestroy() 
    {
    	mServiceProxy.StopService();
    	super.onDestroy();
    }

    @Override
    protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
        
        NavDrawerItem[] menu = new NavDrawerItem[] {
                NavMenuSection.create( 100, "Groups"),
                NavMenuItem.create(101,"Group 1", "navdrawer_friends", false, this),
                NavMenuSection.create( 200, "Favourites"),
                NavMenuItem.create(201,"Group 1", "navdrawer_friends", false, this),
                NavMenuItem.create(202,"Group 2", "navdrawer_friends", false, this),
                NavMenuSection.create( 300, "Settings"),
                NavMenuItem.create(301,"App Settings", "navdrawer_friends", false, this),
          };
        
        NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
        navDrawerActivityConfiguration.setMainLayout(R.layout.activity_main);
        navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
        navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
        navDrawerActivityConfiguration.setNavItems(menu);
        navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);       
        navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
        navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
        navDrawerActivityConfiguration.setBaseAdapter(
            new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
        return navDrawerActivityConfiguration;
    }

	@Override
	protected void onNavItemSelected(int id) {
        switch ((int)id) {
        case 301:
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            break;
        }
		
	};
    


}