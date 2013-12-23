package com.mini.findmeapp;

import android.content.Intent;

//Klasa bêd¹ca proxy dla serwisu
//Udostêpnia metody do zarz¹dzania cyklem ¿ycia instancji FindMeService
public class ServiceProxy {

	private MainActivity mMainActivity;
	private String mUserId;
	
	//Nazwy wszyskich parametrów umieszczonych w intencie do przekazania do serwisu
	public static final String UserIdTag = "USER_ID";
	
	//Konstruktor klasy Proxy
	//Trzeba podaæ informacje niezbêdne do utworzenia serwisu
	public ServiceProxy(MainActivity mainActivity, String userId)
	{
		mMainActivity = mainActivity;
		mUserId = userId;
	}
	
	//Metoda s³u¿y do wystartowania serwisu
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
