package com.commonea.cehomer2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private UserLoginTask mAuthTask = null;
    int taskExist = 1;
    // UI references.
    private EditText mUsernameView;
    private EditText mPasswordView;

    ProgressDialog pDialog;
    JSON_Praser dataPraser = new JSON_Praser();
    String loginURL = "http://49.207.56.232:83/cea/login_post.php";
    private AsyncTask<String, String, String> mAsyncTask;
    private static final String TAG_LOGGED = "logged";
    int logged;
    String timingOut = "";
    int networkFailure = 0;
    int JSON_Error = 0;
    int pending = 0;
    private String mUsername;
    private String mPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mUsernameView = (EditText) findViewById(R.id.fld_username);
        mPasswordView = (EditText) findViewById(R.id.fld_password);

        Button mLoginButton = (Button) findViewById(R.id.btn_login);
        mLoginButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(pending == 0) {
                    timingOut = "1";
                    attemptLogin();
                    Log.d("step0", "ok");
                    try {
                        sleep(5000);
                    }catch (Exception e){
                        Log.d("Sleep", "exception");
                    }

                }
                else{
                    Toast.makeText(LoginActivity.this, "Please wait for a moment and retry...", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void attemptLogin() {

        Log.d("step1", "ok");
        // Reset errors.
        mUsernameView.setError(null);
        mPasswordView.setError(null);

        Log.d("step2", "ok");
        // Store values at the time of the login attempt.
        String username = mUsernameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid username.
        if (TextUtils.isEmpty(username)) {
            Log.d("username", "empty");
            mUsernameView.setError(getString(R.string.error_field_required));
            focusView = mUsernameView;
            cancel = true;
        } else if (!isUsernameValid(username)) {

            Log.d("username", "invalid");
            mUsernameView.setError(getString(R.string.error_invalid_username));
            focusView = mUsernameView;
            cancel = true;
        }
        Log.d("step3", "ok");
        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            Log.d("password", "empty");
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            Log.d("password", "invalid");
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }
        Log.d("step4", "ok");
        if (cancel) {
            Log.d("step5", "ok");
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // perform the user login attempt.
            Log.d("step5", "ok");
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
            Log.d("step6", "ok");
        }
    }
    public void setPrefs(){

        StoredData.setUsername(this, mUsername);
        StoredData.setPassword(this, mPassword);
    }

    private boolean isUsernameValid(String username) {
        //TODO: Replace this with your own logic
        return username.length() > 3;
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }
    /**
     * Represents an asynchronous login task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        UserLoginTask(String email, String password) {
            Log.d("step7", "ok");
            mUsername = email;
            mPassword = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(LoginActivity.this);
            pDialog.setMessage("Attempting to login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            try{
                taskExist = 1;
                JSONObject jsonData = dataPraser.JSON_Post(loginURL, "", "", mUsername, mPassword);
                logged = jsonData.getInt(TAG_LOGGED);
                timingOut = "0";
                Log.d("Reply", "Received");
            }
            catch(JSONException e){
                e.printStackTrace();
                timingOut = "0";
                JSON_Error = 1;
            }
            catch (Exception e) {
                timingOut = "0";
                networkFailure = 1;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            pDialog.dismiss();
            if (logged == 1){
                setPrefs();
                Intent i = new Intent(LoginActivity.this, HomeActivity.class);
                pending = 0;
                finish();
                startActivity(i);
            }
            else if (logged == 0 && JSON_Error != 1 && networkFailure != 1){
                mUsernameView.setText("");
                mPasswordView.setText("");
                mUsernameView.setError(getString(R.string.error_incorrect_credentials));
                mPasswordView.setError(getString(R.string.error_incorrect_credentials));
                mUsernameView.requestFocus();
            }
            if (networkFailure == 1){
                Toast.makeText(LoginActivity.this, "Unable to reach Server.!", Toast.LENGTH_LONG).show();
            }
            else if(JSON_Error == 1){
                Toast.makeText(LoginActivity.this, "JAVA Error, Contact developer.!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected void onCancelled() {

        }
    }


    //Timer code.
    public Void sleep(final int sleepTime){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run()
            {
                Log.d("SleepFunc", "Entered");
                pending = 0;
                if(timingOut.equals("1")){
                    Log.d("Timer", "Timed_Out");
                    try {
                        mAsyncTask.cancel(true);
                    }
                    catch (Exception e){
                        taskExist = 0;
                        Log.d("mAsyncTask", "Unable to stop Task.!");
                    }
                    Log.d("DB1", "Cancelling");
                    if(taskExist == 1) {
                        if (mAsyncTask.isCancelled()) {
                            Log.d("DB2", "Cancelled");
                            pDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Unable to reach Server.!", Toast.LENGTH_LONG).show();
                            timingOut = "0";
                        }
                    }
                    else{
                        Toast.makeText(LoginActivity.this, "No task exists.!", Toast.LENGTH_LONG).show();
                        timingOut = "0";
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
}



