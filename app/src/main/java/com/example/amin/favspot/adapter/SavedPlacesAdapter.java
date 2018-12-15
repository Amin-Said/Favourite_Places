package com.example.amin.favspot.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.amin.favspot.R;
import com.example.amin.favspot.model.Place;

import java.util.List;

public class SavedPlacesAdapter extends RecyclerView.Adapter<SavedPlacesAdapter.NumberViewHolder> {

    private Context context;
    private List<Place> placeList;

    public SavedPlacesAdapter(Context context, List<Place> recipesList) {
        this.context = context;
        this.placeList = recipesList;
    }

    @Override
    public SavedPlacesAdapter.NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutForListItems = R.layout.place_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmeddiatly = false;

        View view = inflater.inflate(layoutForListItems,viewGroup,shouldAttachToParentImmeddiatly);
        NumberViewHolder viewHolder = new NumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SavedPlacesAdapter.NumberViewHolder  holder, int position) {

        Place place = placeList.get(position);
        if (place.getAddress().isEmpty()){
            holder.name.setText("مكان غير معلوم");
        }else {
        holder.name.setText(place.getAddress());
      }
        holder.timestamp.setText(place.getTimestamp());

    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }

    class NumberViewHolder extends RecyclerView.ViewHolder {

        public TextView name,timestamp ;

        // @param itemView
        public NumberViewHolder(View itemView){
            super(itemView);
            name= (TextView)itemView.findViewById(R.id.place_address);
            timestamp= (TextView)itemView.findViewById(R.id.timpstamp);

        }

    }


}