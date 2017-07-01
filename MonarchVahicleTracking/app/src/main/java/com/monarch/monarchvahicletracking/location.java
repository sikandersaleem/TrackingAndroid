package com.monarch.monarchvahicletracking;

public class location {
    public double latitude;
    public double longitude;
    public String lastaddress;

    public location()
    {
    }
    public location(double lat,double logi, String lastaddress)
    {
        this.latitude=lat;
        this.longitude=logi;
        this.lastaddress=lastaddress;
    }
}
