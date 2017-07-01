package com.monarch.monarchvahicletracking;

public class history {
    public double latitude;
    public double longitude;
    public String time;

    public history(){

    }
    public history(String time, double latitude, double longitude){
       this.time=time;
       this.latitude=latitude;
        this.longitude=longitude;
    }
}
