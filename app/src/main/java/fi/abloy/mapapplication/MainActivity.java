package fi.abloy.mapapplication;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;

import fi.abloy.mapapplication.adapter.LocationRecyclerViewAdapter;
import fi.abloy.mapapplication.async.AsyncGetCoordinatesData;
import fi.abloy.mapapplication.common.Extra;
import fi.abloy.mapapplication.data.Location;
import fi.abloy.mapapplication.data.Point;
import fi.abloy.mapapplication.interfaces.IOnItemClickListener;
import fi.abloy.mapapplication.interfaces.IResponseHelper;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener, IOnItemClickListener {
    private RecyclerView recyclerViewLocation;
    private LocationRecyclerViewAdapter locationRecyclerViewAdapter;
    private ArrayList<String> excludedIdsList;
    private LinearLayoutManager mLayoutManager;
    private int previousVisibleItems, visibleItemCount, totalItemCount;
    private ProgressBar progressBarLocation;
    private String query;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        initializeData();
    }

    private void initializeUI() {
        recyclerViewLocation = findViewById(R.id.recycler_view_location);
        progressBarLocation = findViewById(R.id.progress_bar_location_loading);
        progressBarLocation.setVisibility(View.INVISIBLE);
        //scroll listener for paging
        recyclerViewLocation.addOnScrollListener(new RecyclerView.OnScrollListener() {


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    previousVisibleItems = mLayoutManager.findFirstVisibleItemPosition();
                    if (loading) {
                        if ((visibleItemCount + previousVisibleItems) >= totalItemCount) {
                            loading = false;
                            getLocation(query, excludedIdsList); //sending request with excluded ids
                            progressBarLocation.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }
        });
    }


    private void initializeData() {
        locationRecyclerViewAdapter = new LocationRecyclerViewAdapter(this);
        recyclerViewLocation.setAdapter(locationRecyclerViewAdapter);
        mLayoutManager = (LinearLayoutManager) recyclerViewLocation.getLayoutManager();
        excludedIdsList = new ArrayList<>();
    }


    @Override
    public void onItemClick(View view, final Object object) {

        new AsyncGetCoordinatesData(new IResponseHelper() {
            @Override
            public void getData(Object object) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                intent.putExtra(Extra.MAP_COORDINATES, (Point) object);
                startActivity(intent);
            }
        }).execute(object);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        excludedIdsList.clear();
        locationRecyclerViewAdapter.removeAll(locationRecyclerViewAdapter.getLocationList());
        this.query = query;
        getLocation(query, excludedIdsList);
        return true;
    }

    //get query from search field and hit api
    public void getLocation(String query, ArrayList<String> IdsList) {
        NomantimApiRequest.getLocation(query, IdsList, new IResponseHelper() {
            @Override
            public void getData(Object object) {
                ArrayList<Location> locationArrayList = (ArrayList<Location>) object;
                for (int i = 0; i < locationArrayList.size(); i++) {
                    excludedIdsList.add(locationArrayList.get(i).getPlaceId()); //getting the data and adding values in excluded ids list
                }
                locationRecyclerViewAdapter.addAll(locationArrayList);
                locationRecyclerViewAdapter.notifyDataSetChanged();
                progressBarLocation.setVisibility(View.INVISIBLE);
                loading = true;
            }
        });
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }
}

