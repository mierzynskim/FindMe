
package com.mini.findmeapp;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;
import com.mini.findmeapp.AzureConnection.DatabaseProxy;
import com.mini.findmeapp.AzureConnection.Users;
import com.mini.findmeapp.AzureConnection.UsersGroups;

public class LoginActivity extends FragmentActivity {
	public static final String USER_INFO = "UserInfo";

    private UiLifecycleHelper uiHelper;
	public static GraphUser user;
	private String userId;
	private ProgressBar mProgress;

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }

    };
    
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		final Session tmpSession = session;
		
        if (tmpSession != null && tmpSession.isOpened()) {
        	
            Request.newMeRequest(tmpSession, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    if (tmpSession == Session.getActiveSession()) {
                        if (user != null) {
                        	LoginActivity.user = user;
                        	if (userId == null) { 
                        		addNewUser();
                        	}
                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                  	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                	MainActivity.wasChange = true;
                                	Log.i("service", "MAIN ACTIVITY STARTING");
                                	startActivity(intent);
                                	finish();
                                }
                            }).start();
  
                        }   
                    }   
                }   
            }).executeAsync();
        	
        }   
	}
	
	private void addNewUser() {
		DatabaseProxy db = new DatabaseProxy(this);
		
		db.addUser(user.getId(), user.getProperty("email").toString(), new TableOperationCallback<Users>() 
				{			
			@Override
			public void onCompleted(Users arg0, Exception arg1,
					ServiceFilterResponse arg2) {
				if(arg1 == null)
				{
					Log.i("service", "xxx USER ADD OK");
				}
				else
					Log.i("service", "xxx USER ADD NIE OK " + arg1.getMessage());		
			}
		});
	}



    @Override
    public void onCreate(Bundle savedInstanceState) {
    	SharedPreferences settings = getSharedPreferences(USER_INFO, MODE_PRIVATE);
    	userId = settings.getString("userId", null);
    	
    	
        super.onCreate(savedInstanceState);
        
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        LoginButton authButton = (LoginButton)findViewById(R.id.authButton);
        if (userId == null)
        	authButton.setVisibility(View.VISIBLE);
        else {
        	mProgress = (ProgressBar) findViewById(R.id.progressBar);
        	mProgress.setVisibility(View.VISIBLE);
		}
        
        
		authButton.setReadPermissions(Arrays.asList("email"));

    }
    
    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();

        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data, null);
        
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
        Log.i("service", "LOGIN ACTIVITY DESTROY");
        MainActivity.wasChange = true;
    }
}
