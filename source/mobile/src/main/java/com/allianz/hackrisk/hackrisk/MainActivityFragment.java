package com.allianz.hackrisk.hackrisk;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
 class RatingListViewArrayAdapter extends ArrayAdapter<String> {

    Context adapterContext = null;
    ArrayList<Integer> ratingIconsResource;
    ArrayList<String> ratingPercentage;

    int redThreshold = 75;
    int greenThreshold = 40;

    Drawable redProgress;
    Drawable amberProgress;
    Drawable greenProgress;

    public RatingListViewArrayAdapter(Context context, int textViewResourceId,
                              HashMap<Integer, String> rating) {

        super(context, textViewResourceId, (List)new ArrayList<Integer>(rating.keySet()));

        this.adapterContext = context;

        this.ratingIconsResource = new ArrayList<Integer>(rating.keySet());
        this.ratingPercentage= new ArrayList<String>(rating.values());
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
        View rowView = inflater.inflate(R.layout.listviewitem_rating, parent, false);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.listItemRatingIcon);
        TextView ratingNameView = (TextView) rowView.findViewById(R.id.listItemRatingName);
        TextView ratingWordView = (TextView) rowView.findViewById(R.id.listItemRatingWord);

        imageView.setImageResource(ratingIconsResource.get(position));

        if(ratingIconsResource.get(position) == R.drawable.car_icon_2)
        {
            ratingNameView.setText("Travel Risk");
        }
        else if(ratingIconsResource.get(position) == R.drawable.crime_button)
        {
            ratingNameView.setText("Crime Risk");
        }else if(ratingIconsResource.get(position) == R.drawable.health_button_2)
        {
            ratingNameView.setText("Health Risk");
        }
        else if(ratingIconsResource.get(position) == R.drawable.car_icon_s)
        {
            ratingNameView.setTextSize(12);
            ratingWordView.setTextSize(12);
            ratingNameView.setText("Travel Risk");
        }
        else if(ratingIconsResource.get(position) == R.drawable.crime_icon_s)
        {
            ratingNameView.setTextSize(12);
            ratingWordView.setTextSize(12);
            ratingNameView.setText("Crime Risk");
        }else if(ratingIconsResource.get(position) == R.drawable.health_icon_s)
        {
            ratingNameView.setTextSize(12);
            ratingWordView.setTextSize(12);
            ratingNameView.setText("Health Risk");
        }

        ratingNameView.setTextColor(Color.parseColor("#00509D"));
                ratingWordView.setText(ratingPercentage.get(position));

        if(ratingPercentage.get(position).equals("High"))
        {
            ratingWordView.setTextColor(Color.parseColor("#E10042"));
        }
        else if (ratingPercentage.get(position).equals("Medium"))
        {
            ratingWordView.setTextColor(Color.parseColor("#FF9600"));
        }
        else if(ratingPercentage.get(position).equals("Low"))
        {
            ratingWordView.setTextColor(Color.parseColor("#00509D"));
        }
        return rowView;
    }


}

public class MainActivityFragment extends Fragment {

    private ListView ratingListView;
    public ArrayList<CrimeApiResult> crimeResult;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View mView =  inflater.inflate(R.layout.fragment_main, container, false);

        ratingListView = (ListView) mView.findViewById (R.id.ratingListView);

        RebindFragment(null, true);
        /*
        HashMap<Integer,Double> rating = new HashMap<Integer,Double>();


        if(crimeResult == null || (crimeResult != null && crimeResult.size() == 0)) {
            rating.put(R.drawable.car_icon, Double.parseDouble("10"));
            rating.put(R.drawable.crime_icon, Double.parseDouble("10"));
            rating.put(R.drawable.health_icon, Double.parseDouble("10"));
        }
        else
        {
            rating.put(R.drawable.car_icon, Double.parseDouble("80"));
            rating.put(R.drawable.crime_icon, Double.parseDouble("80"));
            rating.put(R.drawable.health_icon, Double.parseDouble("80"));
        }

        final RatingListViewArrayAdapter adapter = new RatingListViewArrayAdapter(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1, rating, getActivity()
                .getApplicationContext().getResources().getDrawable(R.drawable.ratingamberprogress),getActivity()
                .getApplicationContext().getResources().getDrawable(R.drawable.ratingamberprogress), getActivity()
                .getApplicationContext().getResources().getDrawable(R.drawable.ratingamberprogress)
                );
        ratingListView.setAdapter(adapter);

        ratingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), RiskListActivity.class);
                startActivity(intent);
                /*final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }

        });
*/
        return mView;
    }

    public void RebindFragment(final ArrayList<CrimeApiResult> result, boolean isMainPage)
    {
        HashMap<Integer,String> rating = new HashMap<Integer,String>();

        if(result == null || (result != null && result.size() == 0)) {
            rating.put((isMainPage) ? R.drawable.car_icon_2 : R.drawable.car_icon_s, "");
            rating.put((isMainPage) ? R.drawable.crime_button : R.drawable.crime_icon_s, "");
            rating.put((isMainPage) ? R.drawable.health_button_2 : R.drawable.health_icon_s, "");
        }
        else
        {
            double percentage = ((double)result.size() / 30*100);
            String ratingWord = "";

            if(result != null)
            {
                if(percentage > 65)
                {
                    ratingWord = "High";
                }else if(percentage < 40)
                {
                    ratingWord = "Low";
                }
                else
                {
                    ratingWord = "Medium";
                }
            }
            rating.put((isMainPage) ? R.drawable.car_icon_2 : R.drawable.car_icon_s, "Medium");
            rating.put((isMainPage) ? R.drawable.crime_button : R.drawable.crime_icon_s, ratingWord);
            rating.put((isMainPage) ? R.drawable.health_button_2 : R.drawable.health_icon_s, "Low");
        }

        final RatingListViewArrayAdapter adapter2 = new RatingListViewArrayAdapter(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1, rating);

        ratingListView.setAdapter(adapter2);

        ratingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {

                Intent intent = new Intent(getActivity(), RiskListActivity.class);

                Bundle b = new Bundle();

                ArrayList<RiskObject> ro = new ArrayList<RiskObject>();

                String selectedRisk = ((TextView)ratingListView.getChildAt(position).findViewById(R.id.listItemRatingName)).getText().toString();

                if (result != null && selectedRisk.equals("Crime Risk")) {
                    for (Iterator<CrimeApiResult> i = result.iterator(); i.hasNext(); ) {
                        CrimeApiResult item = i.next();
                        RiskObject o = new RiskObject();

                        o.Header = item.getCategory();

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
                        o.Location = item.getLocation().getStreet().getName();
                        o.Date = item.getMonth();

                        o.Category = item.getCategory();
                        o.Latitude = Double.parseDouble(item.getLocation().getLatitude());
                        o.Longitude = Double.parseDouble(item.getLocation().getLongitude());
                        o.StreetName = item.getLocation().getStreet().getName();
                        ro.add(o);
                    }
                } else if (selectedRisk.equals("Travel Risk")) {
                    RiskObject r1 = new RiskObject();
                    r1.Header = "Travel Risk Headline1";
                    r1.Description = "sdfasd fdsafdsa fdsafsa dfdasf sdf sdfsdafdsa fsdaf sdafsda fdsfdsf asdfsda fa dsf dsaf";
                    r1.Location = "London , United Kingdom";
                    r1.Date = "25/05/2015";

                    RiskObject r2 = new RiskObject();
                    r2.Header = "Travel Risk Headline2";
                    r2.Description = "sdfasd fdsafdsa fdsafsa dfdasf sdf sdfsdafdsa fsdaf sdafsda fdsfdsf asdfsda fa dsf dsaf";
                    r2.Location = "London , United Kingdom";
                    r2.Date = "25/05/2015";

                    ro.add(r1);
                    ro.add(r2);
                } else if (selectedRisk.equals("Health Risk")) {
                    RiskObject r1 = new RiskObject();
                    r1.Header = "Health Risk Headline1";
                    r1.Description = "sdfasd fdsafdsa fdsafsa dfdasf sdf sdfsdafdsa fsdaf sdafsda fdsfdsf asdfsda fa dsf dsaf";
                    r1.Location = "London , United Kingdom";
                    r1.Date = "25/05/2015";

                    RiskObject r2 = new RiskObject();
                    r2.Header = "Health Risk Headline2";
                    r2.Description = "sdfasd fdsafdsa fdsafsa dfdasf sdf sdfsdafdsa fsdaf sdafsda fdsfdsf asdfsda fa dsf dsaf";
                    r2.Location = "London , United Kingdom";
                    r2.Date = "25/05/2015";

                    ro.add(r1);
                    ro.add(r2);
                }

                b.putSerializable(MainActivity.EXTRA_CRIMEAPIRESULT, ro);
                intent.putExtra(MainActivity.EXTRA_CRIMEAPIRESULT, b);

                startActivity(intent);
                /*final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });*/
            }

        });
    }
}
