package com.mini.findmeapp.AzureConnection;

import java.net.MalformedURLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;

//Klasa reperzentuje proxy dla operacji na bazie danych, w obecnej wersji przez Azure Mobile Services
public class DatabaseProxy {
	
	private MobileServiceClient mClient;
	
	//Konstruktor DatabaseProxy. Context to np. MainActivity
	public DatabaseProxy(Context context)
	{
		//Utworzenie klienta us³ug Azure
        try 
        {
			mClient = new MobileServiceClient( "https://findmeservice.azure-mobile.net/", "OLKfbhinUWxodQYJemjqlykJWzpizN37", context );
			Log.i("service", "xxx azure OK");
		} catch (MalformedURLException e) 
		{
			e.printStackTrace();
			Log.i("service", "xxx azure exception");
		}
	}
	
	//Metoda próbuje dodaæ nowego u¿ytkownika pod warunkiem, ¿e nie ma w bazie u¿ytkownika o takim samym facebookId
	//Po próbie wywo³uje onInsertCallback
	public void addUser(final String userId, final String userEmail, final TableOperationCallback<Users> onInsertCallback)
	{
		final MobileServiceTable<Users> usersTable = mClient.getTable(Users.class);
		
		usersTable.where().field("facebookId").eq(userId).execute(new TableQueryCallback<Users>() {
			
			@Override
			public void onCompleted(List<Users> result, int count, Exception exception,
					ServiceFilterResponse filter) {
				if(exception == null )
				{
					Boolean condition = false;
					
					for(Users item : result)
						condition = item != null ? true : condition;
					
					if(condition)
					{
						Log.i("service", "xxx USER ADD NIE OK BO JUZ DODANY");
						return;
					}
					
					Users userToAdd = new Users();
					userToAdd.facebookId = userId;
					userToAdd.eventCounter = Users.MAX_EVENTS;
					userToAdd.groupCounter = Users.MAX_GROUPS;
					userToAdd.emailAddress = userEmail;

					usersTable.insert(userToAdd, onInsertCallback);
				}
			}
		});
	}

	
	public void addGroup(String userFacebookId, final String name, final String description, final String password, final Boolean isPrivate, final TableOperationCallback<Groups> onGroupAdd)
	{
		final MobileServiceTable<Users> usersTable = mClient.getTable(Users.class);
		
		usersTable.where().field("facebookId").eq(userFacebookId).select("Id","facebookId","groupCounter","eventCounter","emailAddress").execute(new TableQueryCallback<Users>() {
			
			@Override
			public void onCompleted(List<Users> result, int count, Exception exception,
					ServiceFilterResponse filter) {
				if( exception == null )
					for(Users item : result)
					{
						if(item != null && item.groupCounter > 0)
						{
							item.groupCounter--;
							usersTable.update(item, null);
						
							MobileServiceTable<Groups> groupsTable = mClient.getTable(Groups.class);
							Groups groups = new Groups();
							groups.name = name;
							groups.description = description;
							groups.isPrivate = isPrivate;
							groups.password = password;
							groups.ownerId = item.Id;
						
							groupsTable.insert(groups, onGroupAdd);
						}
					}
				else
					Log.i("service", "xxx USER BY ID NIE OK OK");
			}
		});
	}
}
