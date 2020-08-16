package com.nesib.countriesapp.adapters;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.database.DatabaseHelper;
import com.nesib.countriesapp.models.Country;
import com.nesib.countriesapp.ui.favorites.FavoritesFragmentDirections;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class FavoritesChildAdapter extends RecyclerView.Adapter<FavoritesChildAdapter.ViewHolder> {
    private List<Country> countryList;
    private DatabaseHelper db;
    private List<Country> countryCodes;
    private NavController navController;
    private View view;
    private FavoritesParentAdapter.Callable<String,Void> deleteRow;

    public FavoritesChildAdapter(List<Country> countryList, Context context, NavController navController, FavoritesParentAdapter.Callable<String,Void> deleteRow) {
        this.countryList = countryList;
        db = DatabaseHelper.getInstance(context);
        this.navController = navController;
        this.deleteRow = deleteRow;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_favorites_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.countryName.setText(country.getName());
        holder.area.setText(country.getArea() + "");
        holder.population.setText(country.getPopulation() + "");
        holder.countryFullName.setText(country.getAltSpellings()[0]);
        GlideToVectorYou
                .init()
                .with(holder.itemView.getContext())
                .load(Uri.parse(country.getFlag()), holder.flagImage);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView countryName, countryFullName, area, population;
        public ImageButton heartButton;
        public ImageView flagImage;
        public View overlay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countryName = itemView.findViewById(R.id.countryName);
            countryFullName = itemView.findViewById(R.id.countryFullName);
            area = itemView.findViewById(R.id.area);
            population = itemView.findViewById(R.id.population);
            flagImage = itemView.findViewById(R.id.flagImage);
            heartButton = itemView.findViewById(R.id.heartButton);
            overlay = itemView.findViewById(R.id.overlay);

            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Country currentCountry = countryList.get(getAdapterPosition());
                    db.deleteCountry(currentCountry);
                    countryList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());
                    if(countryList.size() == 0){
                        deleteRow.call(currentCountry.getRegion());
                    }
                    Animation scaleInAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in);
                    Animation scaleOutAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_out);
                    heartButton.startAnimation(scaleInAnimation);
                    heartButton.startAnimation(scaleOutAnimation);
                }
            });

            overlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FavoritesFragmentDirections.ActionNavigationFavoritesToNavigationDetails action =
                            FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails();
                    Country currentCountry = countryList.get(getAdapterPosition());
                    action.setCountry(currentCountry);
                    action.setFromSearch(true);
                    navController.navigate(action);
                }
            });
        }
    }

}
