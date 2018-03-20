package fi.mapapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fi.mapapplication.R;
import fi.mapapplication.data.Location;
import fi.mapapplication.interfaces.IOnItemClickListener;
import fi.mapapplication.recyclerview.LocationRecyclerViewHolder;


public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewHolder> {
    private List<Location> locationList;
    private IOnItemClickListener iOnItemClickListener;

    public LocationRecyclerViewAdapter(IOnItemClickListener iOnItemClickListener) {
        this.locationList = new ArrayList<>();
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return locationList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull LocationRecyclerViewHolder holder, final int position) {
        final Location mLocation = locationList.get(position);
        holder.textViewLocationName.setText(mLocation.getDisplayName());
    }

    /*Creates the view for recycler view*/
    @Override
    public LocationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mViewGroup = (ViewGroup) layoutInflater.inflate(
                R.layout.location_recyclerview_item, viewGroup, false);

        final LocationRecyclerViewHolder locationRecyclerViewHolder = new LocationRecyclerViewHolder(mViewGroup);

        locationRecyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(v, locationList.get(locationRecyclerViewHolder.getAdapterPosition()));
            }
        });

        return locationRecyclerViewHolder;
    }

    //Adding item in the recycler view
    public void add(final Location location) {
        locationList.add(location);
        notifyDataSetChanged();
    }

    //Adding all item in the recycler view
    public void addAll(final List<Location> locationList) {
        this.locationList.addAll(locationList);
        notifyDataSetChanged();
    }

    //Removing item from recycler view
    public void removeAll(final List<Location> locationList) {
        locationList.removeAll(locationList);
        notifyDataSetChanged();
    }

    public List<Location> getLocationList() {
        return locationList;
    }
}
