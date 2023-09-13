package com.example.memomind;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class FinalActivity extends AppCompatActivity {

    private NfcAdapter nfcAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.final_layout);

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        alarmManager.cancel(pendingIntent);


    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            enableNfcForegroundDispatch();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            disableNfcForegroundDispatch();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent); // Save the new intent
        if (nfcAdapter != null && NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Log.e("MainActivity", "nfcAdapter for NFC");
            handleNfcTag(intent);
        }
    }

    private void enableNfcForegroundDispatch() {
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    private void disableNfcForegroundDispatch() {
        // Disable NFC foreground dispatch if needed
    }

    private void handleNfcTag(Intent intent) {
        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag != null)
        {
            byte[] id = tag.getId();
            // Convert id to a string (you may need to adjust the conversion)
            String nfcId = byteArrayToHexString(id);


            if ("04B8BBB2003E80".equals(nfcId) && (AlarmReceiver.indexTrack==0))
            {
                AlarmReceiver.mediaPlayer.stop();

            } else if ("041DDB5AA84A81".equals(nfcId) && (AlarmReceiver.indexTrack==1))
            {
                AlarmReceiver.mediaPlayer.stop();

            } else if ("04F31E42274980".equals(nfcId) && (AlarmReceiver.indexTrack==2))
            {
                AlarmReceiver.mediaPlayer.stop();

            }else if ("04F91E42274980".equals(nfcId) && (AlarmReceiver.indexTrack==3))
            {
                AlarmReceiver.mediaPlayer.stop();

            }else if ("0415CE5AA84A81".equals(nfcId) && (AlarmReceiver.indexTrack==4))
            {
                AlarmReceiver.mediaPlayer.stop();

            }
        }

    }

    // Helper method to convert byte array to hexadecimal string
    private String byteArrayToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString();
    }


    }

