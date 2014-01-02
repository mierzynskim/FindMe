package com.mini.findmeapp.Service;

import java.net.MalformedURLException;
import java.util.Timer;
import java.util.TimerTask;
import com.microsoft.windowsazure.mobileservices.*;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

//Klasa serwisu
public class FindMeService extends Service 
{
        
        private Timer mTimer = new Timer();
        private GPSTracker mGpsTracker;
        private MobileServiceClient mClient;
        
        private String recordId;
        private String userId;
        private String eventId;
        private String groupId;
        private String caption;
        private double latitude;
        private double longitude;
        
        private UsersLocations mUserLocationItem;
        
        //Metoda wywo³ywana przy utworzeniu serwisu
        //Wykonuje wstêpn¹ konfiguracjê i uruchamia timer
		@SuppressWarnings("deprecation")
		@Override
        public void onStart(Intent intent, int startId) 
        {
                super.onStart(intent, startId);
                userId = intent.getStringExtra(ServiceProxy.UserIdTag);
                eventId = intent.getStringExtra(ServiceProxy.EventIdTag);
                groupId = intent.getStringExtra(ServiceProxy.GroupIdTag);
                caption = intent.getStringExtra(ServiceProxy.CaptionTag);

                //Utworzenie GPSTrackera
                mGpsTracker = new GPSTracker(this);
                latitude = mGpsTracker.getLatitude();
            	longitude = mGpsTracker.getLongitude();
                
                //Utworzenie klienta usług Azure
                try 
                {
					mClient = new MobileServiceClient( "https://findmeservice.azure-mobile.net/", "OLKfbhinUWxodQYJemjqlykJWzpizN37", this );
				} catch (MalformedURLException e) 
				{
					e.printStackTrace();
					Log.i("xxx", "azure exception");
				}
                
                //Utworzenie obiektu UsersLocation i dodanie go do bazy
                assemblyUsersLocations();
                
                //Dodanie lokalizacji użytkownika do bazy danych
                addUsersLocation();
                
                //Wystartowanie timera
                Log.i("service", "xxx Service Started"+userId);
                mTimer.scheduleAtFixedRate(new TimerTask(){public void run(){onTimerTick();}}, 0, 5000L);
                
        };
        
        //Dodanie informacji o lokalizacji użytkownika do bazy danych
        private void addUsersLocation() 
        {
        	
        	MobileServiceTable<UsersLocations> usersLocationTable = mClient.getTable(UsersLocations.class);
        	
        	usersLocationTable.insert(mUserLocationItem, new TableOperationCallback<UsersLocations>() 
        	{
		        public void onCompleted(UsersLocations entity, Exception exception, ServiceFilterResponse response) 
		        {   
		        		if (exception == null) 
		        		{
		        			recordId = entity.Id;
		        			Log.i("service", "xxx ADD Read object with ID " + entity.Id);  
		        		} 
		        }
        	});
        	
        	
		}

        //Usunięcie informacji o lokalizacji użytkownika z bazy dancych
        private void deleteUsersLocations() 
{
			
        	MobileServiceTable<UsersLocations> usersLocationsTable = mClient.getTable(UsersLocations.class);
        	Log.i("service", "xxx onDeliting " + recordId);
        	
        	usersLocationsTable.delete(mUserLocationItem, new TableDeleteCallback() 
        	{
				@Override
				public void onCompleted(Exception exception, ServiceFilterResponse arg1) 
				{
					if(exception == null)
						Log.i("service", "xxx DELETE SUCCESS");
					else
						Log.i("service", "xxx " + exception.getMessage());
					
				}
			});
			
		}
        
		//Uworzenie obiektu UsersLocation
        private void assemblyUsersLocations() 
{
			mUserLocationItem = new UsersLocations();
			
			mUserLocationItem.caption = caption;
			mUserLocationItem.eventId = eventId;
			mUserLocationItem.groupId = groupId;
			mUserLocationItem.userId = userId;
			mUserLocationItem.userLatitude = latitude;
			mUserLocationItem.userLongitude = longitude;
		}

		//Metoda wywo³ywana przy zatrzymaniu serwisu
        //Wy³¹cza timer i niszczy serwis
        @Override
        public void onDestroy()
        {
                android.util.Log.i("service", "xxx Service Stopped");
                mTimer.cancel();
                mGpsTracker.stopUsingGPS();
                
                //Usunięcie informacji o lokalizacji użytkownika z bazy dancyh
                deleteUsersLocations();
                
                super.onDestroy();
        };

		//Metoda wywolywana przy tyknieciu timera
        private void onTimerTick()
        {
        	double newLatitude = mGpsTracker.getLatitude();
        	double newLongitude = mGpsTracker.getLongitude();

        	if( newLatitude != latitude || newLongitude != longitude)
        		updateLocation(newLatitude, newLongitude);
      
        };
        
        //Uaktualnienie informacji o położwniu użytkownika w bazie danych
        private void updateLocation(double newLatitude, double newLongitude) 
        {
			latitude = newLatitude;
			longitude = newLongitude;
			
			mUserLocationItem.userLatitude = latitude;
			mUserLocationItem.userLongitude = longitude;
			
			MobileServiceTable<UsersLocations> usersLocationsTable = mClient.getTable(UsersLocations.class);
			
			usersLocationsTable.update(mUserLocationItem, new TableOperationCallback<UsersLocations>() 
        	{
		        public void onCompleted(UsersLocations entity, Exception exception, ServiceFilterResponse response) 
		        {   
		        		if (exception == null) 
		        		{
		        			Log.i("service", "xxx UPDATED Read object with ID " + entity.Id);  
		        		} 
		        }
        	});
			
		}

        //Implementacja klasy bazowej Service
		@Override
        public IBinder onBind(Intent arg0) 
		{
                return null;
        }
        
        


}

