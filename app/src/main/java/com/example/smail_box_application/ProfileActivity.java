package com.example.smail_box_application;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);          // showing content layout of Profile page

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(ProfileActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ProfileActivity.this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        Button userbutton = findViewById(R.id.userbutton);          // creating object and initialize userbutton
        Button uspsbutton = findViewById(R.id.uspsbutton);
        userbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeNotification("User accessed to mailbox");
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
            }
        });

        uspsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                makeNotification("USPS accessed to mailbox");
                startActivity(new Intent(getApplicationContext(), USPSHomeActivity.class));
                finish();
            }
        });

    }

    public void makeNotification(String contentText){
        String channelID = "CHANNEL_ID_NOTIFICATION";
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),channelID);
        builder.setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("SmailBox")
            .setContentText(contentText)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent intent = new Intent(getApplicationContext(), NotificationActivity.class);    // new notification open directly to Notification page
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notificationData","contentText");

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder.setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = notificationManager.getNotificationChannel(channelID);
            if(notificationChannel == null) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                notificationChannel = new NotificationChannel(channelID,"Some description", importance);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        notificationManager.notify(0,builder.build());
    }

}