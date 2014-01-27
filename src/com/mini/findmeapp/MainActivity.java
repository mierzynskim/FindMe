package com.mini.findmeapp;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.widget.LoginButton;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Groups;
import com.mini.findmeapp.NavigationDrawer.AbstractNavDrawerActivity;
import com.mini.findmeapp.NavigationDrawer.NavDrawerActivityConfiguration;
import com.mini.findmeapp.NavigationDrawer.NavDrawerAdapter;
import com.mini.findmeapp.NavigationDrawer.NavDrawerItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuItem;
import com.mini.findmeapp.NavigationDrawer.NavMenuSection;
import com.mini.findmeapp.Service.ServiceProxy;
import com.mini.findmeapp.Service.UsersLocations;

public class MainActivity extends AbstractNavDrawerActivity {

	//Proxy do obs³ugi Servisu wysy³aj¹cego lokalizacjê na serwer
	private ServiceProxy mServiceProxy;
	
	//Timer do cyklicznego pobierania lokalizacji u¿ytkowników z serwera
	private Timer mTimer;
	
	//Klasa trzyma dane sesji i zapisuje je do sharedpreferences
	public SessionData mSessionData;
	
	//Konfiguracja panelu nawigacji
	private NavDrawerActivityConfiguration navDrawerActivityConfiguration;
		
	//Obiekt mapy
	private GoogleMap mMap;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		//Inicjalizacja menu bocznych
		initMenu();
		
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.activity_main);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();
		
		LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
		
		//Utworzenie SessionData
		mSessionData = new SessionData(getSharedPreferences(SessionData.FILE_NAME, 0));
		//Wystartowanie serwisu
		mServiceProxy = new ServiceProxy(this, LoginActivity.user.getId(), mSessionData.getEventId(),
				mSessionData.getGroupId(), mSessionData.getCaption());
		
		//Utworzenie timera
		mTimer = new Timer(MainActivity.this, 10, mSessionData.getGroupId(),
				mSessionData.getEventId(), new TableQueryCallback<UsersLocations>() {
					
					@Override
					public void onCompleted(List<UsersLocations> arg0, int arg1,
							Exception arg2, ServiceFilterResponse arg3) {
						if (arg0 != null){
							for (UsersLocations usersLocations : arg0) {
								Log.i("service", String.valueOf(usersLocations.userLatitude) );
								mMap.addMarker(new MarkerOptions().position(new LatLng(usersLocations.userLatitude,
										usersLocations.userLongitude)));
								
							}
							
						}
						Log.i("TIMER", "TIMER COMPLETE");
						
					}
				});
		
	}
	
	@Override
	public void onStart() {
	    super.onStart();
    	MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    	mMap = mapFragment.getMap();

    	mMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				mMap.addMarker(new MarkerOptions().position(point));
				Log.i("tap", "xxx GROUP ADD OK");
				
			}
		});
    	
    	//Wystartowanie timera
    	mTimer.StartTimer();
    	
    	//Uaktualnienie informacji o grupie i evencie
    	mServiceProxy.ChangeGroup(mSessionData.getGroupId(), mSessionData.getEventId());
	}
	
	//Przy zatrzymaniu MainActivity zatrzymujemy te¿ Timer
	@Override
	protected void onStop() {
		mTimer.StopTimer();
		super.onStop();
	};
	
	//Przy zniszczeniu aplikacji dodatkowo zatrzymujemy serwis i Timer
	@Override
	protected void onDestroy()
	{
		mServiceProxy.StopService();
		mTimer.StopTimer();
		super.onDestroy();
	}

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {
		return navDrawerActivityConfiguration;
	}
	
	

	@Override
	protected void onNavItemSelected(int id) {
		//TODO: change data in mSessionData
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
		
		DatabaseProxy db = new DatabaseProxy(this);
		final ArrayList<NavDrawerItem> menuList = new ArrayList<NavDrawerItem>();
		final MainActivity mSelf = this;
		db.getUserGroups(LoginActivity.user.getId(), new TableQueryCallback<Groups>() {
			
			@Override
			public void onCompleted(List<Groups> groups, int count, Exception exception,
					ServiceFilterResponse filter) {
				
					if(exception == null)
					{
						Integer i = 0;
					
						Integer baseGroupIndex = 100;
						for(Groups group : groups)
						{
							menuList.add(NavMenuSection.create(baseGroupIndex+100*i, group.name));
							//TODO:add events for each group
							++i;
						}
					}
					
					NavDrawerItem[] menu = menuList.toArray(new NavDrawerItem[menuList.size()]);
					navDrawerActivityConfiguration.setNavItemsLeft(menu);
					navDrawerActivityConfiguration.setBaseAdapterLeft(
							new NavDrawerAdapter(mSelf, R.layout.navdrawer_item, menu ));
			}
		});
	
		//NavDrawerItem[] menu = new NavDrawerItem[] {
				//NavMenuSection.create( 100, "Group 1"),
				//NavMenuItem.create(102,"Event 1", "Group 1", "ic_action_new_event", true, this),
				//NavMenuItem.create(103,"Event 2","Group 1", "ic_action_new_event", true, this),
				//NavMenuSection.create( 200, "Group 2"),
				//NavMenuItem.create(201,"Event 1","Group 2", "ic_action_new_event", true, this),
				//NavMenuItem.create(202,"Event 2","Group 2", "ic_action_new_event", true, this),
				//NavMenuSection.create( 300, "Settings"),
				//NavMenuItem.create(302,"Add new group", "", "ic_action_new", false, this),
				//NavMenuItem.create(101,"Add event", "", "ic_action_add_group", false, this),
				//NavMenuItem.create(301,"App Settings", "", "ic_action_settings", false, this),
		//};
		
		NavDrawerItem[] menu2 = new NavDrawerItem[] {
				NavMenuSection.create( 400, "Members of Group"),
				NavMenuItem.create(401, LoginActivity.user.getFirstName() + " " + LoginActivity.user.getLastName(), "", "ic_action_add_group", false, this)
		};
		
		NavDrawerItem[] menu = menuList.toArray(new NavDrawerItem[menuList.size()]);

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