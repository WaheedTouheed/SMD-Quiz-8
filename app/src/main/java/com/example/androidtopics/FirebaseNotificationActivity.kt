package com.example.androidtopics

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.messaging.FirebaseMessaging

class FirebaseNotificationActivity : AppCompatActivity() {

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_notification)

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)

        // Log activity view event
        val viewBundle = Bundle()
        viewBundle.putString(FirebaseAnalytics.Param.SCREEN_NAME, "Firebase Notification Activity")
        viewBundle.putString(FirebaseAnalytics.Param.SCREEN_CLASS, "FirebaseNotificationActivity")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW, viewBundle)

        val subscribeToTopicButton: Button = findViewById(R.id.subscribeToTopicButton)
        val tokenTextView: TextView = findViewById(R.id.tokenTextView)

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM_TOKEN", "Fetching token failed", task.exception)
                tokenTextView.text = "Failed to get token"

                // Log analytics event for token fetch failure
                val failureBundle = Bundle()
                failureBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FCM Token Fetch")
                failureBundle.putString("status", "failed")
                firebaseAnalytics.logEvent("fcm_token_fetch", failureBundle)

                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCM_TOKEN", token)
            tokenTextView.text = "FCM Token:\n$token"
            Toast.makeText(this, "FCM token created.", Toast.LENGTH_SHORT).show()

            // Log analytics event for successful token fetch
            val successBundle = Bundle()
            successBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FCM Token Fetch")
            successBundle.putString("status", "success")
            firebaseAnalytics.logEvent("fcm_token_fetch", successBundle)
        }

        subscribeToTopicButton.setOnClickListener {
            // Log button click event immediately
            val clickBundle = Bundle()
            clickBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Subscribe Topic Button")
            clickBundle.putString("action", "clicked")
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, clickBundle)
            Log.d("Analytics", "Button click event logged")

            FirebaseMessaging.getInstance().subscribeToTopic("news")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FCM_TOPIC", "Successfully subscribed to news topic")
                        Toast.makeText(this, "Subscribed to news topic", Toast.LENGTH_SHORT).show()
                        
                        // Show a feedback notification
                        showSubscriptionNotification()

                        // Log analytics event for successful topic subscription
                        val subBundle = Bundle()
                        subBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Topic Subscription")
                        subBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "news")
                        subBundle.putString("status", "success")
                        firebaseAnalytics.logEvent("topic_subscription", subBundle)
                        Log.d("Analytics", "Topic subscription success event logged")
                    } else {
                        Log.e("FCM_TOPIC", "Failed to subscribe to topic", task.exception)
                        Toast.makeText(this, "Subscription failed", Toast.LENGTH_SHORT).show()

                        // Log analytics event for subscription failure
                        val failSubBundle = Bundle()
                        failSubBundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Topic Subscription")
                        failSubBundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, "news")
                        failSubBundle.putString("status", "failed")
                        failSubBundle.putString("error", task.exception?.message ?: "Unknown error")
                        firebaseAnalytics.logEvent("topic_subscription", failSubBundle)
                        Log.d("Analytics", "Topic subscription failure event logged")
                    }
                }
        }
    }

    private fun showSubscriptionNotification() {
        val channelId = "topic_subscription_channel"
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Topic Subscription", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Subscription Successful")
            .setContentText("You are now subscribed to: news")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(101, notification)
    }
}