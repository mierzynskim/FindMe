package com.mini.findmeapp;

import java.util.ArrayList;
import java.util.List;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.microsoft.windowsazure.mobileservices.TableQueryCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Groups;
import com.mini.findmeapp.AzureConnection.UsersGroups;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class JoinPublicGroupActivity extends Activity {

	private DatabaseProxy mDb;
	private JoinPublicGroupActivity self;
	private ArrayList<Groups> mPublicGroups;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_public_group);
		self = this;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_public_group, menu);

		//Utworzenie DatabaseProxy
		mDb = new DatabaseProxy(this);
		
		//Wczytanie z bazy grup publicznych
		mDb.getPublicGroups(new TableQueryCallback<Groups>() {
			
			@Override
			public void onCompleted(List<Groups> arg0, int arg1, Exception arg2,
					ServiceFilterResponse arg3) {
				if(arg2 == null)
				{
					mPublicGroups = new ArrayList<Groups>(arg0);
					ListView listView = (ListView)findViewById(R.id.publicGroupsList);
					ArrayAdapter<Groups> adapter = new ArrayAdapter<Groups>(self, android.R.layout.simple_list_item_1, mPublicGroups);
					listView.setAdapter(adapter);
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							Groups group = mPublicGroups.get(arg2);
							mDb.addUserToGroup(LoginActivity.user.getId(), group.Id, group.password, new TableOperationCallback<UsersGroups>() {
								
								@Override
								public void onCompleted(UsersGroups arg0, Exception arg1,
										ServiceFilterResponse arg2) {
									
									Context context = getApplicationContext();
									CharSequence text;
									int duration = Toast.LENGTH_SHORT;
									
									if(arg1 == null )
									{
										//Dodanie u¿ytkownika do grupy
										Log.i("service", "xxx Add user to private group " + arg0.groupId.toString());
										SessionData sd = new SessionData(getSharedPreferences(SessionData.FILE_NAME, 0));
										sd.setGroupId(arg0.groupId);
										
										text = "You joined group";
									}
									else
									{
										text = "You did not joined group. Try again";
										Intent intent = new Intent(getApplicationContext(), JoinPublicGroupActivity.class);
										startActivity(intent);
									}
									Toast toast = Toast.makeText(context, text, duration);
									toast.show();
								}
							});
							
							Context context = getApplicationContext();
							CharSequence text = "Wait for confirmation";
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
							
							self.finish();
						}
					});
				}
			}
		});
		return true;
	}

}
