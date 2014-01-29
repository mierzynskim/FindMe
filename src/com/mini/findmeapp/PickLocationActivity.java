package com.mini.findmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PickLocationActivity extends FragmentActivity {
	private GoogleMap mMap;
	private LatLng location;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pick_location);
		getSupportFragmentManager().beginTransaction().replace(R.id.content_frame_pick, new GoogleMapFragment()).commit();
		setupActionBar();
		
		findViewById(R.id.submitLocationButton).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent returnIntent = new Intent();
				returnIntent.putExtra("latitude", location.latitude);
				returnIntent.putExtra("longitude", location.longitude);
				Log.i("tap", location.latitude + " " + location.longitude);
				setResult(RESULT_OK, returnIntent);  
				PickLocationActivity.this.finish();
			}
		});
	}
	
	@Override
	public void onStart() {
	    super.onStart();
    	MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
    	final GoogleMap mMap = mapFragment.getMap();

    	mMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng point) {
				mMap.clear();
				mMap.addMarker(new MarkerOptions().position(point));
				location = point;
				Log.i("tap", "Map tapped");
				
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
		getMenuInflater().inflate(R.menu.pick_location, menu);
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
