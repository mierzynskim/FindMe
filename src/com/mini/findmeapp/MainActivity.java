package com.mini.findmeapp;

import java.net.MalformedURLException;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.MobileServiceAuthenticationProvider;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceTable;
import com.microsoft.windowsazure.mobileservices.MobileServiceUser;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.UserAuthenticationCallback;


public class MainActivity extends Activity {
	
	private MobileServiceClient mClient;
	private String mUserId = "abc";
	
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mGroupNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
		try {
			mClient = new MobileServiceClient( "https://findmeservice.azure-mobile.net/", "OLKfbhinUWxodQYJemjqlykJWzpizN37", this);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		authenticate();

		
		//ListView listView = (ListView) findViewById(R.id.myList);
		//MobileServiceTable<ToDoItem> mToDoTable = mClient.getTable(ToDoItem.class);
		
		//MobileServiceJsonTable values =  mClient.getTable("ToDoItem");
		//mToDoTable.execute(new TableQueryCallback<ToDoItem>() {
            //public void onCompleted(List<ToDoItem> result, int count,
                //Exception exception, ServiceFilterResponse response) {
                //if (exception == null) {
                    //for (ToDoItem item : result) {
                        //Log.i("FindMeLOG", "Read object with ID " + item.getText());  
                    //}
                //}
            //}
        //});

        mTitle = mDrawerTitle = getTitle();
        mGroupNames = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mGroupNames));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);


        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  
                mDrawerLayout,         
                R.drawable.ic_drawer,  
                R.string.drawer_open,  
                R.string.drawer_close
                ) {
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); 
            }

            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mDrawerTitle);
                invalidateOptionsMenu(); 
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

//        if (savedInstanceState == null) {
//            selectItem(0);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch(item.getItemId()) {
        case R.id.action_websearch:
            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
            intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
            }
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        //args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mPlanetTitles[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

 
	
	public void AddToDoItem(View view){
		Log.i("FindMeLOG", "AddToDoITem");
		
		ToDoItem newItem = new ToDoItem();
		newItem.setText("Another item from app by user" + mUserId);
		
		MobileServiceTable<ToDoItem> mToDoTable = mClient.getTable(ToDoItem.class);
		
		mToDoTable.insert(newItem, new TableOperationCallback<ToDoItem>(){
			public void onCompleted(ToDoItem entity, Exception exception, ServiceFilterResponse response){
				if( exception == null){
				
					Log.i("FindMeLOG", "Inserted item with id" + entity.getId());
				}
				else
				{
					Log.i("FindMeLOG", "ERROR");
				}
				
			}
			
			
		});
		
	}
	
	private void authenticate() {

		mClient.login(MobileServiceAuthenticationProvider.Facebook,
		        new UserAuthenticationCallback() {


		            @Override
		            public void onCompleted(MobileServiceUser user,
		                    Exception exception, ServiceFilterResponse response) {


		                if (exception == null) {
		                    Log.i("FindMeLOG",
		                                    "You are now logged in - %1$2s" +
		                                    user.getUserId() + "Success");
		                    mUserId = user.getUserId();
		                  
		                } else {
		                    Log.i("FindMeLOG","You must log in. Login Required" + "Error");
		                }
		            }
		        });


		}

}



