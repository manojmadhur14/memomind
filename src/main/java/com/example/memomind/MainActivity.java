package com.example.memomind;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText[] taskEditTexts = new EditText[5];
    private Button[] timePickerButtons = new Button[5];
    private String[] selectedTimes = new String[5];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize EditTexts and Buttons for tasks
        for (int i = 0; i < 5; i++) {
            int editTextId = getResources().getIdentifier("editTextTask" + (i + 1), "id", getPackageName());
            int buttonId = getResources().getIdentifier("buttonTimePicker" + (i + 1), "id", getPackageName());

            taskEditTexts[i] = findViewById(editTextId);
            timePickerButtons[i] = findViewById(buttonId);

            final int taskIndex = i;

            timePickerButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showTimePickerDialog(taskIndex);
                }
            });
        }

        Button buttonConfirm = findViewById(R.id.buttonConfirm);
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();
            }
        });


    }


    private void showTimePickerDialog(final int taskIndex) {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String selectedTime = String.format("%02d:%02d", hourOfDay, minute);
                        selectedTimes[taskIndex] = selectedTime;
                        timePickerButtons[taskIndex].setText(selectedTime);
                    }
                },
                0, 0, true
        );
        timePickerDialog.show();
    }

    private void showConfirmationDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Do you want to save the alarms?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Implement your saving logic here using selectedTimes array
                for (int i = 0; i < selectedTimes.length; i++) {
                    if (selectedTimes[i] != null) {
                        Log.e("selectedTimes in setAlarms.................", selectedTimes[i]);
                        setAlarm(i, selectedTimes[i]);
                    }
                }
                Toast.makeText(MainActivity.this, "Alarms saved!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, FinalActivity.class);
                startActivity(intent);


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void setAlarm(int taskIndex, String selectedTime) {
        // Parse selectedTime and set the alarm using AlarmManager
        String[] timeComponents = selectedTime.split(":");
        int hour = Integer.parseInt(timeComponents[0]);
        int minute = Integer.parseInt(timeComponents[1]);

        //Intent alarmIntent = new Intent("com.example.myapp.ALARM_ACTION");


        Intent alarmIntent = new Intent(this, AlarmReceiver.class);

        Log.e("taskIndex...................................", String.valueOf(taskIndex));

        alarmIntent.putExtra("task_index", taskIndex);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, taskIndex, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        long alarmTimeMillis = calendar.getTimeInMillis();

        // Schedule the alarm with a unique pending intent request code
        //alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
    }

}

