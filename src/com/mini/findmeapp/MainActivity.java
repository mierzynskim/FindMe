package com.mini.findmeapp;

import java.net.MalformedURLException;
import java.util.List;

import android.os.Bundle;
import android.app.Activity;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.view.View;

import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;


import com.microsoft.windowsazure.mobileservices.*;
import com.google.gson.*;



public class MainActivity extends Activity {
	
	private MobileServiceClient mClient;
	private String mUserId = "abc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		try {
			mClient = new MobileServiceClient( "https://findmeservice.azure-mobile.net/", "OLKfbhinUWxodQYJemjqlykJWzpizN37", this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		authenticate();

		
		//ListView listView = (ListView) findViewById(R.id.myList);
		//MobileServiceTable<ToDoItem> mToDoTable = mClient.getTable(ToDoItem.class);
		
		//MobileServiceJsonTable values =  mClient.getTable("ToDoItem");
		//mToDoTable.execute(new TableQueryCallback<ToDoItem>() {
            //public void onCompleted(List<ToDoItem> result, int count,
                //Exception exception, ServiceFilterResponse response) {
                //if (exception == null) {
                    //for (ToDoItem item : result) {
                        //Log.i("FindMeLOG", "Read object with ID " + item.getText());  
                    //}
                //}
            //}
        //});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void AddToDoItem(View view){
		Log.i("FindMeLOG", "AddToDoITem");
		
		ToDoItem newItem = new ToDoItem();
		newItem.setText("Another item from app by user" + mUserId);
		
		MobileServiceTable<ToDoItem> mToDoTable = mClient.getTable(ToDoItem.class);
		
		mToDoTable.insert(newItem, new TableOperationCallback<ToDoItem>(){
			public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response){
				if( exception == null){
				
					Log.i("FindMeLOG", "Inserted item with id" + entity.getId());
				}
				else
				{
					Log.i("FindMeLOG", "ERROR");
				}
				
			}
			
			
		});
		
		
	}
	
	private void authenticate() {


		// Login using the Google provider.
		mClient.login(MobileServiceAuthenticationProvider.Facebook,
		        new UserAuthenticationCallback() {


		            @Override
		            public void onCompleted(MobileServiceUser user,
		                    Exception exception, ServiceFilterResponse response) {


		                if (exception == null) {
		                    Log.i("FindMeLOG",
		                                    "You are now logged in - %1$2s" +
		                                    user.getUserId() + "Success");
		                    mUserId = user.getUserId();
		                  
		                } else {
		                    Log.i("FindMeLOG","You must log in. Login Required" + "Error");
		                }
		            }
		        });


		}

}



