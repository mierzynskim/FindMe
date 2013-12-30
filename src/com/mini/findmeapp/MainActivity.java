package com.mini.findmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.mini.findmeapp.AzureConnection.Groups;
import com.mini.findmeapp.AzureConnection.Users;
import com.mini.findmeapp.NavigationDrawer.AbstractNavDrawerActivity;
import com.mini.findmeapp.NavigationDrawer.NavDrawerActivityConfiguration;
import com.mini.findmeapp.NavigationDrawer.NavDrawerAdapter;
import com.mini.findmeapp.NavigationDrawer.NavDrawerItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuSection;
import com.mini.findmeapp.Service.ServiceProxy;


public class MainActivity extends AbstractNavDrawerActivity {

	private String mUserId = "1";
	private String mEventId = "1";
	private String mGroupId = "1";
	private String mCaption = "opis na mapce";
	private ServiceProxy mServiceProxy;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();

		//Wystartowanie serwisu
		mServiceProxy = new ServiceProxy(this, mUserId, mEventId, mGroupId, mCaption);
		mServiceProxy.StartService();
		
		recieveIntent();
		
		//Testy DatabaseProxy
//		DatabaseProxy db = new DatabaseProxy(this);
//						
//		db.addUser(mUserId, "albertwolant@gmail.com", new TableOperationCallback<Users>() 
//				{			
//			@Override
//			public void onCompleted(Users arg0, Exception arg1,
//					ServiceFilterResponse arg2) {
//				if(arg1 == null)
//				{
//					Log.i("service", "xxx USER ADD OK");
//				}
//				else
//					Log.i("service", "xxx USER ADD NIE OK " + arg1.getMessage());		
//			}
//		});
		

		
//		db.addGroup("100000874404821", "Super grupa", "Ale fajna to grupa.", "qwerty123", true, new TableOperationCallback<Groups>() {
//			@Override
//			public void onCompleted(Groups arg0, Exception arg1,
//					ServiceFilterResponse arg2) {
//				if(arg1 == null)
//				{
//					Log.i("service", "xxx GROUP ADD OK");
//					mGroupId = arg0.Id;
//				}
//				else
//					Log.i("service", "xxx GROUP NIE ADD OK");
//				
//			}
//
//			
//		});

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
				NavMenuSection.create( 100, "Group 1"),
				NavMenuItem.create(101,"Event 1", "Group 1", "navdrawer_friends", true, this),
				NavMenuItem.create(102,"Event 2","Group 1", "navdrawer_friends", true, this),
				NavMenuSection.create( 200, "Group 2"),
				NavMenuItem.create(201,"Event 1","Group 2", "navdrawer_friends", true, this),
				NavMenuItem.create(202,"Event 2","Group 2", "navdrawer_friends", true, this),
				NavMenuSection.create( 300, "Settings"),
				NavMenuItem.create(301,"App Settings", "", "ic_action_settings", false, this),
		};
		
		NavDrawerItem[] menu2 = new NavDrawerItem[] {
				NavMenuSection.create( 400, "Members of Group"),

		};

		NavDrawerActivityConfiguration navDrawerActivityConfiguration = new NavDrawerActivityConfiguration();
		navDrawerActivityConfiguration.setMainLayout(R.layout.activity_main);
		navDrawerActivityConfiguration.setDrawerLayoutId(R.id.drawer_layout);
		navDrawerActivityConfiguration.setLeftDrawerId(R.id.left_drawer);
		navDrawerActivityConfiguration.setRightDrawerId(R.id.right_drawer);
		navDrawerActivityConfiguration.setNavItemsLeft(menu);
		navDrawerActivityConfiguration.setNavItemsRight(menu);
		navDrawerActivityConfiguration.setDrawerShadow(R.drawable.drawer_shadow);
		navDrawerActivityConfiguration.setDrawerOpenDesc(R.string.drawer_open);
		navDrawerActivityConfiguration.setDrawerCloseDesc(R.string.drawer_close);
		navDrawerActivityConfiguration.setBaseAdapterLeft(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, menu ));
		navDrawerActivityConfiguration.setBaseAdapterRight(
				new NavDrawerAdapter(this, R.layout.navdrawer_item, menu2 ));
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