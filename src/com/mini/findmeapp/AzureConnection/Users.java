package com.mini.findmeapp.AzureConnection;

//Klasa reprezentująca rekordy tabeli Users
public class Users {
	
	public static final Integer MAX_GROUPS = 5;
	public static final Integer MAX_EVENTS = 5;
	
	
	//Identyfikator Użytkownika w bazie
	public String Id;
	
	//Identyfikator Użytkownika pobrany z facebooka
	public String facebookId;
	
	//Licznik grup użytkownika
	public Integer groupCounter;
	
	//Licznik eventów
	public Integer eventCounter;
	
	//Adres e-mail użytkownika
	public String emailAddress;
}
