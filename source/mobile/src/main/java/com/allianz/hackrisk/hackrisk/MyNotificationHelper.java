package com.allianz.hackrisk.hackrisk;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jim on 9/28/13.
 */
public class MyNotificationHelper {
    public static final int DOWNLOAD_NOTIFICATION_ID = 1;

    public static void displayNotification(Context context, String contentTitle, String contentText, int progress)  //, Location location)
    {
       // String mapUri =
       //         String.format("http://maps.google.com/maps?q=%.6f,%.6f",
       //                 location.getLatitude(), location.getLongitude());
        //Uri uri = Uri.parse(mapUri);

        RiskObject r1 = new RiskObject();
        r1.Header = "Road Work at Charing Cross.";
        r1.Description = "Road work start on sunday till early monday morning, please find alternative route when travelling.";
        r1.Location = "London , United Kingdom";
        r1.Date = "31/05/2015";

        ArrayList<RiskObject> ro = new ArrayList<RiskObject>();
        ro.add(r1);

        Intent intent = new Intent(context, RiskListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Bundle b = new Bundle();

        b.putSerializable(MainActivity.EXTRA_CRIMEAPIRESULT, ro);

        intent.putExtra(MainActivity.EXTRA_CRIMEAPIRESULT, b);


        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, 0);




        //String message = formatNotificationMessage(location.getProvider(), location.getLatitude(), location.getLongitude());

        Notification.Builder builder = new Notification.Builder(context);
        builder.setContentTitle(contentTitle)
                .setContentText(contentText)
                .setSmallIcon(R.drawable.car_icon_2)
                .setContentIntent(contentIntent);
                //.setProgress(100, progress, false);


        Notification notification = builder.getNotification();

        NotificationManager mgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

//    String tag = this.getClass().getName();
        mgr.notify(DOWNLOAD_NOTIFICATION_ID, notification);
    }

    public static void removeNotification(Context context, String contentTitle, String contentText) {

        try {
            NotificationManager mgr =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(context, notificationSound);
             r.play();

            Notification.Builder builder = new Notification.Builder(context);
            builder.setContentTitle(contentTitle)
                    .setContentText(contentText)
                    .setSound(notificationSound)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSmallIcon(R.drawable.car_icon_2);
                    //.setProgress(100, 100, false);
            //.setContentIntent(pendingIntent);

            Notification notification = builder.getNotification();
            notification.sound = notificationSound;

            //notification.defaults = Notification.DEFAULT_ALL;

            mgr.notify(DOWNLOAD_NOTIFICATION_ID, notification);


            Thread.sleep(5000);

            mgr.cancel(DOWNLOAD_NOTIFICATION_ID);
        }
        catch (Exception ex) {

        }
    }

    //private static String formatNotificationMessage(String provider, double latitude, double longitude) {
    //    return String.format("%.6f/%.6f Provider:%s", latitude, longitude, provider);
    //}

}