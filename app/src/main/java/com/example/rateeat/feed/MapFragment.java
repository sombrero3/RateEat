package com.example.rateeat.feed;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.rateeat.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapFragment extends Fragment {
    List<Address> addressList=null;
    SearchView sv;
    Button btn;
    String addressString,source;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //init view
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        //init map fragment
        SupportMapFragment supportMapFragment=(SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.google_map);
        source = MapFragmentArgs.fromBundle(getArguments()).getSource();
        btn=view.findViewById(R.id.button);
        btn.setVisibility(View.INVISIBLE);
        addressString="";
        //async map
        supportMapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                sv=view.findViewById(R.id.sv);
                //when map is ready
                sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        String location=query;
                        Log.d("tag", location);

                        Geocoder geocoder=new Geocoder(getContext());

                        try {
                            addressList=geocoder.getFromLocationName(location,1);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        if(addressList==null || addressList.isEmpty()){
                            Toast.makeText(getContext(),"No location was found",Toast.LENGTH_LONG).show();
                        }else{
                        Address address=addressList.get(0);
                        Log.d("TAG2", address.toString());
                        addressString=address.getAddressLine(0);
                        Log.d("addressString", addressString);
                        LatLng latLng=new LatLng(address.getLatitude(), address.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(latLng).title(addressString));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,10));
                            sv.setVisibility(View.INVISIBLE);
                            btn.setVisibility(View.VISIBLE);
                        }

                        btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(source.equals("add")) {
                                    Navigation.findNavController(v).navigate(MapFragmentDirections.actionMapFragmentToAddReviewFragment(addressString, query));
                                }else{
                                    String args[] = source.split(" ");
                                    Navigation.findNavController(v).navigate(MapFragmentDirections.actionMapFragmentToEditReviewFragment(args[1],addressString,query));
                                }
                            }
                        });
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        return false;
                    }
                });

//                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(@NonNull LatLng latLng) {
//                        //when clicked on map
//                        //init marker options
//                        MarkerOptions markerOptions=new MarkerOptions();
//                        //set position
//                        markerOptions.position(latLng);
//                        //set title of marker
//                        markerOptions.title(latLng.latitude+":"+ latLng.longitude);
//                        //remove all marker
//                        googleMap.clear();
//                        //animating zoom
//                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                                latLng, 10
//                        ));
//                        //add marker on map
//                        googleMap.addMarker(markerOptions);
//                    }
//                });
            }
        });


        return view;
    }

}