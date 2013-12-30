package com.mini.findmeapp.AzureConnection;

//Klasa reprezentuj¹ca rekordy tabeli Users
public class Users {
	
	public static final Integer MAX_GROUPS = 5;
	public static final Integer MAX_EVENTS = 5;
	
	
	//Identyfikator U¿ytkownika w bazie
	public String Id;
	
	//Identyfikator U¿ytkownika pobrany z facebooka
	public String facebookId;
	
	//Licznik grup u¿ytkownika
	public Integer groupCounter;
	
	//Licznik eventów
	public Integer eventCounter;
	
	//Adres e-mail u¿ytkownika
	public String emailAddress;
}
