package com.example.david.sorttextureapp;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by David on 8/3/16.
 */
public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String REG_TOKEN = "REG_TOKEN";
    @Override
    public void onTokenRefresh() {

        String recentToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("REG_TOKEN",recentToken);
    }
}
