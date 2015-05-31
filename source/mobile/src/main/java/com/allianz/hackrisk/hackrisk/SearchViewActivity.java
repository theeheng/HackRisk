package com.allianz.hackrisk.hackrisk;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Intent;
import android.location.*;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SearchViewActivity extends Activity implements OnMapReadyCallback {

    private double longitude;
    private double latitude;
    private List<RiskObject> crimeAPIResult;
private GoogleApiClient mGoogleApiClient;
    private String TAG = SearchViewActivity.class.getSimpleName();
    private CrimeApiHandler crimeApiHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setIcon(R.drawable.backbutton);
        View cView = getLayoutInflater().inflate(R.layout.actionbar, null);
        actionBar.setCustomView(cView);

        crimeApiHandler = new CrimeApiHandler();

        Intent queryIntent = getIntent();

        if(queryIntent.getStringExtra(MainActivity.EXTRA_LONG) != null && queryIntent.getStringExtra(MainActivity.EXTRA_LAT) != null) {
            longitude = Double.parseDouble(queryIntent.getStringExtra(MainActivity.EXTRA_LONG));
            latitude = Double.parseDouble(queryIntent.getStringExtra(MainActivity.EXTRA_LAT));
        }

        Bundle bundle = queryIntent.getExtras();

        if (bundle != null && bundle.getBundle(MainActivity.EXTRA_CRIMEAPIRESULT) != null) {
            bundle = bundle.getBundle(MainActivity.EXTRA_CRIMEAPIRESULT);
            crimeAPIResult = (List<RiskObject>) bundle.getSerializable(MainActivity.EXTRA_CRIMEAPIRESULT);
        }

        final String queryAction = queryIntent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction))
        {
int i = 1;
           // CallSearchStockCountItem searchItemAsync = new CallSearchStockCountItem(this);
          //  String searchType = HomeActivity.SearchType.SearchByName.toString();
          //  searchItemAsync.execute(searchType, , null, Boolean.toString(false));
           // finish();
        }
        else if(Intent.ACTION_VIEW.equals(queryAction))
        {

            mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                    //.enableAutoManage(getContext(), 0 /* clientId */, this)
                    .addApi(Places.GEO_DATA_API)
                    .build();

            mGoogleApiClient.connect();

            String placeId = Uri.parse(queryIntent.getDataString()).getLastPathSegment();
            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                    .getPlaceById(mGoogleApiClient, placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
           // CallSearchStockCountItem searchItemAsync = new CallSearchStockCountItem(this);
           // String searchType = SearchType.SearchBySiteItemId.toString();
           // searchItemAsync.execute(searchType, queryIntent.getData().getLastPathSegment());
           // finish();
        }
        else {
            Log.d(TAG,"Create intent NOT from search");


            setupMap();
        }
    }

    /**
     * Callback for results from a Places Geo Data API query that shows the first place result in
     * the details view on screen.
     */
    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                // Request did not complete successfully
                Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                places.release();
                return;
            }
            // Get the Place object from the buffer.
            final Place place = places.get(0);



            LatLng coordinate =  place.getLatLng();

            longitude = coordinate.longitude;
            latitude = coordinate.latitude;

            CrimeAPIClient client = new CrimeAPIClient(SearchViewActivity.class.getSimpleName());

            client.CallCrimeRateAPI(latitude, longitude , crimeApiHandler);

            /* Format details of the place for display and show it in a TextView.
            mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
                    place.getId(), place.getAddress(), place.getPhoneNumber(),
                    place.getWebsiteUri()));

            // Display the third party attributions if set.
            final CharSequence thirdPartyAttribution = places.getAttributions();
            if (thirdPartyAttribution == null) {
                mPlaceDetailsAttribution.setVisibility(View.GONE);
            } else {
                mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
            }*/


            Log.i(TAG, "Place details received: " + place.getName());

            mGoogleApiClient.disconnect();
            places.release();


        }
    };

    private void setupMap() {


        ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng position = new LatLng(latitude, longitude);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 15.0f));
        googleMap.addMarker(new MarkerOptions().position(position));

    Double prevLatitude = null;
        Double prevLongitude = null;

        Double threshold= 0.0002;

if(crimeAPIResult != null) {
    for (Iterator<RiskObject> i = crimeAPIResult.iterator(); i.hasNext(); ) {
        RiskObject item = i.next();

        LatLng pos;

        if((prevLatitude != null && prevLongitude != null)&&(prevLatitude.equals(item.Latitude) && prevLongitude.equals(item.Longitude)))
        {
            pos = new LatLng(item.Latitude+ threshold, item.Longitude - threshold);
            threshold += threshold;
           // 51.507340, -0.130706
        }
        else {
            pos = new LatLng(item.Latitude, item.Longitude);
        }

        MarkerOptions mo = new MarkerOptions();
        mo.title(item.Category);
        mo.snippet(item.StreetName);
        BitmapDescriptor bitmapm = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        mo.icon(bitmapm);
        mo.position(pos);
        googleMap.addMarker(mo);

        prevLatitude = item.Latitude;
        prevLongitude = item.Longitude;

    }
    }

        FragmentManager fm = getFragmentManager();
        MainActivityFragment maf = (MainActivityFragment)fm.findFragmentById(R.id.fragment2);

        ArrayList<CrimeApiResult> ro = new ArrayList<CrimeApiResult>();

        if (crimeAPIResult != null) {
            for (Iterator<RiskObject> i = crimeAPIResult.iterator(); i.hasNext(); ) {
                RiskObject item = i.next();
                CrimeApiResult o = new CrimeApiResult();

                o.setCategory(item.Header);

                if (item.Description != null) {

                    OutcomeStatus out = new OutcomeStatus();
                    out.setDate(item.Date);
                    out.setCategory(item.Description);
                    o.setOutcomeStatus(out);
                }
                Location l = new Location();
                Street str = new Street();
                str.setName(item.Location);
                l.setStreet(str);
                o.setLocation(l);
                l.setLongitude(item.Latitude.toString());
                l.setLatitude(item.Longitude.toString());
                o.setMonth(item.Date);

                ro.add(o);
            }

//            fm.beginTransaction().detach(maf).attach(maf).commit();
            maf.crimeResult = ro;
            maf.RebindFragment(ro, false);

        }

        super.onSearchRequested();

    }

    @Override
    protected void onNewIntent(Intent intent) {

        int i = 0;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_search, menu);
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

        if(id == R.id.action_search)
        {
            super.onSearchRequested();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {

        int itemId = item.getItemId();
        switch (itemId) {
            case android.R.id.home:

                handleBackAction();
                // Toast.makeText(this, "home pressed", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onMenuItemSelected(featureId,item);

        }
    }

    private void handleBackAction() {
        finish();
    }

    class CrimeApiHandler implements ICrimeUpdateHandler
    {
        @Override
        public void HandleCrimeUpdate(ArrayList<CrimeApiResult> result) {

            if(result != null) {
                crimeAPIResult = new ArrayList<RiskObject>();
                for (Iterator<CrimeApiResult> i = result.iterator(); i.hasNext(); ) {
                    CrimeApiResult item = i.next();
                    RiskObject o = new RiskObject();
                    o.Category = item.getCategory();
                    o.Latitude = Double.parseDouble(item.getLocation().getLatitude());
                    o.Longitude = Double.parseDouble(item.getLocation().getLongitude());
                    o.StreetName = item.getLocation().getStreet().getName();
                    o.Date = item.getMonth();

                    if (item.getOutcomeStatus() != null) {

                        if (item.getOutcomeStatus().getCategory() != null) {
                            o.Description = item.getOutcomeStatus().getCategory();
                        }

                        if (item.getOutcomeStatus().getCategory() != null) {

                            if (o.Description != null) {
                                o.Description = o.Description + " " + item.getOutcomeStatus().getDate();
                            } else {
                                o.Description = item.getOutcomeStatus().getDate();
                            }
                        }

                    }

                    o.Header = item.getCategory();
                    o.Location = item.getLocation().getStreet().getName();
                    crimeAPIResult.add(o);
                }

                /*
                 FragmentManager fm = getFragmentManager();
        MainActivityFragment maf = (MainActivityFragment)fm.findFragmentById(R.id.fragment);

        if(maf != null) {
            maf.crimeResult = result;
            maf.RebindFragment(result);
        }
        this.crimeApiResults = result;
        //fm.beginTransaction().detach(maf).attach(maf).commit();
                 */
            }

            setupMap();
        }
    }
}
