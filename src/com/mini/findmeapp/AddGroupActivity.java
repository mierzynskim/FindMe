package com.mini.findmeapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Groups;

public class AddGroupActivity extends Activity {
	
	private DatabaseProxy databaseProxy ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_group);
		setupActionBar();
		databaseProxy = new DatabaseProxy(this);
        final Button button = (Button) findViewById(R.id.submitGroupButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	EditText groupName = (EditText) findViewById(R.id.groupName);
            	EditText groupPassword = (EditText) findViewById(R.id.groupPassword);
            	EditText groupDescribtion = (EditText) findViewById(R.id.groupDescribtion);
            	CheckBox isPrivateBox = (CheckBox) findViewById(R.id.isPrivate);
            	databaseProxy.addGroup(LoginActivity.user.getId(), groupName.getText().toString(), groupDescribtion.getText().toString(), 
            			groupPassword.getText().toString(), isPrivateBox.isChecked(), new TableOperationCallback<Groups>() {
    			@Override
    			public void onCompleted(Groups arg0, Exception arg1,
    					ServiceFilterResponse arg2) {
    				if(arg1 == null)
    				{
    					Log.i("service", "xxx GROUP ADD OK");
    					MainActivity.mGroupId = arg0.Id;
    					AddGroupActivity.this.finish();
    					//mGroupId = arg0.Id;
    				}
    				else
    					Log.i("service", "xxx GROUP NIE ADD OK");
    			}
    		});
            }
        });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add_new_group, menu);
		return true;
	}
	
	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
