package com.mini.findmeapp;


import android.content.Intent;
import android.os.Bundle;

import com.facebook.widget.LoginButton;
import com.mini.findmeapp.NavigationDrawer.AbstractNavDrawerActivity;
import com.mini.findmeapp.NavigationDrawer.NavDrawerActivityConfiguration;
import com.mini.findmeapp.NavigationDrawer.NavDrawerAdapter;
import com.mini.findmeapp.NavigationDrawer.NavDrawerItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuSection;
import com.mini.findmeapp.Service.ServiceProxy;


public class MainActivity extends AbstractNavDrawerActivity {

	private String mCaption = "opis na mapce";
	private ServiceProxy mServiceProxy;
	private Timer mTimer;
	
	private NavDrawerActivityConfiguration navDrawerActivityConfiguration;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		initMenu();
		super.onCreate(savedInstanceState);

		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();

		LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
		//Wystartowanie serwisu
		mServiceProxy = new ServiceProxy(this, LoginActivity.user.getId(), "6ED74A78-0B5E-4C1E-9ED6-0220B6724562", "9CDE2757-E243-4055-B0BB-6E9EA63A4A5B", mCaption);
		mServiceProxy.StartService();
		
		
				
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
		return navDrawerActivityConfiguration;
	}
	
	

	@Override
	protected void onNavItemSelected(int id) {
		switch ((int)id) {
		case 101:
//			NavDrawerItem[] menu = new NavDrawerItem[] {
//					NavMenuSection.create( 100, "Group 1"),
//					NavMenuItem.create(101,"Event 1", "Group 1", "ic_action_new_event", true, this),
//					NavMenuItem.create(102,"Event 2","Group 1", "ic_action_new_event", true, this),
//					NavMenuSection.create( 300, "Settings"),
//					NavMenuItem.create(301,"App Settings", "", "ic_action_settings", false, this),
//			};
//			
//			addGroup(menu, null);
			Intent intentGroup = new Intent(MainActivity.this, AddEventActivity.class);
			startActivity(intentGroup);
			break;
		case 301:
			Intent intentSetting = new Intent(MainActivity.this, SettingsActivity.class);
			startActivity(intentSetting);
			break;
		case 302:
			Intent intent = new Intent(MainActivity.this, AddGroupActivity.class);
			startActivity(intent);
			break;
		}

	};
	
	private void initMenu() {
		NavDrawerItem[] menu = new NavDrawerItem[] {
				NavMenuSection.create( 100, "Group 1"),
				NavMenuItem.create(102,"Event 1", "Group 1", "ic_action_new_event", true, this),
				NavMenuItem.create(103,"Event 2","Group 1", "ic_action_new_event", true, this),
				NavMenuSection.create( 200, "Group 2"),
				NavMenuItem.create(201,"Event 1","Group 2", "ic_action_new_event", true, this),
				NavMenuItem.create(202,"Event 2","Group 2", "ic_action_new_event", true, this),
				NavMenuSection.create( 300, "Settings"),
				NavMenuItem.create(302,"Add new group", "", "ic_action_new", false, this),
				NavMenuItem.create(101,"Add event", "", "ic_action_add_group", false, this),
				NavMenuItem.create(301,"App Settings", "", "ic_action_settings", false, this),
		};
		
		NavDrawerItem[] menu2 = new NavDrawerItem[] {
				NavMenuSection.create( 400, "Members of Group"),
				NavMenuItem.create(401, LoginActivity.user.getFirstName() + " " + LoginActivity.user.getLastName(), "", "ic_action_add_group", false, this)
		};

		navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
		navDrawerActivityConfiguration.setMainLayout(R.layout.activity_main);
		navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
		navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
		navDrawerActivityConfiguration.setRightDrawerId(R.id.right_drawer);
		navDrawerActivityConfiguration.setNavItemsLeft(menu);
		navDrawerActivityConfiguration.setNavItemsRight(menu2);
		navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
		navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
		navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
		navDrawerActivityConfiguration.setBaseAdapterLeft(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
		navDrawerActivityConfiguration.setBaseAdapterRight(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, menu2 ));
	}
}