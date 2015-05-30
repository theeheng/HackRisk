package com.allianz.hackrisk.hackrisk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RiskListActivity extends ListActivity {

    static final protected String LIST_ITEM_LINE1 = "line1";
    static final protected String LIST_ITEM_LINE2 = "line2";

    private static final int DETAIL_DIALOG = 1;

    private ProgressDialog mLoadingDialog;
    private final Handler mHandler = new Handler();
    private String mErrorMsg = "";
    private List<RiskObject> items;
    private RiskObject selectedItem;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_list);

        PrepareItemClickHandler();

        PostCreate();

        refreshData();

        registerForContextMenu(getListView());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_risk_list, menu);
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

    private void PrepareItemClickHandler()
    {
        ListView myListView = (ListView) findViewById(getListViewResId());
        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1, int index,
                                    long arg3) {
                ShowDetailForItemAt(index);
            }

        });
    }

    protected void ShowDetailForItemAt(int index)
    {
        selectedItem = getItemAt(index);
        showDialog(DETAIL_DIALOG);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DETAIL_DIALOG:
                LayoutInflater li = LayoutInflater.from(this);
                View detailView = li.inflate(getDetailViewResId(), null);

                AlertDialog.Builder detailDialog = new AlertDialog.Builder(this);
                detailDialog.setTitle("Detail View");
                detailDialog.setView(detailView);
                return detailDialog.create();
        }
        return null;
    }

    protected int getDetailViewResId()
    {
        return R.layout.view_riskdetail;
    }


    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch(id) {
            case DETAIL_DIALOG:
                PrepareDetailDialog(dialog, selectedItem);
        }
    }

    protected void refreshData()
    {
        mErrorMsg = "";

        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
                items = loadData();
                mHandler.post(mUpdateResults);
            }
        };
        mLoadingDialog = ProgressDialog.show(this, "", this.getResources().getString(R.string.loading));
        t.start();
    }

    protected RiskObject getItemAt(int index)
    {
        if (items == null)
            return null;

        return items.get(index);
    }

    protected RiskObject getSelectedItem()
    {
        return selectedItem;
    }

    protected void setError(String errorMsg)
    {
        mErrorMsg = errorMsg;
    }

    // Create runnable for posting
    private final Runnable mUpdateResults = new Runnable() {
        public void run() {
            if (mErrorMsg != "")
            {
                new AlertDialog.Builder(RiskListActivity.this).setMessage(mErrorMsg).show();
                mErrorMsg = "";
            }
            else
            {
                List<Map<String, String>> data = new ArrayList<Map<String, String>>();

                for (RiskObject item: items)
                {
                    Map<String, String> row = new HashMap<String, String>();
                    row.put(LIST_ITEM_LINE1, getLine1(item));
                    row.put(LIST_ITEM_LINE2, getLine2(item));
                    data.add(row);
                }

                setListAdapter(new SimpleAdapter(RiskListActivity.this, data,
                        android.R.layout.two_line_list_item,
                        new String[] {LIST_ITEM_LINE1, LIST_ITEM_LINE2},
                        new int[] {android.R.id.text1, android.R.id.text2}));

                setTitle(getActivityTitle());
                if (data.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), R.string.no_items, Toast.LENGTH_SHORT).show();
                    //new AlertDialog.Builder(RallyListActivity.this).setMessage(R.string.no_items).show();
                }
            }

            if (mLoadingDialog != null && mLoadingDialog.isShowing())
                mLoadingDialog.cancel();
        }
    };

    public void PostCreate() {}

    protected int getListViewResId() { return android.R.id.list; }

    protected int getListLayoutResId() { return android.R.layout.two_line_list_item; }

    private void PrepareDetailDialog(Dialog dialog, RiskObject selected) {
         dialog.setTitle(selected.Header);

        ((TextView)dialog.findViewById(R.id.headerView)).setText(selected.Header);
        ((TextView)dialog.findViewById(R.id.descriptionView)).setText(selected.Description);
        ((TextView)dialog.findViewById(R.id.locationView)).setText(selected.Location);
        ((TextView)dialog.findViewById(R.id.dateView)).setText(selected.Date);
    }

    public List<RiskObject> loadData()
    {
        ArrayList<RiskObject> test = new ArrayList<RiskObject>();

        RiskObject r1 = new RiskObject();
        r1.Header = "Header Line R1";
        r1.Description = "sdfasd fdsafdsa fdsafsa dfdasf sdf sdfsdafdsa fsdaf sdafsda fdsfdsf asdfsda fa dsf dsaf";
        r1.Location = "London , United Kingdom";
        r1.Date = "25/05/2015";

        RiskObject r2 = new RiskObject();
        r2.Header = "Header Line R1";
        r2.Description = "sdfasd fdsafdsa fdsafsa dfdasf sdf sdfsdafdsa fsdaf sdafsda fdsfdsf asdfsda fa dsf dsaf";
        r2.Location = "London , United Kingdom";
        r2.Date = "25/05/2015";
        test.add(r1);
        test.add(r2);

        return test;
    }

    public String getLine1(RiskObject risk){
        return risk.Description.substring(1,20);
    }

    public String getLine2(RiskObject risk){
        return "second line";
    }

    public String getActivityTitle()
    {
        return "Risk List";
    }




}
