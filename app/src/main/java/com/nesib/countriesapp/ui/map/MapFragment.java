package com.nesib.countriesapp.ui.map;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nesib.countriesapp.R;

import java.util.HashMap;
import java.util.Map;

public class MapFragment extends Fragment implements View.OnClickListener {
    private MapView mapView;
    private GoogleMap mGoogleMap;
    private float[] latlng;
    private Button normal,satellite,hybrid,terrain,focusedButton;
    private ImageButton backButton;
    private Button [] settingButtons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = view.findViewById(R.id.mapView);
        normal = view.findViewById(R.id.normalBtn);
        hybrid = view.findViewById(R.id.hybridBtn);
        terrain = view.findViewById(R.id.terrainBtn);
        satellite = view.findViewById(R.id.satelliteBtn);
        backButton = view.findViewById(R.id.backButton);

        settingButtons = new Button[]{normal,satellite,terrain,hybrid};
        for(Button btn: settingButtons){
            btn.setOnClickListener(this);
        }
        focusedButton = settingButtons[0];
        focusedButton.setTextColor(getResources().getColor(R.color.colorPrimary));
        focusedButton.setBackground(getResources().getDrawable(R.drawable.map_setttings_item_bg_filled));
        backButton.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapsInitializer.initialize(getActivity());
        mapView.onCreate(savedInstanceState);
        latlng = MapFragmentArgs.fromBundle(getArguments()).getLatLng();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mGoogleMap = googleMap;

                MarkerOptions markerOptions = new MarkerOptions();
                LatLng latLng = new LatLng(latlng[0],latlng[1]);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,5f));
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onClick(View view) {
        boolean sameButtonPressed = false;
        switch (view.getId()){
            case R.id.normalBtn:
                sameButtonPressed = changeButtonFocusTo(settingButtons[0]);
                if(!sameButtonPressed){
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.satelliteBtn:
                sameButtonPressed = changeButtonFocusTo(settingButtons[1]);
                if(!sameButtonPressed){
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }
                break;
            case R.id.terrainBtn:
                sameButtonPressed = changeButtonFocusTo(settingButtons[2]);
                if(!sameButtonPressed){
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                }
                break;
            case R.id.hybridBtn:
                sameButtonPressed = changeButtonFocusTo(settingButtons[3]);
                if(!sameButtonPressed){
                    mGoogleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                }
                break;
            case R.id.backButton:
                requireActivity().getOnBackPressedDispatcher().onBackPressed();
                break;

        }
    }

    public boolean changeButtonFocusTo(Button buttonToFocus){
        if(focusedButton == buttonToFocus){
            return true;
        }
        buttonToFocus.setTextColor(getResources().getColor(R.color.colorPrimary));
        buttonToFocus.setBackground(getResources().getDrawable(R.drawable.map_setttings_item_bg_filled));

        focusedButton.setTextColor(Color.WHITE);
        focusedButton.setBackground(getResources().getDrawable(R.drawable.map_setttings_item_bg));

        this.focusedButton = buttonToFocus;
        return false;
    }
}