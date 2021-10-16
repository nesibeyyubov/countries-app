package com.nesib.countriesapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.database.DatabaseHelper;
import com.nesib.countriesapp.models.Country;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {
    private List<Country> countryList = new ArrayList<>();
    private OnItemClickListener listener;
    private List<Country> countryCodes;
    private Context context;
    private DatabaseHelper db;

    public SearchResultAdapter(List<Country> countryCodes, Context context) {
        this.countryCodes = countryCodes;
        this.context = context;
        db = DatabaseHelper.getInstance(context);
    }

    public void setCountryCodes(List<Country> countryCodes) {
        this.countryCodes = countryCodes;
    }

    public void setCountryList(List<Country> countryList) {
        this.countryList = countryList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.country_search_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.countryName.setText(country.getName());
        holder.capitalName.setText(country.getCapital());
        String area = Double.toString(country.getArea());
        area = area.substring(0, area.length() - 2);
        holder.area.setText(area);


        DecimalFormat decimalFormat = new DecimalFormat("#,##0.###");
        holder.population.setText(decimalFormat.format(country.getPopulation()));
        try{
            GlideToVectorYou
                    .init()
                    .with(holder.itemView.getContext())
                    .load(Uri.parse(country.getFlag()), holder.flagImage);
            holder.flagImage.setTransitionName(country.getFlag());

        }catch (Exception exception){

        }

        if(isFavorite(country)){
            holder.heartButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_favorites_filled,null));
        }
        else{
            holder.heartButton.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(),R.drawable.ic_favorites_outline,null));
        }

    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView flagImage;
        public ImageButton heartButton;
        public TextView countryName, capitalName, regionName, population, area;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.flagImage);
            countryName = itemView.findViewById(R.id.countryName);
            capitalName = itemView.findViewById(R.id.capitalName);
            population = itemView.findViewById(R.id.population);
            area = itemView.findViewById(R.id.area);
            heartButton = itemView.findViewById(R.id.heartButton);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(getAdapterPosition());
                }
            });
            heartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onHeartClick(getAdapterPosition());
                    if (isFavorite(countryList.get(getAdapterPosition()))) {
                        heartButton.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_favorites_outline));
                    } else {
                        heartButton.setImageDrawable(view.getContext().getDrawable(R.drawable.ic_favorites_filled));
                    }
                    Animation scaleInAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_in);
                    Animation scaleOutAnimation = AnimationUtils.loadAnimation(itemView.getContext(), R.anim.scale_out);
                    heartButton.startAnimation(scaleInAnimation);
                    heartButton.startAnimation(scaleOutAnimation);
                    countryCodes = db.getCountryCodes();
                }
            });
        }

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onClick(int position);
        void onHeartClick(int position);
    }

    public boolean isFavorite(Country currentCountry) {
        boolean flag = false;
        for (Country code : countryCodes) {
            if (currentCountry.getFlag().equals(code.getFlag())) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
