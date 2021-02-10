package com.example.taxi.Service;

import com.example.taxi.Common.Common;
import com.example.taxi.Model.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import com.google.firebase.messaging.FirebaseMessagingService;


public class MyFirebaseIdService extends FirebaseInstanceIdService {


    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
       updateTokenToServer(refreshedToken); //refr esh token-> update realtime db
    }

    private void updateTokenToServer(String refreshedToken) {
        FirebaseDatabase db =FirebaseDatabase.getInstance();
        DatabaseReference tokens =db.getReference(Common.token_tb1);

        Token token =new Token(refreshedToken);
        if(FirebaseAuth.getInstance().getCurrentUser() !=null) //user connect must update token
            tokens.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
           .setValue(token);


    }
}
