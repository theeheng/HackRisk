package com.allianz.hackrisk.hackrisk;

import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;

/**
 * Created by devsupport on 30/05/2015.
 */
public class CrimeAPIClient {

    private String TAG;


    private String CrimeAtLocationApiURL = "https://data.police.uk/api/crimes-at-location?lat=%s&lng=%s";

    public CrimeAPIClient(String tag)
    {
        this.TAG = tag;
    }

    public void CallCrimeRateAPI(double lat, double lng, final FrameLayout progressBarHolder)
    {
        final LinkedList<CrimeApiResult> crimeResult = new LinkedList<CrimeApiResult>();

        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM" );

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        String dateFormatString = formatter.format(cal.getTime());

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                String.format(CrimeAtLocationApiURL,Double.toString(lat),Double.toString(lng)) , new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {
                    // Parsing json object response
                    // response will be a json object

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonObj = (JSONObject) response.get(i);

                        CrimeApiResult s = new CrimeApiResult();
                        s.setCategory(jsonObj.getString("category"));
                       /* s.CategoryId = Integer.parseInt(jsonUsrPro.getString("CategoryID"));
                        s.ItemName = jsonUsrPro.getString("ItemName");
                        s.CategoryName = jsonUsrPro.getString("CategoryName");
                        s.CategoryHierarchy = jsonUsrPro.getString("CategoryHierarchy");
                        s.SupplierId = Integer.parseInt(jsonUsrPro.getString("SupplierID"));
                        s.SiteId = Integer.parseInt(jsonUsrPro.getString("SiteID"));
                        s.StockItemId = Integer.parseInt(jsonUsrPro.getString("StockItemID"));
                        s.CostPrice = Double.parseDouble(jsonUsrPro.getString("CostPrice"));
                        s.Count = new LinkedList<StockCount>();
*/

                        JSONObject location = (JSONObject) jsonObj.getJSONObject("location");

                        if (location != null) {

                            Location lc = new Location();

                            lc.setLatitude(location.getString("latitude"));
                            lc.setLongitude(location.getString("longitude"));

                            JSONObject street = (JSONObject) location.getJSONObject("street");

                            if (street != null) {
                                Street str = new Street();
                                str.setName(street.getString("name"));

                                lc.setStreet(str);
                                s.setLocation(lc);
                            }
                        }

                        JSONObject outcome = (JSONObject) jsonObj.getJSONObject("outcome_status");

                        if(outcome != null) {
                            OutcomeStatus outStatus = new OutcomeStatus();
                            outStatus.setCategory(outcome.getString("category"));
                            s.setOutcomeStatus(outStatus);
                        }

                        crimeResult.add(s);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();

                    Log.d(TAG, e.getMessage());
                }

                AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
                outAnimation.setDuration(200);
                progressBarHolder.setAnimation(outAnimation);
                progressBarHolder.setVisibility(View.GONE);
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.getMessage());

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);

        /*
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                String.format(CrimeAtLocationApiURL,dateFormatString,Double.toString(lat),Double.toString(lng)) , null , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                }
                catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

        }
        });
                       // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
*/


    }
}
