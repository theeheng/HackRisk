package com.allianz.hackrisk.hackrisk;

/**
 * Created by devsupport on 30/05/2015.
 */
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;



/**
 * Created by Jim on 8/1/13.
 */
public class NavigationDrawerHelper {
    DrawerLayout mDrawerLayout;
    ListView mDrawerListView;
    private ActionBarDrawerToggle mDrawerToggle;

    public  void init(Activity theActivity, ListView.OnItemClickListener listener) {
        mDrawerLayout = (DrawerLayout) theActivity.findViewById(R.id.drawer_layout);
        mDrawerListView = (ListView) theActivity.findViewById(R.id.left_drawer);

        String[] navigationDrawerOptions =
                theActivity.getResources().getStringArray(R.array.navigation_drawer_options);
        ArrayAdapter<String> navigationDrawerAdapter =
                new ArrayAdapter<String>(theActivity, R.layout.drawer_option_item, navigationDrawerOptions);
        mDrawerListView.setAdapter(navigationDrawerAdapter);
        mDrawerListView.setOnItemClickListener(listener);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

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

            //handleUploadStockCountButtonClick(activity, accessToken, siteId);
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
}


