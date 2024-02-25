package com.example.aston_intensiv_1

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity


const val CHANNEL_ID = "100132"
class MainActivity : AppCompatActivity() {
    private var receiver = TimerReceiver()
    inner class TimerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == "TIMER_ACTION") {
                val isPlay = intent.getBooleanExtra("isPlay",false)
                if(isPlay) {
                    findViewById<ImageView>(R.id.play_pause).setImageResource(android.R.drawable.ic_media_pause)
                }else{
                    findViewById<ImageView>(R.id.play_pause).setImageResource(android.R.drawable.ic_media_play)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createNotificationChannel()
        findViewById<ImageView>(R.id.play_pause).setOnClickListener {
            startServiceWithCommand("play_pause")
        }
        findViewById<ImageView>(R.id.next).setOnClickListener {
            startServiceWithCommand("next")
        }
        findViewById<ImageView>(R.id.prev).setOnClickListener {
            startServiceWithCommand("prev")
        }
        startServiceWithCommand(null)
    }

    private fun startServiceWithCommand(command:String?){
        val intent = Intent(this, MusicService::class.java)
        intent.putExtra("command",command)
        startService(intent)
    }
    override fun onStart() {
        super.onStart()
        registerReceiver(receiver, IntentFilter("TIMER_ACTION"))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(receiver)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_LOW
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

}