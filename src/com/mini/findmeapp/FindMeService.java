package com.mini.findmeapp;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

//Klasa serwisu
public class FindMeService extends Service {
	
	private Timer mTimer = new Timer();
	private int counter;
	private String userId;
	private double langitude;
	private double longitude;
	
	
	//Metoda wywo³ywana przy utworzeniu serwisu
	//Wykonuje wstêpn¹ konfiguracjê i uruchamia timer
	@Override
	public void onStart(Intent intent, int startId) 
	{
		super.onStart(intent, startId);
		
		Log.i("service", "xxx Service Started");
		counter = 0;
		userId = intent.getStringExtra(ServiceProxy.UserIdTag);

		//Wystartowanie timera
		mTimer.scheduleAtFixedRate(new TimerTask(){public void run(){onTimerTick();}}, 0, 1000L);
		
		
		
	};
	
	//Metoda wywo³ywana przy zatrzymaniu serwisu
	//Wy³¹cza timer i niszczy serwis
	@Override
	public void onDestroy()
	{
		android.util.Log.i("service", "xxx Service Stopped");
		mTimer.cancel();
		super.onDestroy();
	};
	
	//Metoda wywo³ywana przy tykniêciu timera
	private void onTimerTick()
	{
		counter++;
		Log.i("service","xxx "+Integer.toString(counter) + " " + userId);
		
		
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		
		return null;
	}

}
