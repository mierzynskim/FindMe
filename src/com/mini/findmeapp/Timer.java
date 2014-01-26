package com.mini.findmeapp;

import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.Service.UsersLocations;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

//Wystartowanie timera;
//mTimer = new Timer(this, 5, "6ED74A78-0B5E-4C1E-9ED6-0220B6724562", "9CDE2757-E243-4055-B0BB-6E9EA63A4A5B", null);
//mTimer.StartTimer();
//Zmiana parametrów
//mTimer.ChangeParameters("6ED74A78-0B5E-4C1E-9hlkhjjED6-0220B6724562", "9CDE2lkjhlkjh757-E243-4055-B0BB-6E9EA63A4A5B");
//Zakoñczenie timera (po tym ju¿ go nie ma )
//mTimer.CancelTimer();
		

public class Timer{
	
	private final MainActivity mMainActivity;
	private final Integer mTimeout;
	private final DatabaseProxy mDb;
	private String mGroupId;
	private String mEventId;
	private final TableQueryCallback<UsersLocations> mCallback;
	
	private Handler mHandler;
	private Runnable mRunnable;
	
	
	//Konstruktor
	//timeout w sekundach
	public Timer(MainActivity mainActivity, int timeout, String groupId, String eventId, TableQueryCallback<UsersLocations> callback ) 
	{
		mMainActivity = mainActivity;
		mTimeout = timeout * 1000;
		mDb = new DatabaseProxy(mMainActivity);
		mGroupId = groupId;
		mEventId = eventId;
		mCallback = callback;
		
		mHandler = new Handler();
		
		mRunnable = new Runnable() {
			@Override
			public void run() {
				Log.i("service", "xxx Timer tick");
				mDb.getUsersLocations(mGroupId, mEventId, mCallback);
				mHandler.postDelayed(this, mTimeout);
			}
		};

	}

	
	
	//Metoda zatrzymuj¹ca Timer
	public void StopTimer(){
		Log.i("service", "xxx TIMER STOPPED");
		mHandler.removeCallbacks(mRunnable);
	}
	
	public void StartTimer()
	{
		Log.i("service", "xxx TIMER STARTED");
		mHandler.postDelayed(mRunnable, mTimeout);
	}
	
	public void ChangeParameters(String groupId, String eventId)
	{
		mGroupId = groupId;
		mEventId = eventId;
	}
	

	
}
