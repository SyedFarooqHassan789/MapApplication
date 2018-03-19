package fi.abloy.mapapplication.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import fi.abloy.mapapplication.R;
import fi.abloy.mapapplication.data.Location;
import fi.abloy.mapapplication.interfaces.IOnItemClickListener;
import fi.abloy.mapapplication.recyclerview.LocationRecyclerViewHolder;


public class LocationRecyclerViewAdapter extends RecyclerView.Adapter<LocationRecyclerViewHolder> {
    private List<Location> getLocationList;
    private IOnItemClickListener iOnItemClickListener;

    public LocationRecyclerViewAdapter(IOnItemClickListener iOnItemClickListener) {
        this.getLocationList = new ArrayList<>();
        this.iOnItemClickListener = iOnItemClickListener;
    }

    @Override
    public int getItemCount() {
        return getLocationList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull LocationRecyclerViewHolder holder, final int position) {
        final Location getLocation = getLocationList.get(position);
        holder.textViewLocationName.setText(getLocation.getDisplayName());
    }

    /*Creates the view for recycler view*/
    @Override
    public LocationRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup mViewGroup = (ViewGroup) layoutInflater.inflate(
                R.layout.location_recyclerview_item, viewGroup, false);

        final LocationRecyclerViewHolder recyclerViewHolder = new LocationRecyclerViewHolder(mViewGroup);

        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnItemClickListener.onItemClick(v, getLocationList.get(recyclerViewHolder.getAdapterPosition()));
            }
        });

        return recyclerViewHolder;
    }

    //Adding item in the recycler view
    public void add(final Location locationInfo) {
        getLocationList.add(locationInfo);
        notifyDataSetChanged();
    }

    //Adding all item in the recycler view
    public void addAll(final List<Location> getLocationList) {
        this.getLocationList.addAll(getLocationList);
        notifyDataSetChanged();
    }

    //Removing item from recycler view
    public void removeAll(final List<Location> getLocationList) {
        getLocationList.removeAll(getLocationList);
        notifyDataSetChanged();
    }

    public List<Location> getGetLocationList() {
        return getLocationList;
    }
}
