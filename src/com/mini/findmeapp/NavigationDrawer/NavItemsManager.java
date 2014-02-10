package com.mini.findmeapp.NavigationDrawer;

import java.util.ArrayList;

import com.mini.findmeapp.MainActivity;
import com.mini.findmeapp.AzureConnection.Events;
import com.mini.findmeapp.AzureConnection.Groups;

public class NavItemsManager {
	
	//Lista grup u¿ytkownika
	private ArrayList<Groups> mGroupsList;
	
	//Lista, której elementami s¹ listy eventów dla danej grupy
	private ArrayList<ArrayList<Events>> mEventsArrayList;
	
	//MainActivity, na którym wszystko siê dzieje
	private MainActivity mMainActivity;
	
	public NavItemsManager(MainActivity mainActivity){
		
		mGroupsList = new ArrayList<Groups>();
		mEventsArrayList = new ArrayList<ArrayList<Events>>();
		mMainActivity = mainActivity;
	}
	
	public void addGroup(Groups newGroup)
	{
		if(mGroupsList.indexOf(newGroup) != -1 )
			return;
		
		mGroupsList.add(newGroup);
		mEventsArrayList.add(new ArrayList<Events>());
	}
	
	public void addEvent(Events newEvent, Groups group)
	{
		addGroup(group);
		
		int listIndex = mGroupsList.indexOf(group);
		
		if((mEventsArrayList.get(listIndex)).indexOf(newEvent) != -1)
			return;
		
		(mEventsArrayList.get(listIndex)).add(newEvent);
	}
	
	public Events getEventAt(Integer code)
	{
		int listIndex = ( code / 100 ) - 1;
		int itemIndex = ( code % 100 ) - 1;

		return mEventsArrayList.get(listIndex).get(itemIndex);
		
	}
	
	public Events getEventById(String eventId)
	{
		Events event = new Events();
		
		for( ArrayList<Events> list : mEventsArrayList)
		{
			for( Events item : list)
			{
				if(item.Id.equalsIgnoreCase(eventId))
					return item;
			}
		}
		
		
		return event;
	}
	
	public Groups getGroupAt(Integer code)
	{
		Integer groupIndex = ( code / 100 ) - 1;
		
		return mGroupsList.get(groupIndex);
	}
	
	
	public NavDrawerItem[] getItemsArray()
	{
		ArrayList<NavDrawerItem> itemsList = new ArrayList<NavDrawerItem>();
		
		Integer k = 0;
		//Dodawanie grup i eventów
		for(Groups group : mGroupsList)
		{
			Integer groupIndex = 100 + 100 * mGroupsList.indexOf(group);
			itemsList.add(NavMenuSection.create(groupIndex, group.name));
			
			Integer i = 1;
			for(Events event : mEventsArrayList.get(k))
			{
				itemsList.add(NavMenuItem.create(groupIndex+i, event.name, event.description,
						"ic_action_new_event", true, mMainActivity));
				++i;
			}
			
			++k;
		}
		
		
		//Dodawanie dolnej czêœci
		itemsList.add( NavMenuSection.create( 10, "Settings") );
		itemsList.add( NavMenuItem.create(20,"Add new group", "", "ic_action_new", false, mMainActivity) );
		itemsList.add( NavMenuItem.create(30,"Add event", "", "ic_action_add_group", false, mMainActivity) );
		itemsList.add( NavMenuItem.create(40,"Join existing group", "", "ic_action_add_group", false, mMainActivity) );
		itemsList.add( NavMenuItem.create(50,"App Settings", "", "ic_action_settings", false, mMainActivity) );
		itemsList.add( NavMenuItem.create(60,"Change caption", "", "ic_action_settings", false, mMainActivity) );
		itemsList.add( NavMenuItem.create(70,"Log out", "", "ic_action_settings", false, mMainActivity) );
		
		
		return itemsList.toArray(new NavDrawerItem[0]);
	}
	
	

}
