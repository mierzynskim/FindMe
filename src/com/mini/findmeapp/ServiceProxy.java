package com.mini.findmeapp;

import android.content.Intent;

//Klasa b�d�ca proxy dla serwisu
//Udost�pnia metody do zarz�dzania cyklem �ycia instancji FindMeService
public class ServiceProxy {

	private MainActivity mMainActivity;
	private String mUserId;
	
	//Nazwy wszyskich parametr�w umieszczonych w intencie do przekazania do serwisu
	public static final String UserIdTag = "USER_ID";
	
	//Konstruktor klasy Proxy
	//Trzeba poda� informacje niezb�dne do utworzenia serwisu
	public ServiceProxy(MainActivity mainActivity, String userId)
	{
		mMainActivity = mainActivity;
		mUserId = userId;
	}
	
	//Metoda s�u�y do wystartowania serwisu
	public void StartService()
	{
		//Wystartowanie serwisu
				Intent intent = new Intent(mMainActivity,FindMeService.class );
				intent.putExtra(UserIdTag, mUserId);
				mMainActivity.startService(intent);
		
	}
	
	public void StopService()
	{
		mMainActivity.stopService(new Intent(mMainActivity, FindMeService.class));
	}
	
	
	
	
	
	
	
}
