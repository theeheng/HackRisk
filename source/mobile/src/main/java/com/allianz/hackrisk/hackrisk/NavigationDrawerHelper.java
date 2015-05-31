package com.allianz.hackrisk.hackrisk;


import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Jim on 8/1/13.
 */

class NavigationDrawerAdapter extends ArrayAdapter<String> {

    Context adapterContext = null;
    List<String> icons;

    public NavigationDrawerAdapter(Context context, int textViewResourceId, List<String> iconList) {

        super(context, textViewResourceId, iconList);

        this.adapterContext = context;
        this.icons = iconList;
    }

    @Override
    public long getItemId(int position) {
        // String item = getItem(position);
        // return adapterRating.get(item);
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) this.adapterContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        View rowView = inflater.inflate(R.layout.drawer_option_item, parent, false);

        ImageView imgView = (ImageView) rowView.findViewById(R.id.listToggleIcon);
        TextView toggleText = (TextView) rowView.findViewById(R.id.toggleName);
        Switch toggle = (Switch) rowView.findViewById(R.id.togglebutton);

        if(position == 0) {
            imgView.setPadding(0,0,0,0);

            imgView.setImageResource(R.drawable.profile);
            toggleText.setVisibility(View.INVISIBLE);
            toggle.setVisibility(View.INVISIBLE);
        }
        else
        {
            toggleText.setText(icons.get(position));
            toggle.setChecked(true);

            if (icons.get(position).equals("Health Risk")) {
                imgView.setImageResource(R.drawable.health_icon_s);

            } else if (icons.get(position).equals("Crime Risk")) {
                imgView.setImageResource(R.drawable.crime_icon_s);
            } else if (icons.get(position).equals("Travel Risk")) {
                imgView.setImageResource(R.drawable.car_icon_s);
            }

            //imageView.setImageResource(ratingIconsResource.get(position));


            toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        // The toggle is enabled
                        int i = 1;
                    } else {
                        // The toggle is disabled
                        int j = 0;
                    }
                }
            });
        }

        return rowView;
    }


}

public class NavigationDrawerHelper {
    DrawerLayout mDrawerLayout;
    ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;

    public  void init(Activity theActivity, ListView.OnItemClickListener listener) {
        mDrawerLayout = (DrawerLayout) theActivity.findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) theActivity.findViewById(R.id.left_drawer);

        String[] navigationDrawerOptions =
                theActivity.getResources().getStringArray(R.array.navigation_drawer_options);
        NavigationDrawerAdapter navigationDrawerAdapter =
                new NavigationDrawerAdapter(theActivity, R.layout.drawer_option_item, Arrays.asList(navigationDrawerOptions));
        mDrawerListView.setAdapter(navigationDrawerAdapter);
        mDrawerListView.setOnItemClickListener(listener);
//        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerLayout.setBackgroundResource(android.R.color.white);

        //mDrawerListView.setItemChecked(0, true);
        setupActionBar(theActivity);
    }

    private void setupActionBar(Activity theActivity){
        final Activity activity = theActivity;
        ActionBar actionBar = theActivity.getActionBar();

        actionBar.setDisplayHomeAsUpEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                theActivity,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.open_drawer_message,
                R.string.close_drawer_message
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                activity.invalidateOptionsMenu();
                super.onDrawerOpened(drawerView);
            }
        };

    }

    public void handleSelect(int option, Activity activity, String accessToken, int siteId) {

        //if(mDrawerListView != null)
        //    mDrawerListView.setItemChecked(option, true);

        if(mDrawerLayout != null)
            mDrawerLayout.closeDrawer(mDrawerListView);

        if(option == 0)
        {
            handleDownloadStockCountItemClick(activity, accessToken, siteId);
        }
        else if(option == 1) {

            handleUploadStockCountButtonClick(activity, accessToken, siteId);
        }
        else
        {

        }

    }

    public void handleOnPrepareOptionsMenu(Menu menu) {

        if(mDrawerLayout != null) {
            boolean itemVisible = !mDrawerLayout.isDrawerOpen(mDrawerListView);

            for (int index = 0; index < menu.size(); index++) {
                MenuItem item = menu.getItem(index);
                item.setEnabled(itemVisible);
            }
        }
    }

    public void handleOnOptionsItemSelected(MenuItem item) {
        mDrawerToggle.onOptionsItemSelected(item);
    }

    public void syncState() {
        if(mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    public void setSelection(int option) {
        mDrawerListView.setItemChecked(option, true);
    }

    private void handleDownloadStockCountItemClick(final Activity activity, String accessToken, int siteId) {


    }

    private void handleUploadStockCountButtonClick(final Activity activity, String accessToken, int siteId) {



    }
}

