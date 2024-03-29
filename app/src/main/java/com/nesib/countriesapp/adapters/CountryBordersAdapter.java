package com.nesib.countriesapp.adapters;

import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.nesib.countriesapp.R;
import com.nesib.countriesapp.models.Country;

import java.util.List;

public class CountryBordersAdapter extends RecyclerView.Adapter<CountryBordersAdapter.ViewHolder> {
    private List<Country> countryList;
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onClick(Country selectedCountry);
    }

    public CountryBordersAdapter(List<Country> countryList){
        this.countryList = countryList;
    }

    @NonNull
    @Override
    public CountryBordersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.border_country_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryBordersAdapter.ViewHolder holder, int position) {
        Country country = countryList.get(position);
        holder.countryName.setText(country.getName());
        GlideToVectorYou
                .init()
                .with(holder.itemView.getContext())
                .load(Uri.parse(country.getFlag()),holder.flagImage);
    }

    @Override
    public int getItemCount() {
        return countryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView flagImage;
        public TextView countryName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            flagImage = itemView.findViewById(R.id.borderCountryImage);
            countryName = itemView.findViewById(R.id.borderCountryName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Animation scaleOut = AnimationUtils.loadAnimation(view.getContext(),R.anim.scale_out);
                    itemView.startAnimation(scaleOut);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listener.onClick(countryList.get(getAdapterPosition()));
                        }
                    },200);

                }
            });
        }
    }


}
