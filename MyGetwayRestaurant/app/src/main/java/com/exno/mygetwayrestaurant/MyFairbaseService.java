package com.exno.mygetwayrestaurant;

import android.app.Notification;
        import android.app.NotificationChannel;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.os.Build;
        import android.support.annotation.RequiresApi;
        import android.support.v4.app.NotificationCompat;
        import android.util.Log;

        import com.google.firebase.messaging.FirebaseMessagingService;
        import com.google.firebase.messaging.RemoteMessage;

public class MyFairbaseService extends FirebaseMessagingService
{
    final  static String CHANNEL_ID="1";
    static String TAG="ffffffffbbbbbbbb";
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
      //  Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        // ...
        try {




        if (remoteMessage.getData().get("isnoti").equals("1"))
        {
            Intent dialogIntent = new Intent(this, ActivityNewTask.class);
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("a",""+remoteMessage.getData().get("order_id"));
            dialogIntent.putExtra("b",""+remoteMessage.getData().get("message"));

            startActivity(dialogIntent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            {
                issueNotification(remoteMessage.getData().get("order_id"),remoteMessage.getData().get("message"));
            }
            else
                {
                    addNotification(remoteMessage.getData().get("order_id"),remoteMessage.getData().get("message"));
                }
        }
        else {
            Intent dialogIntent = new Intent(this, ActivityDashBoard.class);
            Log.e(TAG, "From: " + remoteMessage.getFrom());
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            dialogIntent.putExtra("a",""+remoteMessage.getData().get("order_id"));
            dialogIntent.putExtra("b",""+remoteMessage.getData().get("message"));
            dialogIntent.putExtra("c",""+remoteMessage.getData().get("acceptDID"));
            dialogIntent.putExtra("d",""+remoteMessage.getData().get("isAccepted"));

            Log.e(TAG, "Test: " + remoteMessage.getData().get("vehicle_no"));
            dialogIntent.putExtra("vehicle_no",""+remoteMessage.getData().get("vehicle_no"));
            dialogIntent.putExtra("reg_id",""+remoteMessage.getData().get("reg_id"));
            dialogIntent.putExtra("contact_no",""+remoteMessage.getData().get("contact_no"));
            dialogIntent.putExtra("drive_name",""+remoteMessage.getData().get("drive_name"));
            dialogIntent.putExtra("email_id",""+remoteMessage.getData().get("email_id"));
            startActivity(dialogIntent);
        }
        }catch (Exception e){
Log.d("exnodata",""+e.getMessage());
        }

        Log.d(TAG, "ALL : " + remoteMessage.getData().get("title"));


        //  addNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        // addNotification2(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.

            } else {
                // Handle message within 10 seconds

            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void addNotification2aa(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true); // set false to disable badges, Oreo exclusive
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);





    }


    void issueNotification(String title,String msg)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, ActivityDashBoard.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);

        // make the channel. The method has been discussed before.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addNotification2aa("CHANNEL_1", "Echannel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        // the check ensures that the channel will only be made
        // if the device is running Android 8+

        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        // the second parameter is the channel id.
        // it should be the same as passed to the makeNotificationChannel() method
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notification
                .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
                .setContentTitle("Order Id : "+title)
                .setContentText(msg)
                .setSubText(msg)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent)
                .setNumber(3); // this shows a number in the notification dots

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());
        // it is better to not use 0 as notification id, so used 1.
    }




    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(String msg,String title)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, ActivityDashBoard.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int myColor =getResources().getColor(R.color.colorAccent);
        Notification mNotification =new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setColor(myColor)
                .setContentTitle("Order Id : "+title)
                .setContentText(msg)
                .setSubText(msg)
                .setLargeIcon(bitmap)
                .setContentIntent(viewPendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, mNotification);
    }



}