
package com.mini.findmeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.facebook.AppEventsLogger;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginActivity extends FragmentActivity {

    public static String USER_ID;

    private UiLifecycleHelper uiHelper;

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
                        	LoginActivity.this.user = user;
                        	Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        	intent.putExtra(USER_ID, user.getId());
                        	startActivity(intent);
                        	finish();
                        }   
                    }   
                }   
            }).executeAsync();
        	
        }   
	}

	private LoginButton loginButton;
	protected GraphUser user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);

        loginButton = (LoginButton) findViewById(R.id.authButton);
        loginButton.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                LoginActivity.this.user = user;
            }
        });
        
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
