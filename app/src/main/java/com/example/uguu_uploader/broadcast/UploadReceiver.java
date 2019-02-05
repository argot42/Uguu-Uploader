package com.example.uguu_uploader.broadcast;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.example.uguu_uploader.LoginActivity;
import com.example.uguu_uploader.R;
import com.example.uguu_uploader.model.Upload;

public class UploadReceiver extends BroadcastReceiver {
    public static final String UPLOAD_SUCCESSFUL = "com.example.uguu_uploader.UPLOAD_SUCCESSFUL";
    public static final String UPLOAD_FAILED = "com.example.uguu_uploader.UPLOAD_FAILURE";

    @Override
    public void onReceive(final Context context, Intent intent) {
        Upload upload;
        Notification notification;
        Intent i;
        PendingIntent resultPendingIntent;
        TaskStackBuilder stackBuilder;
        NotificationManagerCompat nManager;

        if (intent == null)
            return;
        String action = intent.getAction();
        if (action == null)
            return;
        switch(action) {
            case UPLOAD_SUCCESSFUL:
                Log.d("BroadcastReceiver", "Upload done! :)");
                upload = intent.getExtras().getParcelable("upload");

                // clickable notification
                i = new Intent(context, LoginActivity.class);
                stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(i);
                resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                notification = new NotificationCompat.Builder(context, context.getString(R.string.uploadChannelID))
                        .setSmallIcon(R.drawable.ic_upload)
                        .setContentTitle("Upload finished")
                        .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(upload.getName() + " was uploaded to " + upload.getUrl()))
                        .setContentIntent(resultPendingIntent)
                        .build();

                nManager = NotificationManagerCompat.from(context);
                nManager.notify(0, notification);

                break;
            case UPLOAD_FAILED:
                Log.d("BroadcastReceiver", "Upload failed :(");
                upload = intent.getExtras().getParcelable("upload");

                // clickable notification
                i = new Intent(context, LoginActivity.class);
                i.putExtra("username", upload.getUser());
                stackBuilder = TaskStackBuilder.create(context);
                stackBuilder.addNextIntentWithParentStack(i);
                resultPendingIntent = stackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT);

                notification = new NotificationCompat.Builder(context, context.getString(R.string.uploadChannelID))
                        .setSmallIcon(R.drawable.ic_upload_failed)
                        .setContentTitle("Upload failed")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(upload.getName() + " failed to upload :-("))
                        .setContentIntent(resultPendingIntent)
                        .build();

                nManager = NotificationManagerCompat.from(context);
                nManager.notify(0, notification);

                break;
        }
    }
}
