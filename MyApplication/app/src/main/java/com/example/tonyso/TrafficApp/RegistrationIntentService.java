package com.example.tonyso.TrafficApp;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.tonyso.TrafficApp.gcm.GCMStartPreference;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by TonySo on 22/3/16.
 */
public class RegistrationIntentService extends IntentService{

    private static final String TAG = RegistrationIntentService.class.getCanonicalName();
    private static final String[] TOPICS = {"global"};


    public RegistrationIntentService(String name) {
        super(name);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {

                //向GCM註冊(產生Token)
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken("Sender ID", GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                //App Server註冊(回傳Token)
                sendRegistrationToServer(token);
                //訂閱發怖主題
                subscribeTopics(token);
                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                        new StoreObject<Object>(false, GCMStartPreference.SENT_TOKEN_TO_SERVER,true ),ShareStorage.SP.PrivateData,this);

                // [END register_for_gcm]

            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, GCMStartPreference.SENT_TOKEN_TO_SERVER, false), ShareStorage.SP.PrivateData, this);

        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(GCMStartPreference.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

    }

    private void subscribeTopics(String token) {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            try {
                pubSub.subscribe(token, "/topics/" + topic, null);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendRegistrationToServer(String token) {

    }

}
