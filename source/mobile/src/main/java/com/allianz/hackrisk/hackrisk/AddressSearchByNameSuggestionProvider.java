package com.allianz.hackrisk.hackrisk;

import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.AutocompletePredictionBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by htan on 26/01/2015.
 */


public class AddressSearchByNameSuggestionProvider extends ContentProvider implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String tag = "SuggestUrlProvider";
    public static String AUTHORITY =
            "com.allianz.hackrisk.hackrisk.AddressSearchByNameSuggestionProvider";

    private static final int SEARCH_SUGGEST = 0;
    private static final int SHORTCUT_REFRESH = 1;
    private static final UriMatcher sURIMatcher = buildUriMatcher();

    private static final String[] COLUMNS = {
            "_id",  // must include this column
            SearchManager.SUGGEST_COLUMN_TEXT_1,
            SearchManager.SUGGEST_COLUMN_TEXT_2,
            SearchManager.SUGGEST_COLUMN_INTENT_DATA,
            SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
            SearchManager.SUGGEST_COLUMN_SHORTCUT_ID
    };

    private static UriMatcher buildUriMatcher()
    {
        UriMatcher matcher =
                new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY,
                SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_QUERY +
                        "/*",
                SEARCH_SUGGEST);
        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_SHORTCUT,
                SHORTCUT_REFRESH);
        matcher.addURI(AUTHORITY,
                SearchManager.SUGGEST_URI_PATH_SHORTCUT +
                        "/*",
                SHORTCUT_REFRESH);
        return matcher;
    }

    private PlaceAutocompleteAdapter mAdapter;

    protected GoogleApiClient mGoogleApiClient;

    private static final LatLngBounds BOUNDS_GREATER_UK = new LatLngBounds(
            new LatLng(50.064192, -9.711914), new LatLng(61.015725, 3.691406));
    //private DB db;

    @Override
    public boolean onCreate() {

    //    db = new DB(getContext());
        // and disconnect() explicitly.
        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                //.enableAutoManage(getContext(), 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();

        // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
        // the entire world.
        mAdapter = new PlaceAutocompleteAdapter(getContext(), android.R.layout.simple_list_item_1,
                mGoogleApiClient, BOUNDS_GREATER_UK, null);

        mGoogleApiClient.connect();

        //lets not do anything in particular
        Log.d(tag, "onCreate called");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,
                        String selection, String[] selectionArgs,
                        String sortOrder)
    {
        Log.d(tag, "query called with uri:" + uri);
        Log.d(tag, "selection:" + selection);

        String query = selectionArgs[0];
        Log.d(tag, "query:" + query);

        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                Log.d(tag, "search suggest called");
                return getSuggestions(query);
            case SHORTCUT_REFRESH:
                Log.d(tag, "shortcut refresh called");
                return null;
            default:
                throw new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    private Cursor getSuggestions(String query)
    {
        List<PlaceAutocomplete> result;

        if (query == null) return null;
        else if(query.length() <= 2) {
            return null;
        }
        else
        {
            result = mAdapter.getAutocomplete(query);
        }

        MatrixCursor cursor = new MatrixCursor(COLUMNS);

        if(result != null) {
            for(PlaceAutocomplete s : result) {
                cursor.addRow(createRow(s, result.indexOf(s)));
            }
        }


        return cursor;
    }

    private Object[] createRow(PlaceAutocomplete suggestion, int indexId)
    {
        return columnValuesOfQuery(Integer.toString(indexId),
                "android.intent.action.VIEW",
                "http://com.allianz.hackrisk.hackrisk.AddressSearchByNameSuggestionProvider/" + suggestion.placeId,
                suggestion.description);
    }

    private Object[] columnValuesOfQuery(String placeId,
                                         String intentAction,
                                         String url,
                                         CharSequence text1)
    {
        String[] colValues = new String[] {
                placeId,     // _id
                text1.toString(),     // text1
               "",     // text2
                url,
                // intent_data (included when clicking on item)
                intentAction, //action
                SearchManager.SUGGEST_NEVER_MAKE_SHORTCUT
        };

        return colValues;
    }

    private Cursor refreshShortcut(String shortcutId,
                                   String[] projection) {
        return null;
    }

    public String getType(Uri uri) {
        switch (sURIMatcher.match(uri)) {
            case SEARCH_SUGGEST:
                return SearchManager.SUGGEST_MIME_TYPE;
            case SHORTCUT_REFRESH:
                return SearchManager.SHORTCUT_MIME_TYPE;
            default:
                throw
                        new IllegalArgumentException("Unknown URL " + uri);
        }
    }

    public Uri insert(Uri uri, ContentValues values) {
        throw new UnsupportedOperationException();
    }

    public int delete(Uri uri, String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    public int update(Uri uri, ContentValues values,
                      String selection,
                      String[] selectionArgs) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

   /* private List<StockCountItemSearchSuggestion> getSuggestedItemNameFromDB(String query)
    {
        // copy assets DB to app DB.
        try {
            db.create();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            // get all locations
            if (db.open()) {

                List<StockCountItemSearchSuggestion> suggestions = db.getStockCountItemNameSuggestionByItemName(query);

                return suggestions;

            } else {
                // error opening DB.
            }
        }
        catch(Exception ex) {

        }

        return null;
    }
*/



}
