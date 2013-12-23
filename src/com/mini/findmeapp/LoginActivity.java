package com.mini.findmeapp;


import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.facebook.FacebookException;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.facebook.widget.LoginButton.OnErrorListener;

public class LoginActivity extends Activity {
	private String TAG = "LoginActivity";
	public final static String USER_ID = "com.example.myfirstapp.USERID";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);

		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		authButton.setOnErrorListener(new OnErrorListener() {
			@Override
			public void onError(FacebookException error) {
				Log.i(TAG, "Error " + error.getMessage());
			}
		});
		
		authButton.setReadPermissions(Arrays.asList("basic_info","email"));
		authButton.setSessionStatusCallback(new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {

				if (session.isOpened()) {
					Log.i(TAG,"Access Token"+ session.getAccessToken());
					Request.newMeRequest(session, new Request.GraphUserCallback() {
						// callback after Graph API response with user object
						@Override
						public void onCompleted(GraphUser user, Response response) {
							if (user != null) {
								Log.i(TAG,"User ID "+ user.getId());
								Log.i(TAG,"Email "+ user.asMap().get("email"));
								Intent intent = new Intent(LoginActivity.this, MainActivity.class);
								intent.putExtra(USER_ID, user.getId());
								startActivity(intent);
							}
						}
					}).executeAsync();
				}

			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}
