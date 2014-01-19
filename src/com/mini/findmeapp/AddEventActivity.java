package com.mini.findmeapp;

import java.util.Calendar;
import java.util.GregorianCalendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Events;

public class AddEventActivity extends FragmentActivity {
	
	private DatabaseProxy databaseProxy;
	private Events event = new Events();
	
	public enum ACTIVE_DIALOG {
	    START_DATE, END_DATE, START_TIME, END_TIME
	}
	
	public ACTIVE_DIALOG activeDialog;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_event);
		databaseProxy = new DatabaseProxy(this);
		
		setupActionBar();
		findViewById(R.id.startDate).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
					activeDialog = ACTIVE_DIALOG.START_DATE;
					showDatePicker();
				}
				return false;
			}
		});
		findViewById(R.id.endDate).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
					activeDialog = ACTIVE_DIALOG.END_DATE;
					showDatePicker();
				}
				return false;
			}
		});
		findViewById(R.id.startHour).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
					activeDialog = ACTIVE_DIALOG.START_TIME;
					showTimePicker();
				}
				return false;
			}
		});
		findViewById(R.id.endHour).setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					mgr.hideSoftInputFromWindow(v.getWindowToken(), 0);
					activeDialog = ACTIVE_DIALOG.END_TIME;
					showTimePicker();
				}
				return false;
			}
		});
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
				EditText startDateText = (EditText)findViewById(R.id.startDate);
				startDateText.setText(composeDateString(date));
				event.startDate = date.getTime();
			}
			else {
				EditText endEditText = (EditText)findViewById(R.id.endDate);
				endEditText.setText(composeDateString(date));
				event.endDate = date.getTime();
				
			}
		}
	};
	
	OnTimeSetListener onTime = new OnTimeSetListener() {
		
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			if (activeDialog == ACTIVE_DIALOG.START_TIME) {
				EditText startDateText = (EditText)findViewById(R.id.startHour);
				startDateText.setText(hourOfDay + ":" + minute);
			}
			else {
				EditText endEditText = (EditText)findViewById(R.id.endHour);
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
