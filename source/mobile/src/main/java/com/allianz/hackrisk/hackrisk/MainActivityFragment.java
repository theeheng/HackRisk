package com.allianz.hackrisk.hackrisk;

import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
 class RatingListViewArrayAdapter extends ArrayAdapter<String> {

    Context adapterContext = null;
    ArrayList<Integer> ratingIconsResource;
    ArrayList<Double> ratingPercentage;

    int redThreshold = 75;
    int greenThreshold = 40;

    Drawable redProgress;
    Drawable amberProgress;
    Drawable greenProgress;

    public RatingListViewArrayAdapter(Context context, int textViewResourceId,
                              HashMap<Integer, Double> rating, Drawable green, Drawable amber, Drawable red) {

        super(context, textViewResourceId, (List)new ArrayList<Integer>(rating.keySet()));

        this.adapterContext = context;

        this.ratingIconsResource = new ArrayList<Integer>(rating.keySet());
        this.ratingPercentage= new ArrayList<Double>(rating.values());
        redProgress = red;
        amberProgress = amber;
        greenProgress = green;
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
        ProgressBar progressView = (ProgressBar) rowView.findViewById(R.id.listItemRatingBar);

        imageView.setImageResource(ratingIconsResource.get(position));

        int progressBarPercentage = ratingPercentage.get(position).intValue();

        if(progressBarPercentage > redThreshold)
        {
            progressView.setProgressDrawable(redProgress);
        }else if(progressBarPercentage < greenThreshold) {
            progressView.setProgressDrawable(greenProgress);
        }
        else
        {
            progressView.setProgressDrawable(amberProgress);
        }


        progressView.setMax(100);
        progressView.setProgress(progressBarPercentage);


        return rowView;
    }


}

public class MainActivityFragment extends Fragment {

    private ListView ratingListView;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View mView =  inflater.inflate(R.layout.fragment_main, container, false);

        ratingListView = (ListView) mView.findViewById (R.id.ratingListView);

        HashMap<Integer,Double> rating = new HashMap<Integer,Double>();

        rating.put(R.drawable.car_icon, Double.parseDouble("10"));
        rating.put(R.drawable.crime_icon, Double.parseDouble("10"));
        rating.put(R.drawable.health_icon, Double.parseDouble("10"));

        final RatingListViewArrayAdapter adapter = new RatingListViewArrayAdapter(getActivity()
                .getApplicationContext(), android.R.layout.simple_list_item_1, rating, getActivity()
                .getApplicationContext().getResources().getDrawable(R.drawable.ratinggreenprogress),getActivity()
                .getApplicationContext().getResources().getDrawable(R.drawable.ratingamberprogress), getActivity()
                .getApplicationContext().getResources().getDrawable(R.drawable.ratingredprogress)
                );
        ratingListView.setAdapter(adapter);

        ratingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
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

        return mView;
    }
}
