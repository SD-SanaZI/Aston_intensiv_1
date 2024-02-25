package com.example.aston_intensiv_1

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder
import androidx.core.app.NotificationCompat


const val NOTIFICATION_ID = 1

class MusicService : Service() {
    private var mediaPlayer: MediaPlayer? = null
    val playlist = Playlist(listOf(R.raw.music1,R.raw.music2, R.raw.music3))

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onCreate() {
        mediaPlayer = MediaPlayer.create(this, playlist.getCurrent())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(startId == 1) {
            startForeground(NOTIFICATION_ID, createNotification())
        }
        when(intent?.getStringExtra("command")){
            "play_pause" -> {
                if(mediaPlayer!!.isPlaying){
                    mediaPlayer!!.pause()
                }else{
                    mediaPlayer!!.start()
                }
            }
            "next" -> {
                mediaPlayer!!.stop()
                mediaPlayer = null
                mediaPlayer = MediaPlayer.create(this, playlist.getNext())
                mediaPlayer!!.start()
            }
            "prev" -> {
                mediaPlayer!!.stop()
                mediaPlayer = null
                mediaPlayer = MediaPlayer.create(this, playlist.getPrev())
                mediaPlayer!!.isLooping = true
                mediaPlayer!!.start()
            }
        }
        startForeground(NOTIFICATION_ID, createNotification())
        sendData(mediaPlayer!!.isPlaying)
        return START_STICKY
    }

    private fun sendData(isPlay: Boolean){
        val intent = Intent()
        intent.action = "TIMER_ACTION"
        intent.putExtra("isPlay",isPlay)
        sendBroadcast(intent)
    }

    private fun createNotification(): Notification{
        val playOrPauseIcon = if(mediaPlayer!!.isPlaying) android.R.drawable.ic_media_pause else android.R.drawable.ic_media_play
        val playOrPauseTitle = if(mediaPlayer!!.isPlaying) "Pause" else "Play"
        val actionPrev = createAction("prev",1,android.R.drawable.ic_media_previous,"Previous")
        val actionPlay = createAction("play_pause",2,playOrPauseIcon,playOrPauseTitle)
        val actionNext = createAction("next",3,android.R.drawable.ic_media_next,"Next")

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.music)
            .setContentTitle(getString(R.string.notification_name))
            .addAction(actionPrev)
            .addAction(actionPlay)
            .addAction(actionNext)
            .setPriority(NotificationCompat.PRIORITY_MIN)
            .build()
    }

    private fun createAction(command:String, requestCode:Int, icon:Int, title:String): NotificationCompat.Action{
        val intent = Intent(this,MusicService::class.java)
        intent.putExtra("command",command)
        val pendingIntentPrev = PendingIntent.getService(this,requestCode,intent, PendingIntent.FLAG_IMMUTABLE)
        return NotificationCompat.Action.Builder(icon,title,pendingIntentPrev).build()
    }

    override fun onDestroy() {
        mediaPlayer!!.stop()
        mediaPlayer = null
    }
}