package com.mini.findmeapp.JoinGroup;

import com.mini.findmeapp.R;
import com.mini.findmeapp.R.id;
import com.mini.findmeapp.R.layout;
import com.mini.findmeapp.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.support.v4.app.NavUtils;

public class AddUserToGroupMainActivity extends Activity {

	protected static final int REQUEST_CODE = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_user_to_group_main);
		// Show the Up button in the action bar.
		setupActionBar();
		
		//Ustawienie onClick listenerów
		findViewById(R.id.joinPrivateButton).setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new  Intent(AddUserToGroupMainActivity.this, JoinPrivateGroupActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		}
		);
		findViewById(R.id.joinPublicButton).setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new  Intent(AddUserToGroupMainActivity.this, JoinPublicGroupActivity.class);
				startActivityForResult(intent, REQUEST_CODE);
			}
		}
		);
		
		
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
		getMenuInflater().inflate(R.menu.add_user_to_group_main, menu);
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
	
	@Override

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		this.finish();
	}

}
