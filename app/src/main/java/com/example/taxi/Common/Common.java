package com.example.taxi.Common;

import android.location.Location;

import com.example.taxi.Remote.FCMClient;
import com.example.taxi.Remote.IFCMService;
import com.example.taxi.Remote.IGoogleAPI;
import com.example.taxi.Remote.RetrofitClient;
import com.example.taxi.User;

public class Common {

    public static final String driver_tb1 ="Drivers";
    public static final String user_driver_tb1 ="DriversInformation";
    public static final String user_client_tb1 ="clientsInformation";
    public static final String pickup_request_tb1 ="PickupRequest";
    public static final String token_tb1 ="Tokens";

    public static User currentUser;


    public static Location mLastLocation=null;




    public static final String baseURL="https://maps.googleapis.com";
    public static final String fcmURL="https://fcm.googleapis.com/";

    public static IGoogleAPI getGoogleAPI(){

        return RetrofitClient.getClient(baseURL).create(IGoogleAPI.class);

    }


    public static IFCMService getFCMService(){

        return FCMClient.getClient(fcmURL).create(IFCMService.class);

    }
}
