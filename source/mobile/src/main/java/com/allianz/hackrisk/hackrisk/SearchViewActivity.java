package com.allianz.hackrisk.hackrisk;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.location.*;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
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

    private String TAG = SearchViewActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchview);

        Intent queryIntent = getIntent();

        longitude = Double.parseDouble(queryIntent.getStringExtra(MainActivity.EXTRA_LONG));
        latitude = Double.parseDouble(queryIntent.getStringExtra(MainActivity.EXTRA_LAT));

        Bundle bundle = queryIntent.getExtras();

        if (bundle != null && bundle.getBundle(MainActivity.EXTRA_CRIMEAPIRESULT) != null) {
            bundle = bundle.getBundle(MainActivity.EXTRA_CRIMEAPIRESULT);
            crimeAPIResult = (List<RiskObject>) bundle.getSerializable(MainActivity.EXTRA_CRIMEAPIRESULT);
        }



        setupMap();

        final String queryAction = queryIntent.getAction();
        if (Intent.ACTION_SEARCH.equals(queryAction))
        {
           // CallSearchStockCountItem searchItemAsync = new CallSearchStockCountItem(this);
          //  String searchType = HomeActivity.SearchType.SearchByName.toString();
          //  searchItemAsync.execute(searchType, queryIntent.getStringExtra(SearchManager.QUERY), null, Boolean.toString(false));
           // finish();
        }
        else if(Intent.ACTION_VIEW.equals(queryAction))
        {

           // CallSearchStockCountItem searchItemAsync = new CallSearchStockCountItem(this);
           // String searchType = SearchType.SearchBySiteItemId.toString();
           // searchItemAsync.execute(searchType, queryIntent.getData().getLastPathSegment());
           // finish();
        }
        else {
            Log.d(TAG,"Create intent NOT from search");
        }
    }

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

if(crimeAPIResult != null) {
    for (Iterator<RiskObject> i = crimeAPIResult.iterator(); i.hasNext(); ) {
        RiskObject item = i.next();

        LatLng pos;

        if((prevLatitude != null && prevLongitude != null)&&(prevLatitude.equals(item.Latitude) && prevLongitude.equals(item.Longitude)))
        {
            pos = new LatLng(item.Latitude + 0.05, item.Longitude + 0.05 );
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
        super.onSearchRequested();

    }

    @Override
    protected void onNewIntent(Intent intent) {

        int i = 0;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_view, menu);
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

}
