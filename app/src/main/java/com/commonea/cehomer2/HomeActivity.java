package com.commonea.cehomer2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;


public class HomeActivity extends ActionBarActivity implements View.OnClickListener {
    private ImageButton rooms_button;
    private ImageButton modes_button;
    private ImageButton lights_button;
    private ImageButton blinds_button;
    private ImageButton fans_button;
    private ImageButton ac_button;
    private ImageButton security_button;
    private ImageButton others_button;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rooms_button = (ImageButton)findViewById(R.id.id_rooms);
        rooms_button.setOnClickListener(this);
        modes_button = (ImageButton)findViewById(R.id.id_modes);
        modes_button.setOnClickListener(this);
        lights_button = (ImageButton)findViewById(R.id.id_lights);
        lights_button.setOnClickListener(this);
        blinds_button = (ImageButton)findViewById(R.id.id_blinds);
        blinds_button.setOnClickListener(this);
        fans_button = (ImageButton)findViewById(R.id.id_fans);
        fans_button.setOnClickListener(this);
        ac_button = (ImageButton)findViewById(R.id.id_ac);
        ac_button.setOnClickListener(this);
        security_button = (ImageButton)findViewById(R.id.id_security);
        security_button.setOnClickListener(this);
        others_button = (ImageButton)findViewById(R.id.id_others);
        others_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.id_rooms:
                Intent rooms = new Intent(HomeActivity.this, RoomsActivity.class);
                finish();
                startActivity(rooms);
                break;
            case R.id.id_modes:
                Intent modes = new Intent(HomeActivity.this, ModesActivity.class);
                finish();
                startActivity(modes);
                break;
            case R.id.id_lights:

                lights_button.setVisibility(v.GONE);
                /*Intent lights = new Intent(HomeActivity.this, LightsActivity.class);
                finish();
                startActivity(lights);*/
                break;
            case R.id.id_blinds:
                blinds_button.setVisibility(v.GONE);
                /*Intent blinds = new Intent(HomeActivity.this, BlindsActivity.class);
                finish();
                startActivity(blinds);*/
                break;
            case R.id.id_fans:
                Intent fans = new Intent(HomeActivity.this, FansActivity.class);
                finish();
                startActivity(fans);
                break;
            case R.id.id_ac:
                Intent ac = new Intent(HomeActivity.this, AcActivity.class);
                finish();
                startActivity(ac);
                break;
            case R.id.id_security:
                Intent security = new Intent(HomeActivity.this, SecurityActivity.class);
                finish();
                startActivity(security);
                break;
            case R.id.id_others:
                Intent others = new Intent(HomeActivity.this, OthersActivity.class);
                finish();
                startActivity(others);
                break;
            default:
                break;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        pDialog = new ProgressDialog(HomeActivity.this);
        pDialog.setMessage("Logging Out...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);
        pDialog.show();
        StoredData.setUsername(this, "");
        StoredData.setPassword(this, "");
        Intent lo = new Intent(HomeActivity.this, LoginActivity.class);
        pDialog.dismiss();
        finish();
        startActivity(lo);

    }
}
