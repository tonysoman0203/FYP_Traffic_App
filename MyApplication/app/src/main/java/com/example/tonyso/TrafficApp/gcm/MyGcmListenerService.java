/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.tonyso.TrafficApp.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.tonyso.TrafficApp.MainActivity;
import com.example.tonyso.TrafficApp.R;
import com.example.tonyso.TrafficApp.utility.ShareStorage;
import com.example.tonyso.TrafficApp.utility.StoreObject;
import com.google.android.gms.gcm.GcmListenerService;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        Log.e(TAG, "Received Msg From GCM : " + data.toString());
        String title = data.getString("title");
        String $data = data.getString("message");
        String flag = data.getString("flag");
        String user_id = data.getString("user_id");
        int id;
        if (user_id == null) {
            id = -1;
        } else {
            id = Integer.parseInt(data.getString("user_id"));
        }

        Log.d(TAG, "From: " + from);
        Log.d(TAG, "Message: " + $data);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "Flag: " + flag);
        Log.d(TAG, "User ID = " + user_id);

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else if (flag != null && flag.equals("register")) {
            // normal downstream message.
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, GCMStartPreference.TAG_ID, id), ShareStorage.SP.PrivateData, this);
        } else if (flag != null && flag.equals("update")) {
            // normal downstream message)
            ShareStorage.saveData(ShareStorage.StorageType.SHARED_PREFERENCE,
                    new StoreObject<Object>(false, GCMStartPreference.TAG_ID, id), ShareStorage.SP.PrivateData, this);
        }
        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(title, $data);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    private void sendNotification(String title, String message) {
        if (message.contains("<br>")) {
            message = message.replace("<br>", "\n");
        }
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification notificationBuilder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new Notification.BigTextStyle().bigText(message))
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder);
    }
}
