package com.allianz.hackrisk.hackrisk;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by devsupport on 30/05/2015.
 */
public class CrimeAPIClient {

    private String TAG;


    private String CrimeAtLocationApiURL = "https://data.police.uk/api/crimes-at-location?date=%s&lat=%s&lng=%s";


    public CrimeAPIClient(String tag)
    {
        this.TAG = tag;
    }

    public void CallCrimeRateAPI(double lat, double lng)
    {
        SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM" );

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);

        String dateFormatString = formatter.format(cal.getTime());

        /*JsonArrayRequest jsonObjReq = new JsonArrayRequest(
                String.format(CrimeAtLocationApiURL,dateFormatString,Double.toString(lat),Double.toString(lng)) , new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {

                try {


                    // Parsing json object response
                    // response will be a json object

                    resultStockCountItemArray = new LinkedList<StockCountItem>();
                    resultStockItemSizeArray = new LinkedList<StockItemSize>();

                    for (int i = 0; i < response.length(); i++) {

                        JSONObject jsonUsrPro = (JSONObject) response.get(i);

                        StockCountItem s = new StockCountItem();
                        s.SiteItemId = Integer.parseInt(jsonUsrPro.getString("SiteItemID"));
                        s.CategoryId = Integer.parseInt(jsonUsrPro.getString("CategoryID"));
                        s.ItemName = jsonUsrPro.getString("ItemName");
                        s.CategoryName = jsonUsrPro.getString("CategoryName");
                        s.CategoryHierarchy = jsonUsrPro.getString("CategoryHierarchy");
                        s.SupplierId = Integer.parseInt(jsonUsrPro.getString("SupplierID"));
                        s.SiteId = Integer.parseInt(jsonUsrPro.getString("SiteID"));
                        s.StockItemId = Integer.parseInt(jsonUsrPro.getString("StockItemID"));
                        s.CostPrice = Double.parseDouble(jsonUsrPro.getString("CostPrice"));
                        s.Count = new LinkedList<StockCount>();

                        resultStockCountItemArray.add(s);

                        JSONArray stockItemSizes = (JSONArray) jsonUsrPro.getJSONArray("StockItemSizes");

                        for(int j=0; j < stockItemSizes.length() ; j++)
                        {
                            int stockItemSizeId = Integer.parseInt(stockItemSizes.getJSONObject(j).getString("StockItemSizeID"));
                            boolean exist = false;

                            for(StockItemSize stkSize : resultStockItemSizeArray)
                            {
                                if(stkSize.StockItemSizeId == stockItemSizeId)
                                {
                                    exist= true;
                                    break;
                                }
                            }

                            if(!exist) {
                                StockItemSize sis = new StockItemSize();
                                sis.StockItemSizeId = stockItemSizeId ;
                                sis.StockItemId = Integer.parseInt(stockItemSizes.getJSONObject(j).getString("StockItemID")) ;
                                sis.Size = Double.parseDouble(stockItemSizes.getJSONObject(j).getString("Size")) ;
                                sis.UnitOfMeasureCode = stockItemSizes.getJSONObject(j).getString("UnitOfMeasureCode");
                                // sis.UnitOfMeasureId = Integer.parseInt(stockItemSizes.getJSONObject(j).getString("Size"));
                                sis.ConversionRatio = Double.parseDouble(stockItemSizes.getJSONObject(j).getString("ConversionRatio"));
                                sis.CaseSizeDescription = stockItemSizes.getJSONObject(j).getString("CaseDescriptionID").equals("null") ? null : stockItemSizes.getJSONObject(j).getString("CaseDescriptionID") ;
                                sis.IsDefault = stockItemSizes.getJSONObject(j).getString("IsDefault").equals("true") ? true : false ;

                                String currentCount = stockItemSizes.getJSONObject(j).getString("StockCount");

                                if(!currentCount.equals("null"))
                                {
                                    StockCount cnt = new StockCount();
                                    cnt.StockItemSizeId = sis.StockItemSizeId;
                                    cnt.SiteItemId = s.SiteItemId;
                                    cnt.CurrentCount = Double.parseDouble(currentCount);
                                    cnt.Updated = false;
                                    s.Count.add(cnt);
                                }

                                resultStockItemSizeArray.add(sis);
                            }
                        }
                    }

                    resultStockCountItemArray.get(0).StockItemSizes = resultStockItemSizeArray;

                    CallResetStockCountItemDBForSite(resultStockCountItemArray, mNotificationHelper, startId);



                } catch (JSONException e) {
                    e.printStackTrace();

                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext().getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
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
