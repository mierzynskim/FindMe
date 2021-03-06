package com.mini.findmeapp;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Events;
import com.mini.findmeapp.AzureConnection.GroupsEvents;

public class AddEventActivity extends FragmentActivity {
	
	private DatabaseProxy databaseProxy;
	private Events event = new Events();
	
	//Klasa trzyma dane sesji i zapisuje je do sharedpreferences
	public SessionData mSessionData;
	
	public enum ACTIVE_DIALOG {
	    START_DATE, END_DATE, START_TIME, END_TIME
	}
	
	public ACTIVE_DIALOG activeDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		databaseProxy = new DatabaseProxy(this);
		mSessionData = new SessionData(getSharedPreferences(SessionData.FILE_NAME, 0));
		event.startDate = event.endDate = new Date(0);
		
		setupActionBar();
		findViewById(R.id.startDate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
					activeDialog = ACTIVE_DIALOG.START_DATE;
					showDatePicker();
			}
		});
		findViewById(R.id.endDate).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activeDialog = ACTIVE_DIALOG.END_DATE;
				showDatePicker();
			}
		});
		findViewById(R.id.startHour).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activeDialog = ACTIVE_DIALOG.START_TIME;
				showTimePicker();
			}
		});
		findViewById(R.id.endHour).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				activeDialog = ACTIVE_DIALOG.END_TIME;
				showTimePicker();
			}
		});
		
		findViewById(R.id.eventLocation).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AddEventActivity.this, PickLocationActivity.class);
				AddEventActivity.this.startActivityForResult(intent, 11);
			}
		});

        final Button button = (Button) findViewById(R.id.submitEventButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	EditText eventName = (EditText) findViewById(R.id.eventName);
            	EditText eventDescribtion = (EditText) findViewById(R.id.eventDesctibtion);
            	EditText eventUrl = (EditText) findViewById(R.id.eventUrl);
            	
            	
        		databaseProxy.addEvent(mSessionData.getGroupId(), eventName.getText().toString(),
        				eventDescribtion.getText().toString(), event.locationLatitude, event.locationLongitude,
        				eventUrl.getText().toString(), event.startDate, event.endDate,
        				new TableOperationCallback<GroupsEvents>() {
					
					@Override
					public void onCompleted(GroupsEvents arg0, Exception arg1,
							ServiceFilterResponse arg2) {
						if(arg1 == null) {
							Log.i("service", "Dodano Event o ID " + arg0.eventId);
							mSessionData.setEventId(arg0.eventId);
							
							MainActivity.wasChange = true;
							
							Context context = getApplicationContext();
							CharSequence text = "Event successfully added";
							int duration = Toast.LENGTH_SHORT;

							Toast toast = Toast.makeText(context, text, duration);
							toast.show();
							
							AddEventActivity.this.finish();
							
						}
						
					}
				});
            }
        });
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == 11) {
		     if(resultCode == RESULT_OK) {      
		    	 Bundle extras = data.getExtras();
		    	 event.locationLatitude = extras.getDouble("latitude");
		    	 event.locationLongitude = extras.getDouble("longitude");  
		    	 Button button = (Button) findViewById(R.id.eventLocation);
		    	 button.setText(String.valueOf(event.locationLatitude) + " : " + String.valueOf(event.locationLongitude));
		     }

		  }
		  else {
			  Log.i("null", "NULL");
		  }
		  
	}
		

	private void setupActionBar() {
		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	private void showDatePicker() {
		DatePickerFragment date = new DatePickerFragment();
		Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("year", calender.get(Calendar.YEAR));
		args.putInt("month", calender.get(Calendar.MONTH));
		args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
		date.setArguments(args);

		date.setCallBack(ondate);
		date.show(getSupportFragmentManager(), "Date Picker");
	}
	
	private void showTimePicker() {
		TimePickerFragment date = new TimePickerFragment();
		Calendar calender = Calendar.getInstance();
		Bundle args = new Bundle();
		args.putInt("hour", calender.get(Calendar.HOUR_OF_DAY));
		args.putInt("minute", calender.get(Calendar.MINUTE));
		date.setArguments(args);

		date.setCallBack(onTime);
		date.show(getSupportFragmentManager(), "Time Picker");
	}

	OnDateSetListener ondate = new OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			Calendar date = new  GregorianCalendar(year, monthOfYear, dayOfMonth);
			if (activeDialog == ACTIVE_DIALOG.START_DATE) {
				Button startDateText = (Button)findViewById(R.id.startDate);
				startDateText.setText(composeDateString(date));
				event.startDate = date.getTime();
			}
			else {
				Button endEditText = (Button)findViewById(R.id.endDate);
				endEditText.setText(composeDateString(date));
				event.endDate = date.getTime();
				
			}
		}
	};
	
	OnTimeSetListener onTime = new OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (activeDialog == ACTIVE_DIALOG.START_TIME) {
				Button startDateText = (Button)findViewById(R.id.startHour);
				startDateText.setText(hourOfDay + ":" + minute);
			}
			else {
				Button endEditText = (Button)findViewById(R.id.endHour);
				endEditText.setText(hourOfDay + ":" + minute);
			}
			
		}
	};

	
	private String composeDateString(Calendar date) {
		return new StringBuilder()
        .append(date.get(Calendar.MONTH) + 1).append("-")
        .append(date.get(Calendar.DAY_OF_MONTH)).append("-")
        .append(date.get(Calendar.YEAR)).append(" ").toString();
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.add_group, menu);
		return true;
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
