package com.mini.findmeapp.AzureConnection;

//Klasa reprezentuje Grupy
public class Groups {

	//Identyfikator grupy
	public String Id;
	
	//Identyfikator w�a�ciciela grupy
	public String ownerId;
	
	//Has�o dla grupy
	public String password;
	
	//Czy grupa jest prywatna
	public Boolean isPrivate;
	
	//Opis grupy
	public String description;
	
	//Nazwa grupy
	public String name;
	
	@Override
	public String toString() {
		return name;
	}
	
}
