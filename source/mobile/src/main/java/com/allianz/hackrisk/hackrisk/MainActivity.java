package com.allianz.hackrisk.hackrisk;

import android.app.Activity;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;


public class MainActivity extends Activity implements ListView.OnItemClickListener, ILocationUpdateHandler {


    private Location mLastLocation;

    private MyGoogleAPIClient myApiClient;

    private TextView myLocation;

    NavigationDrawerHelper mNavigationDrawerHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationDrawerHelper = new NavigationDrawerHelper();

        setContentView(R.layout.activity_main);
        myLocation = (TextView) findViewById(R.id.myLocation);

        mNavigationDrawerHelper.init(this, this);

        // First we need to check availability of play services

        myApiClient =  new MyGoogleAPIClient(this.getApplicationContext(), MainActivity.class.getSimpleName(), this);

        if (myApiClient.checkPlayServices()) {

            // Building the GoogleApi client
            myApiClient.buildGoogleApiClient();

            myApiClient.createLocationRequest();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int optionLib, long l) {
        mNavigationDrawerHelper.handleSelect(optionLib, this, "", 123);
    }


    @Override
    protected void onStart() {
        super.onStart();
        if ( myApiClient.GetGoogleAPIClient() != null) {
            myApiClient.GetGoogleAPIClient().connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        myApiClient.checkPlayServices();

        // Resuming the periodic location updates
        if (myApiClient.GetGoogleAPIClient().isConnected() && myApiClient.IsLocationUpdate()) {
            myApiClient.StartLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (myApiClient.GetGoogleAPIClient().isConnected()) {
            myApiClient.GetGoogleAPIClient().disconnect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        myApiClient.StopLocationUpdates();
    }

    /**
     * Method to display the location on UI
     * */
    @Override
    public void HandleUpdate() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(myApiClient.GetGoogleAPIClient());

        if (mLastLocation != null) {

            Geocoder gc = new Geocoder(getApplicationContext());

            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            try {
                Address currentAddress =  gc.getFromLocation(latitude, longitude, 1).get(0);

                String locationLabel = getResources().getString(R.string.myLocation);
                 myLocation.setText(locationLabel + ": " + currentAddress.getLocality() + ",  "+ currentAddress.getPostalCode());
            }
            catch(Exception ex)
            {

            }



        } else {

            myLocation
                    .setText("(Couldn't get the location. Make sure location is enabled on the device)");
        }
    }



}