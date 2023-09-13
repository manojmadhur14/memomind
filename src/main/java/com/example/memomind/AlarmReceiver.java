package com.example.memomind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

    public static MediaPlayer mediaPlayer = new MediaPlayer();
    public static int indexTrack = -1;


    @Override
    public void onReceive(Context context, Intent intent) {
        int taskIndex = intent.getIntExtra("task_index", -1);

        //Log.e("taskIndex...................................", String.valueOf(taskIndex));
        playAlarmSound(context);
        indexTrack = taskIndex;

    }

    private void playAlarmSound(Context context) {
        mediaPlayer = MediaPlayer.create(context, R.raw.alarm);
        mediaPlayer.start();
    }

    private void stopAlarmSound() {
        mediaPlayer.stop();
    }
}
