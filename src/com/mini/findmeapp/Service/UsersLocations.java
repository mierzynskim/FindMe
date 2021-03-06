package com.mini.findmeapp.Service;

//Klasa trzymająca rekordy z tabeli UsersLocations
public class UsersLocations {
	
	//Identyfikator rekordu
	public String Id;
	
	//Identyfikator użytkownika
	public String userId;
	
	//Identyfikator aktualnego Eventu
	public String eventId;
	
	//Identyfikator aktualnej grupy
	public String groupId;
	
	//Długość geograficzna
	public double userLatitude;
	
	//Szerokość geograficzna
	public double userLongitude;
	
	//Opis do wyświetlenia na mapie
	public String caption;
}
