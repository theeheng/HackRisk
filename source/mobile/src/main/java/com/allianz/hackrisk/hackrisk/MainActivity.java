package com.allianz.hackrisk.hackrisk;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements ListView.OnItemClickListener, ILocationUpdateHandler, ICrimeUpdateHandler {


    private Location mLastLocation;

    private MyGoogleAPIClient myApiClient;

    private TextView myLocation;

    NavigationDrawerHelper mNavigationDrawerHelper;

    FrameLayout progressBarHolder;

    AlphaAnimation inAnimation;

    private Button searchRiskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationDrawerHelper = new NavigationDrawerHelper();

        setContentView(R.layout.activity_main);
        myLocation = (TextView) findViewById(R.id.myLocation);
 searchRiskButton = (Button) findViewById(R.id.SearchRiskButton);

        searchRiskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.handleSearchRiskButtonClick((Button) view);
            }
        });

        progressBarHolder = (FrameLayout) findViewById(R.id.progressBarHolder);

        // First we need to check availability of play services

        /*final Intent queryIntent = getIntent();

        myLocation.setText(queryIntent.getStringExtra(LoginActivity.EXTRA_LOCATION));

        Bundle bundle = queryIntent.getExtras();

        if (bundle != null && bundle.getBundle(LoginActivity.EXTRA_CRIMEAPIRESULT) != null) {
            bundle = bundle.getBundle(LoginActivity.EXTRA_CRIMEAPIRESULT);
            crimeAPIResult = (ArrayList<CrimeApiResult>) bundle.getSerializable(LoginActivity.EXTRA_CRIMEAPIRESULT);
        }
*/
        myApiClient =  new MyGoogleAPIClient(getApplicationContext(), "UserLoginTask", this);
            // Building the GoogleApi client

        if (myApiClient.checkPlayServices()) {

            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressBarHolder.setAnimation(inAnimation);
            progressBarHolder.setVisibility(View.VISIBLE);

            myApiClient.buildGoogleApiClient();

            myApiClient.createLocationRequest();
        }

        mNavigationDrawerHelper.init(this, this);

    }

    private void handleSearchRiskButtonClick(Button view) {

        Intent intent = new Intent(getApplicationContext(), SearchViewActivity.class);

        /*        Bundle b = new Bundle();
                b.putSerializable(LoginActivity.EXTRA_CRIMEAPIRESULT, crimeApiResults);

                intent.putExtra(LoginActivity.EXTRA_LOCATION, locationLabel);*/

        startActivity(intent);


    }

    /**
 * Method to display the location on UI
 * */

    public void HandleUpdate() {


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(myApiClient.GetGoogleAPIClient());

        if (mLastLocation != null) {

            Geocoder gc = new Geocoder(getApplicationContext());

            double latitude = mLastLocation.getLatitude();
            double longitude = mLastLocation.getLongitude();

            try {
                Address currentAddress = gc.getFromLocation(latitude, longitude, 1).get(0);

                String locationLabel = getResources().getString(R.string.myLocation);

                myLocation.setText(locationLabel + ": " + currentAddress.getLocality() + ",  "+ currentAddress.getCountryName());

                CrimeAPIClient cac = new CrimeAPIClient(MainActivity.class.getSimpleName());
                cac.CallCrimeRateAPI(latitude, longitude, this);
            }
            catch(Exception ex)
            {


            }


        } else {

            //myLocation.setText("(Couldn't get the location. Make sure location is enabled on the device)");
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
        mNavigationDrawerHelper.handleOnOptionsItemSelected(item);

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
         if(myApiClient.GetGoogleAPIClient().isConnected() && myApiClient.IsLocationUpdate()) {
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


    @Override
    public void HandleCrimeUpdate(ArrayList<CrimeApiResult> result) {
          AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
          outAnimation.setDuration(200);
          progressBarHolder.setAnimation(outAnimation);
          progressBarHolder.setVisibility(View.GONE);

        FragmentManager fm = getFragmentManager();
        MainActivityFragment maf = (MainActivityFragment)fm.findFragmentById(R.id.fragment);
        maf.crimeResult = result;
        maf.RebindFragment(result);
        //fm.beginTransaction().detach(maf).attach(maf).commit();
    }
}