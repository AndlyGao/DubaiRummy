package com.gamegards.dubairummy.Firebase;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.gamegards.dubairummy.Activity.Homepage;
import com.gamegards.dubairummy.Activity.Splashscreen;
import com.gamegards.dubairummy.R;
import com.gamegards.dubairummy._TeenPatti.PublicTable_New;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import androidx.core.app.NotificationCompat;

import static android.media.AudioManager.STREAM_ALARM;
import static com.android.volley.VolleyLog.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String, String> params = remoteMessage.getData();
            String title = "";
            String msg = "";
            String table_id = "";
            try {
                JSONObject object = new JSONObject(params);

                String objectInner = object.getString("message");
                JSONObject objectMSg = new JSONObject(objectInner);

                title = objectMSg.getString("title");
                msg = objectMSg.getString("msg");

                if (objectMSg.has("table_id")) {
                    table_id = objectMSg.getString("table_id");
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            sendNotification(title,table_id);
        }
        else {

           String title = remoteMessage.getNotification().getTitle();
            String msg = remoteMessage.getNotification().getBody();

            if (title != null && !title.equals(""))
                NoficationManager(this, msg, title, "");

        }
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

        private void sendNotification(String messageBody,String table_id) {
            Intent intent;
        if (table_id.length() > 0){

             intent = new Intent(this, PublicTable_New.class);

        }else{

            intent = new Intent(this, Homepage.class);
        }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                    PendingIntent.FLAG_ONE_SHOT);

            String channelId = getString(R.string.default_notification_channel_id);
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.drawable.app_icon)
                            .setContentTitle(getString(R.string.app_name))
                            .setContentText(messageBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            // Since android Oreo notification channel is needed.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(channelId,
                        "Channel human readable title",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }

    public void NoficationManager(Context context, String title, String heading, String rozarid) {

        NotificationCompat.Builder mBuilder = null;


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        long[] vibrate = new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400};


        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";


        try {

            Intent intent;
            intent = new Intent(this, Splashscreen.class);

            intent.putExtra("type", "business");

//                intent.putExtra("orderid",Orderid);
//                intent.putExtra("rozer_id",rozarid);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                PendingIntent detailsPendingIntent = PendingIntent.getActivity(
                        getApplicationContext(),
                        0,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT
                );

//                Uri alarmSound = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.notification);
                Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                mBuilder = new NotificationCompat.Builder(context, channelId)
                        .setSmallIcon(R.drawable.app_icon)
                        .setContentTitle(heading)
                        .setContentText(title)
                        .setAutoCancel(true)
                        .setSound(alarmSound, STREAM_ALARM)
                        .setContentIntent(detailsPendingIntent);


                AudioAttributes attributes = new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                        .build();

                if (notificationManager != null) {
                    List<NotificationChannel> channelList = notificationManager.getNotificationChannels();

                    for (int i = 0; channelList != null && i < channelList.size(); i++) {
                        notificationManager.deleteNotificationChannel(channelList.get(i).getId());
                    }
                }

                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);


                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(vibrate);
                mChannel.setSound(alarmSound, attributes);


                notificationManager.createNotificationChannel(mChannel);
            }

            notificationManager.notify(notificationId, mBuilder.build());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
    }
}
