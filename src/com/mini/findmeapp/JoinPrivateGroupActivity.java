package com.mini.findmeapp;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.UsersGroups;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class JoinPrivateGroupActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_join_private_group);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Ustawienie onClick Listenera
		findViewById(R.id.submitJoinPrivateButton).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DatabaseProxy db = new DatabaseProxy(JoinPrivateGroupActivity.this);
				String groupName = ((EditText)findViewById(R.id.privateGroupName)).getText().toString();
				String groupPassword = ((EditText)findViewById(R.id.privateGroupPassword)).getText().toString();
				
				db.addUserToGroupByName(LoginActivity.user.getId(), groupName, groupPassword, new TableOperationCallback<UsersGroups>() {
					
					@Override
					public void onCompleted(UsersGroups arg0, Exception arg1,
							ServiceFilterResponse arg2) {
						
						Context context = getApplicationContext();
						CharSequence text;
						int duration = Toast.LENGTH_SHORT;
						
						if(arg1 == null || arg0 == null)
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
							Intent intent = new Intent(getApplicationContext(), JoinPrivateGroupActivity.class);
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
				
				JoinPrivateGroupActivity.this.finish();
			}
		});
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.join_private_group, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
