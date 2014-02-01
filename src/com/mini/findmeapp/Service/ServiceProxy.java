package com.mini.findmeapp.Service;

import java.net.MalformedURLException;
import java.util.List;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mini.findmeapp.MainActivity;
import com.mini.findmeapp.AzureConnection.Users;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

//Klasa bêd¹ca proxy dla serwisu
//Udostêpnia metody do zarz¹dzania cyklem ¿ycia instancji FindMeService
public class ServiceProxy {

	private MainActivity mMainActivity;
	private String mUserId;
	public String getmCaption() {
		return mCaption;
	}

	public String getmEventId() {
		return mEventId;
	}

	public String getmGroupId() {
		return mGroupId;
	}

	private String mEventId;
	private String mGroupId;
	private String mCaption;
	
	//Nazwy wszyskich parametrów umieszczonych w intencie do przekazania do serwisu
	public static final String UserIdTag = "USER_ID";
	public static final String EventIdTag = "EVENT_ID";
	public static final String GroupIdTag = "GROUP_ID";
	public static final String CaptionTag = "CAPTION";
	
	//Konstruktor klasy Proxy
	//Trzeba podaæ informacje niezbêdne do utworzenia serwisu
	public ServiceProxy(MainActivity mainActivity, String userId, String eventId, String groupId, String caption)
	{
		mMainActivity = mainActivity;
		mUserId = userId;
		mEventId = eventId;
		mGroupId = groupId;
		mCaption = caption;
	}
	
	//Metoda s³u¿y do wystartowania serwisu
	public void StartService()
	{
		//Sprawdzenie statusu us³ug GPS
		if(!checkGpsStatus())
			showNoLocationDialog();
		else
		{
			//Wystartowanie serwisu
			final Intent intent = new Intent(mMainActivity,FindMeService.class );
			intent.putExtra(EventIdTag, mEventId);
			intent.putExtra(GroupIdTag, mGroupId);
			intent.putExtra(CaptionTag, mCaption);
			
			try {
				MobileServiceClient mClient = new MobileServiceClient( "https://findmeservice.azure-mobile.net/", "OLKfbhinUWxodQYJemjqlykJWzpizN37", mMainActivity );
				MobileServiceTable<Users> users = mClient.getTable(Users.class);
				users.where().field("facebookId").eq(mUserId).select("Id").execute(new TableQueryCallback<Users>() {
					
					@Override
					public void onCompleted(List<Users> arg0, int arg1, Exception arg2,
							ServiceFilterResponse arg3) {
						if(arg2 == null)
						{
							for(Users user : arg0)
								intent.putExtra(UserIdTag, user.Id);
							
							mMainActivity.startService(intent);
						}
						
					}
				});
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
				
			
		}
		
	}
	
	//Sprawdzenie statusu us³ug GPS i ewentualne ich ustawienie
	private boolean	checkGpsStatus()
	{
		LocationManager locationManager = (LocationManager)mMainActivity.getSystemService(Context.LOCATION_SERVICE);
		
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || 
				locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}
	private void showNoLocationDialog() 
	{
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mMainActivity);
	      
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
  
        // Setting Dialog Message
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
  
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mMainActivity.startActivity(intent);
            }
        });
  
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	mMainActivity.finish();
            }
        });
  
        // Showing Alert Message
        alertDialog.show();
    }

	//Zatrzymanie serwisu
	public void StopService()
	{
		mMainActivity.stopService(new Intent(mMainActivity, FindMeService.class));
	}
	
	//Zmiana Eventu
	public void ChangeEvent(String newEventId)
	{
		StopService();
		mEventId = newEventId;
		StartService();
	}
	
	//Zmiana grupy
	public void ChangeGroup(String newGroupId, String newEventId, MainActivity mainActivity)
	{
		StopService();
		mMainActivity = mainActivity;
		mGroupId = newGroupId;
		mEventId = newEventId;
		StartService();
	}
	
	//Zmiana opisu
	public void ChangeCaption(String newCaption)
	{
		StopService();
		mCaption = newCaption;
		StartService();
	}
	
	
	
}
