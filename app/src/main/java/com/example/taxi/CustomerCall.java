package com.example.taxi;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.taxi.Common.Common;
import com.example.taxi.Model.FCMResponse;
import com.example.taxi.Model.Notification;
import com.example.taxi.Model.Sender;
import com.example.taxi.Model.Token;
import com.example.taxi.Remote.IFCMService;
import com.example.taxi.Remote.IGoogleAPI;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomerCall extends AppCompatActivity {

    TextView txtTime,txtAdress,txtDistance;

    Button btnCancel,btnAccept;

      MediaPlayer  mediaPlayer;

      IGoogleAPI mService;
      IFCMService mFCMService;

      String customerId;

      double lat,lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_call);

        mService=Common.getGoogleAPI();
        mFCMService=Common.getFCMService();

        txtAdress=findViewById(R.id.txtAdress);
        txtDistance=findViewById(R.id.txtDistance);
        txtTime=findViewById(R.id.txtTime);

        btnAccept=findViewById(R.id.btnAccept);
        btnCancel=findViewById(R.id.btnDecline);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(customerId))

                    cancelRequest(customerId);

            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             Intent intent = new Intent(CustomerCall.this,DriverTracking.class);
             intent.putExtra("lat",lat);
             intent.putExtra("lng",lng);
             intent.putExtra("customerId",customerId);

             startActivity(intent);
             finish();
    }
});




        mediaPlayer = MediaPlayer.create(this,R.raw.ring);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        if (getIntent() !=null){

         lat =getIntent().getDoubleExtra("lat",-1.0);
         lng =getIntent().getDoubleExtra("lng",-1.0);
        customerId=getIntent().getStringExtra("customer");

        getDirection(lat,lng);
        }

    }

    private void cancelRequest(String customerId) {
        Token token =new Token(customerId);

        Notification notification =new Notification("Cancel","Driver has cancelled your request");
        Sender sender =new Sender(token.getToken(),notification);

        mFCMService.sendMessage(sender)
                .enqueue(new Callback<FCMResponse>() {
                    @Override
                    public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {
                        if (response.body().success==1){

                            Toast.makeText(CustomerCall.this,"Cancelled",Toast.LENGTH_SHORT);
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<FCMResponse> call, Throwable t) {

                    }
                });

    }


    private void getDirection(double lat,double lng ){
        String requestApi = null;
        try {

            requestApi ="https://maps.googleapis.com/maps/api/directions/json?"+
                    "mode=driving&"+
                    "transit_routing_perference=less_driving&"+
                    "origin="+ Common.mLastLocation.getLatitude()+","+Common.mLastLocation.getLongitude()+"&"+
                    "destination="+lat+","+lng+"&"+
                    "key="+getResources().getString(R.string.google_direction_api);
            Log.d("EDMTDEV",requestApi); //print Url for debug

            mService.getPath(requestApi)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                JSONObject jsonObject =new JSONObject (response.body().toString());

                                JSONArray routes=jsonObject.getJSONArray("routes");

                                //get routes
                                JSONObject object =routes.getJSONObject(0);

                                //get array with name legs
                                JSONArray legs = object.getJSONArray("legs");

                                //get first elt of legs array
                                JSONObject legsobject =legs.getJSONObject(0);

                                //get Distance
                                JSONObject distance =legsobject.getJSONObject("distance");
                                txtDistance.setText(distance.getString("text"));

                                //get Time
                                JSONObject time =legsobject.getJSONObject("duration");
                                txtTime.setText(time.getString("text"));


                                //get Address
                                String adress =legsobject.getString("adress");
                                txtAdress.setText(adress);

                            }catch (JSONException e)
                            {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Toast.makeText(CustomerCall.this,""+t.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        mediaPlayer.release();
        super.onStop();
    }

    @Override
    protected void onPause() {
        mediaPlayer.release();
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();

    }
}