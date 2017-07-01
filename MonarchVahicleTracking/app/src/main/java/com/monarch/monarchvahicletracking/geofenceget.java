package com.monarch.monarchvahicletracking;


import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class geofenceget {

    public String name;
    public String type;
    public ArrayList<locations> locations;

    public void geofenceget()
    {

    }
    public void geofenceget(String name,String type, ArrayList<locations> locations)
    {
        this.name =name;
        this.type=type;
        this.locations=locations;
    }
}
