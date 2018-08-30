package com.v1rex.smartincubator.Services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.support.v4.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.v1rex.smartincubator.Activities.SendMessagesActivity
import com.v1rex.smartincubator.R

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)


        var nameUser : String = ""
        var specialty_need : String = ""
        var type : String = ""
        var receiverUid : String = ""

        if(remoteMessage!!.data.size > 0){
            nameUser = remoteMessage!!.data.get("nameUser").toString()
            specialty_need = remoteMessage!!.data.get("specialty_need").toString()
            type = remoteMessage!!.data.get("type").toString()
            receiverUid = remoteMessage!!.data.get("receiverUid").toString()
        }


    }

    private fun sendNotif(messageTitle : String, messageBody: String , nameUser : String , specialty_need : String, type : String , receiverUid : String){
        val intent = Intent(this, SendMessagesActivity::class.java)

        intent.putExtra("name", nameUser)
        intent.putExtra("needSpecialty", specialty_need)
        intent.putExtra("userId", receiverUid)
        intent.putExtra("type", type)

        val pendingIntent = PendingIntent.getActivity(applicationContext, 0, intent , PendingIntent.FLAG_ONE_SHOT)

        var defaultNotifSound : Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        var notificationBuilder : NotificationCompat.Builder = NotificationCompat.Builder(this)
        notificationBuilder.setSmallIcon(R.drawable.ic_message)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultNotifSound)
                .setContentIntent(pendingIntent)

        var notificationManager : NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, notificationBuilder.build())
    }
}