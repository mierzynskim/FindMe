package com.mini.findmeapp;

import android.content.Context;
import android.content.SharedPreferences;

//Klasa trzyma w sharedpreferencess informacje zwi¹zane z sesj¹ (aktualn¹ grupê, event, opis)
public class SessionData {
	
	public static final String FILE_NAME = "FINDMEAPPSESSION";
	private static final String CAPTION_KEY = "CAPTION";
	private static final String GROUP_KEY = "GROUP";
	private static final String EVENT_KEY = "EVENT";
	private static final String EVENT_NULL = "EVENT_NULL_ID";
	
	private final SharedPreferences mSharedPreferences;
	
	public SessionData(SharedPreferences sharedPreferences)
	{
		mSharedPreferences = sharedPreferences;
	}
	
	//Opis danego u¿ytkownika
	public String getCaption() 
	{
		return mSharedPreferences.getString(CAPTION_KEY, "Caption");
	}
	public void setCaption(String caption) 
	{
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(CAPTION_KEY, caption);
		editor.commit();
	}

	//Identyfikator aktualnej grupy
	public String getGroupId() 
	{
		return mSharedPreferences.getString(GROUP_KEY, "");
	}
	public void setGroupId(String groupId) 
	{
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(GROUP_KEY, groupId);
		editor.putString(EVENT_KEY, EVENT_NULL);
		editor.commit();
	}
	
	//Identyfikator aktualnego eventu
	public String getEventId() {
		return mSharedPreferences.getString(EVENT_KEY, "");
	}
	public void setEventId(String eventId) {
		SharedPreferences.Editor editor = mSharedPreferences.edit();
		editor.putString(EVENT_KEY, eventId);
		editor.commit();
	}
	
}
