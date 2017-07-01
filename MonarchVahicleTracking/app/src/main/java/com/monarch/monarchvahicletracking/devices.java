package com.monarch.monarchvahicletracking;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

@IgnoreExtraProperties
public  class devices {

    public String category ;
    public String contact;
    public List<DeviceGeofence> deviceGeofence;
    public Integer groupId;
    public Integer id;
    public String lastUpdate;
    public String date;
    public String model;
    public String name;
    public String phone;
    public Integer positionId;
    public String status;
    public String uniqueId;

    public devices(){

    }
}
