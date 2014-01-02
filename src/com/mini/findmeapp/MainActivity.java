package com.mini.findmeapp;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Groups;
import com.mini.findmeapp.AzureConnection.Users;
import com.mini.findmeapp.AzureConnection.UsersGroups;
import com.mini.findmeapp.NavigationDrawer.AbstractNavDrawerActivity;
import com.mini.findmeapp.NavigationDrawer.NavDrawerActivityConfiguration;
import com.mini.findmeapp.NavigationDrawer.NavDrawerAdapter;
import com.mini.findmeapp.NavigationDrawer.NavDrawerItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuSection;
import com.mini.findmeapp.Service.ServiceProxy;


public class MainActivity extends AbstractNavDrawerActivity {

	private String mUserFacebookId = "1";
	private String mEventId = "1";
	private String mGroupId = "1";
	private String mCaption = "opis na mapce";
	private ServiceProxy mServiceProxy;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();

		recieveIntent();
		
		//Wystartowanie serwisu
		mServiceProxy = new ServiceProxy(this, mUserFacebookId, mEventId, mGroupId, mCaption);
		mServiceProxy.StartService();
		
		//Testy DatabaseProxy
		DatabaseProxy db = new DatabaseProxy(this);
						
//		db.addUser(mUserFacebookId, "albertwolant@gmail.com", new TableOperationCallback<Users>() 
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
		

		
//		db.addGroup(mUserFacebookId, "Super grupa druga", "Ale fajna to grupa. 2", "qwerty123", true, new TableOperationCallback<Groups>() {
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
		
//		db.addUserToGroup(mUserFacebookId, "7F651144-4FCC-419C-A41C-7C3949095D31" , "qwerty123", new TableOperationCallback<UsersGroups>() {
//
//			@Override
//			public void onCompleted(UsersGroups arg0, Exception arg1,
//					ServiceFilterResponse arg2) {
//				// TODO Auto-generated method stub
//				if(arg1 == null)
//					Log.i("service", "User dodany do grupy");
//				else
//					Log.i("service", "User NIE dodany do grupy");				
//			}
//		});
		
		
//	Uwaga: callback wywo³uje siê osobno dla ka¿dej grupy
//		db.getUserGroups(mUserFacebookId, new TableQueryCallback<Groups>() {
//			
//			@Override
//			public void onCompleted(List<Groups> arg0, int arg1, Exception arg2,
//					ServiceFilterResponse arg3) {
//				if(arg2 == null)
//				{
//					Groups group  = arg0.get(0);
//					Log.i("service", group.Id + " " + group.name + " " + group.description);
//				}
//				
//			}
//		});
	}

	private void recieveIntent()
	{
		Intent intent = getIntent();

		mUserFacebookId = intent.getStringExtra(LoginActivity.USER_ID);
		Toast.makeText(this, mUserFacebookId, Toast.LENGTH_LONG).show();
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