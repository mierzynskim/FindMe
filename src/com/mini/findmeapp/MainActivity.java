package com.mini.findmeapp;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;

import com.facebook.widget.LoginButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Events;
import com.mini.findmeapp.AzureConnection.Groups;
import com.mini.findmeapp.JoinGroup.AddUserToGroupMainActivity;
import com.mini.findmeapp.NavigationDrawer.AbstractNavDrawerActivity;
import com.mini.findmeapp.NavigationDrawer.NavDrawerActivityConfiguration;
import com.mini.findmeapp.NavigationDrawer.NavDrawerAdapter;
import com.mini.findmeapp.NavigationDrawer.NavDrawerItem;
import com.mini.findmeapp.NavigationDrawer.NavItemsManager;
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
	
	//Manager itemów w lewym menu
	private NavItemsManager mNavItemsManager;
	
	//Czy by³a zmiana
	public static Boolean wasChange = true;
	
	private Boolean isFirst = true;


	@Override
	protected void onCreate(Bundle savedInstanceState){
		
		//Inicjalizacja menu bocznych
		initMenu();
		
		super.onCreate(savedInstanceState);
		Log.i("service", "MAIN ACTIVITY ON CREATE");
		
		//setContentView(R.layout.activity_main);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new GoogleMapFragment()).commit();
		
		LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
		
		//Utworzenie SessionData
		mSessionData = new SessionData(getSharedPreferences(SessionData.FILE_NAME, 0));
		
		//Utworzenie serwisu
		mServiceProxy = new ServiceProxy(this, LoginActivity.user.getId(), mSessionData.getEventId(),
				mSessionData.getGroupId(), mSessionData.getCaption());
		
		//Utworzenie ItemsManagera
		mNavItemsManager = new NavItemsManager(this);
		
		//Utworzenie timera
		mTimer = new Timer(MainActivity.this, 10, mSessionData.getGroupId(),
				mSessionData.getEventId(), new TableQueryCallback<UsersLocations>() {
					
					@Override
					public void onCompleted(List<UsersLocations> arg0, int arg1,
							Exception arg2, ServiceFilterResponse arg3) {
						if (arg0 != null){
							mMap.clear();
							ArrayList<NavDrawerItem> captionList = new ArrayList<NavDrawerItem>();
							captionList.add(NavMenuSection.create(10, "Members captions"));
							for (UsersLocations usersLocations : arg0) {
								LatLng userPosition = new LatLng(usersLocations.userLatitude, usersLocations.userLongitude);
								Log.i("service", String.valueOf(usersLocations.userLatitude) );
								if (isFirst) {
									mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPosition, 13));
									isFirst = false;
								}
								mMap.addMarker(new MarkerOptions().position(new LatLng(usersLocations.userLatitude,
										usersLocations.userLongitude)).title("Find me").snippet(usersLocations.caption));
								captionList.add(NavMenuItem.create(10, usersLocations.caption, "", "ic_action_person", false, MainActivity.this));
								
							}
							
							redrawRightMenu(captionList.toArray(new NavDrawerItem[0]));
							
						}
						Log.i("TIMER", "TIMER COMPLETE");
						
					}
				});
		
	}
	
	@Override
	public void onStart() {
	    super.onStart();
	    Log.i("service", "MAIN ACTIVITY ON START");
    	MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    	mMap = mapFragment.getMap();

    	
    	//Wystartowanie timera
    	mTimer.ChangeParameters(mSessionData.getGroupId(), mSessionData.getEventId());
    	mTimer.StartTimer();
    	
    	//Uaktualnienie informacji o grupie i evencie
    	mServiceProxy.ChangeGroup(mSessionData.getGroupId(), mSessionData.getEventId());
    	
    	//TODO:uaktualnienie bocznego panelu
    	if(wasChange)
    	{
    		refreshMenu();
    		wasChange = false;
    	}
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
		
		//TODO: przy wybraniu nowego eventu zmiana info w mSessionData plus restart timera i serwisu jak w onStart
		
		//Wybrano coœ z sekcji Settings
		if( id < 100)
		{
			switch (id) {
				
				case 20:
					Intent intentGroup = new Intent(MainActivity.this, AddGroupActivity.class);
					startActivity(intentGroup);
					break;
				case 30:
					Intent intentEvent = new Intent(MainActivity.this, AddEventActivity.class);
					startActivity(intentEvent);
					break;
				case 40:
					Intent intentAddUser = new Intent(MainActivity.this, AddUserToGroupMainActivity.class);
					startActivity(intentAddUser);
					break;
				case 50:
					Intent intentSetting = new Intent(MainActivity.this, SettingsActivity.class);
					startActivity(intentSetting);
					break;
				case 60:
					
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Type new caption");

					// Set up the input
					final EditText input = new EditText(this);
					// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
					input.setInputType(InputType.TYPE_CLASS_TEXT );
					builder.setView(input);

					// Set up the buttons
					builder.setPositiveButton("OK", new DialogInterface.OnClickListener() { 
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        mSessionData.setCaption(input.getText().toString());
					        mServiceProxy.ChangeCaption(mSessionData.getCaption());
					    }
					});
					builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					    @Override
					    public void onClick(DialogInterface dialog, int which) {
					        dialog.cancel();
					    }
					});

					builder.show();
					break;
			}
		}
		else
		{
			Events event = mNavItemsManager.getEventAt(id);
			Groups group = mNavItemsManager.getGroupAt(id);
			
			//Zmiana aktualnej grupy i eventu
			mSessionData.setGroupId(group.Id);
			mSessionData.setEventId(event.Id);
			
			//Wystartowanie timera
	    	mTimer.ChangeParameters(mSessionData.getGroupId(), mSessionData.getEventId());
	    	mTimer.StartTimer();
	    	
	    	//Uaktualnienie informacji o grupie i evencie
	    	mServiceProxy.ChangeGroup(mSessionData.getGroupId(), mSessionData.getEventId());
		
		}

	};
	
	private void initMenu() {
		
		NavDrawerItem[] menu = new NavDrawerItem[0];
		
		NavDrawerItem[] menu2 = new NavDrawerItem[] {
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
	
	private void refreshMenu()
	{
		
		Log.i("service", "REFRESH START");
		final DatabaseProxy db = new DatabaseProxy(this);
		mNavItemsManager = new  NavItemsManager(this);
        
        db.getUserGroups(LoginActivity.user.getId(), new TableQueryCallback<Groups>() {
                
                @Override
                public void onCompleted(List<Groups> groups, int count, Exception exception,
                                ServiceFilterResponse filter) {
                        
                                if(exception == null)
                                {
                                        for(Groups group : groups)
                                        {
                                        	mNavItemsManager.addGroup(group);
                                        	
                                        	final Groups tmp = group;
                                        	db.getAllEvents(group.Id, new TableQueryCallback<Events>() {
												
												@Override
												public void onCompleted(List<Events> arg0, int arg1, Exception arg2,
														ServiceFilterResponse arg3) {
													
													if(arg2 == null)
													{
														for(Events event : arg0)
														{
															mNavItemsManager.addEvent(event, tmp);
														}
														
														redrawLeftMenu(mNavItemsManager.getItemsArray());
													}
													
												}
											});
                                        }
                                        
                                        redrawLeftMenu(mNavItemsManager.getItemsArray());
                                }
                                
                                
                }
        });
	
        redrawLeftMenu(mNavItemsManager.getItemsArray());
	}
	
}