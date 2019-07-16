package vn.com.it.truongpham.appnote;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.Random;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LifecycleObserver;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements LifecycleObserver {
    private final String ADMIN_CHANNEL_ID ="admin_channel";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        if(MyApplication.checkRunningApp){
            EventBus.getDefault().postSticky(remoteMessage);
        }
        else
        {
            Intent intent = new Intent(this, NotificationActivity.class);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            int notificationID = new Random().nextInt(3000);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setupChannels(notificationManager);
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, notificationID, intent, PendingIntent.FLAG_ONE_SHOT);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_app);

            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, ADMIN_CHANNEL_ID)
                    .setSmallIcon(R.drawable.icon_app)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(remoteMessage.getData().get("title"))
                    .setContentText(remoteMessage.getData().get("message"))
                    .setAutoCancel(true)
                    .setSound(uri)
                    .setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            notificationManager.notify(notificationID, notificationBuilder.build());
        }
   
   
    }

    public boolean isRunningForeground() {
        String topActivityClassName = getTopActivityName(this);
        if (topActivityClassName != null && topActivityClassName.startsWith("vn.com.it.truongpham.appnote")) {

            return true;
        } else {

            return false;
        }
    }

    public String getTopActivityName(Context context) {
        String topActivityClassName = null;
        ActivityManager activityManager =
                (ActivityManager) (context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = activityManager.getRunningTasks(1);
        if (runningTaskInfos != null) {
            ComponentName f = runningTaskInfos.get(0).topActivity;
            topActivityClassName = f.getClassName();
        }
        return topActivityClassName;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setupChannels(NotificationManager notificationManager) {
        CharSequence adminChannelName ="new notification";

        String adminChannelDescription= "Device to device notification ";
        NotificationChannel adminChannel;
        adminChannel=new NotificationChannel(ADMIN_CHANNEL_ID,adminChannelName,NotificationManager.IMPORTANCE_HIGH);
        adminChannel.setDescription(adminChannelDescription);
        adminChannel.enableLights(true);
        adminChannel.setLightColor(Color.RED);
        adminChannel.enableVibration(true);
        if(notificationManager!=null){
            notificationManager.createNotificationChannel(adminChannel);
        }
    }

}
