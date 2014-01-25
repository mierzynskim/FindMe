
package com.mini.findmeapp;

import java.util.Arrays;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

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
	protected static GraphUser user;
	private String userId; 

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

                        	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        	startActivity(intent);
                        	finish();
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
		db.addUserToGroup(user.getId(), "F9898B0B-DF9E-4438-B8C8-3B4269F6E491" , "tajne2", new TableOperationCallback<UsersGroups>() {
        	
			@Override
			public void onCompleted(UsersGroups arg0, Exception arg1,
					ServiceFilterResponse arg2) {
				// TODO Auto-generated method stub
				if(arg1 == null)
					Log.i("service", "User dodany do grupy");
				else
					Log.i("service", "User NIE dodany do grupy");				
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
    }
}
