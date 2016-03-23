package com.example.tonyso.TrafficApp;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.tonyso.TrafficApp.gcm.GCMStartPreference;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TonySo on 22/3/16.
 */
public class RegistrationIntentService extends IntentService {

    private static final String TAG = RegistrationIntentService.class.getCanonicalName();
    private static final String[] TOPICS = {"global"};
    private boolean isTokenSendToServer = false;
    private int clientId;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {

                //向GCM註冊(產生Token)
                InstanceID instanceID = InstanceID.getInstance(this);
                String SENDER_ID = "649022348275";
                String token = instanceID.getToken(SENDER_ID, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                //App Server註冊(回傳Token)
                sendRegistrationToServer(token, getClientId());
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

    private void sendRegistrationToServer(String token, int _id) {
        try {
            StringBuilder content = new StringBuilder();
            URL url = new URL("http://101.78.175.101:3480/php_gcm/registerGCM.php");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);
            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter("client_id", token)
                    .appendQueryParameter("id", String.valueOf(_id));
            String query = builder.build().getEncodedQuery();
            Log.d(TAG, query);
            OutputStream os = urlConnection.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
            urlConnection.connect();

            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = rd.readLine()) != null) {
                Log.d(TAG, "Received with " + content.append(line).append("\n").toString());
            }
            rd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getClientId() {
        return ShareStorage.getInteger(GCMStartPreference.TAG_ID, ShareStorage.SP.PrivateData, this);
    }
}
