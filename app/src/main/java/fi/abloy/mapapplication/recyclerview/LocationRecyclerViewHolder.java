package fi.abloy.mapapplication.recyclerview;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fi.abloy.mapapplication.R;


public class LocationRecyclerViewHolder extends RecyclerView.ViewHolder {
    public TextView textViewLocationName;

    public LocationRecyclerViewHolder(@NonNull View view) {
        super(view);
        textViewLocationName = view.findViewById(R.id.text_view_location_name);
    }
}
