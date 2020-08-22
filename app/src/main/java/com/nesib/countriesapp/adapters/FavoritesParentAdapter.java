package com.nesib.countriesapp.adapters;

import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nesib.countriesapp.R;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.models.FavoriteCountry;

import java.util.List;

public class FavoritesParentAdapter extends RecyclerView.Adapter<FavoritesParentAdapter.ViewHolder> {
    private List<FavoriteCountry> favoriteCountries;
    private FavoritesChildAdapter favoritesChildAdapter;
    private NavController navController;
    private OnAllItemsDeletedListener listener;
    public FavoritesParentAdapter(List<FavoriteCountry> favoriteCountries, NavController navController) {
        this.favoriteCountries = favoriteCountries;
        this.navController = navController;
    }

    public void setListener(OnAllItemsDeletedListener listener) {
        this.listener = listener;
    }

    public interface OnAllItemsDeletedListener{
        void onAllDeleted();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_favorites_row, parent, false);
        return new ViewHolder(view);
    }

    public interface Callable<String,Void>{
        Void call(String regionName);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteCountry favoriteCountry = favoriteCountries.get(position);
        List<Country> countries = favoriteCountry.getCountryList();
        String regionName = favoriteCountry.getRegionName();


        holder.regionName.setText(capitalizeString(regionName));
        Callable<String,Void> deleteRow = new Callable<String, Void>() {
            @Override
            public Void call(String regionName) {
                return deleteRow(regionName.toLowerCase());
            }
        };

        favoritesChildAdapter = new FavoritesChildAdapter(countries, holder.itemView.getContext().getApplicationContext(), navController,deleteRow);
        holder.childRecyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(holder.itemView.getContext());
        layoutManager.setOrientation(RecyclerView.HORIZONTAL);
        holder.childRecyclerView.setLayoutManager(layoutManager);
        holder.childRecyclerView.setAdapter(favoritesChildAdapter);
    }

    public String capitalizeString(String text) {
        String newText = text;
        newText = text.substring(0, 1).toUpperCase() + text.substring(1);
        return newText;
    }

    public Void deleteRow(String regionName){
        int indexToDelete = 0;
        for(int i = 0;i<favoriteCountries.size();i++){
            FavoriteCountry favoriteCountry = favoriteCountries.get(i);
            if(favoriteCountry.getRegionName().equals(regionName.toLowerCase())){
                indexToDelete = i;
                break;
            }
        }
        favoriteCountries.remove(indexToDelete);
        if(favoriteCountries.size() == 0){
            listener.onAllDeleted();
        }
        notifyItemRemoved(indexToDelete);
        return null;
    }

    @Override
    public int getItemCount() {
        return favoriteCountries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RecyclerView childRecyclerView;
        private TextView regionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
            regionName = itemView.findViewById(R.id.regionName);
        }
    }
}
