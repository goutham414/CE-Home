package com.commonea.cehomer2;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.commonea.cehomer2.util.SystemUiHider;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends Activity {
    private static final boolean AUTO_HIDE = true;
    private static final int AUTO_HIDE_DELAY_MILLIS = 1000;
    String CheckURL = "http://49.207.56.232:83/cea/login_post.php";
    int taskExists;
    String cUsername;
    String cPassword;

    private AsyncTask<String, String, String> mAsyncTask;
    JSON_Praser dataPraser = new JSON_Praser();
    private static final String TAG_LOGGED = "logged";
    int logged;
    String timingOut = "";
    int networkFailure = 0;
    int JSON_Error = 0;

    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;
    private SystemUiHider mSystemUiHider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        final View contentView = findViewById(R.id.splash_content);

        // Set up an instance of SystemUiHider to control the system UI for
        // this activity.
        mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
        mSystemUiHider.setup();
        mSystemUiHider
                .setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {

                    @Override
                    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
                    public void onVisibilityChange(boolean visible) {

                        if (visible && AUTO_HIDE) {
                            // Schedule a hide().
                            delayedHide(AUTO_HIDE_DELAY_MILLIS);
                        }
                    }
                });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        delayedHide(100);
        cUsername = StoredData.getUsername(this);
        cPassword = StoredData.getPassword(this);
        Log.d("cUsername", cUsername);
        Log.d("cPassword", cPassword);
        timingOut = "1";
        if(cUsername !=null && cPassword != null) {
            loginCheck();
            try {
                sleep(5000);
            }catch (Exception e){

            }
        }

    }

    private String loginCheck() {
        mAsyncTask = new AsyncTask<String, String, String>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }
            @Override
            protected String doInBackground(String... params) {
                try{
                    taskExists = 1;
                    JSONObject jsonData = dataPraser.JSON_Post(CheckURL, "", "", cUsername, cPassword);
                    logged = jsonData.getInt(TAG_LOGGED);
                    timingOut = "0";
                    Log.d("Reply", "Received");

                    Log.d("MessageLogged", String.valueOf(logged));
                    if(logged == 1){
                        //Log.d("Success", jsonData.toString());
                        Intent home = new Intent(SplashActivity.this, HomeActivity.class);
                        finish();
                        startActivity(home);
                    }
                    else{
                        Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                        finish();
                        startActivity(login);
                    }
                }
                catch(JSONException e){
                    e.printStackTrace();
                }
                catch (Exception e) {
                    timingOut = "0";
                    networkFailure = 1;
                }
                return null;
            }
            protected void onPostExecute(String file_url) {
                // dismiss the dialog once product deleted
                taskExists = 0;
                if(networkFailure == 1){
                    Toast.makeText(SplashActivity.this, "Unable to reach Server.!", Toast.LENGTH_LONG).show();
                }
            }
        }.execute();
        return null;
    }

    //Timer code.
    public Void sleep(final int sleepTime){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run()
            {
                Log.d("SleepFunc", "Entered");
                if(timingOut.equals("1")){
                    Log.d("Timer", "Timed_Out");
                    if(taskExists == 1) {
                        mAsyncTask.cancel(true);
                        Log.d("DB1", "Cancelling");
                        if (mAsyncTask.isCancelled()) {
                            Log.d("DB2", "Cancelled");
                            Toast.makeText(SplashActivity.this, "Unable to reach Server.!", Toast.LENGTH_LONG).show();
                            timingOut = "0";
                            Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                            finish();
                            startActivity(login);
                        }
                    }
                    else{
                        Toast.makeText(SplashActivity.this, "Task doesn't exist.!", Toast.LENGTH_LONG).show();
                        timingOut = "0";
                        Intent login = new Intent(SplashActivity.this, LoginActivity.class);
                        finish();
                        startActivity(login);
                    }
                }
                else if(timingOut.equals("0")){
                    Log.d("Timer", "Not_Timed_Out");
                }
                else
                {
                    Log.d("Timer", "Something_is_wrong");
                }
            }
        }, sleepTime);
        Log.d("Sleep", "Exiting");
        return null;
    }


    Handler mHideHandler = new Handler();
    Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            mSystemUiHider.hide();
        }
    };

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
}
