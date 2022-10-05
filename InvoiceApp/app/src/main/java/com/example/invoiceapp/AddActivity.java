package com.example.invoiceapp;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import com.allyants.notifyme.NotifyMe;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.security.cert.CertPathBuilder;
import java.text.DateFormat;
import java.util.Calendar;

import android.app.AlarmManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;

import java.sql.Time;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public String DATE, HOUR;
    private static final String TAG = "Fatura Hatırlatıcı";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    DecimalFormat numf = new DecimalFormat("00");
    public Calendar cal = Calendar.getInstance();
    Calendar now = Calendar.getInstance(TimeZone.getTimeZone("GMT+3"));
    private static final String CHANNEL_ID="101";
    DatePickerDialog dpd;
    TimePickerDialog tpd;
    ImageButton notifiAddBtn,notifiCloseBtn;
    EditText title_input, price_input, date_input;
    Button add_button;
    public int years;
    public int months;
    public int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);


        title_input = findViewById(R.id.title_input);
        price_input = findViewById(R.id.price_input);
        date_input = findViewById(R.id.date_input);
        add_button = findViewById(R.id.add_button);
        notifiAddBtn = findViewById(R.id.notifiAddBtn);
        notifiCloseBtn = findViewById(R.id.notifiCloseBtn);

        dpd = DatePickerDialog.newInstance(
                AddActivity.this,
                years =  now.get(Calendar.YEAR),
                months= now.get(Calendar.MONTH),
                days = now.get(Calendar.DAY_OF_MONTH)
        );

        tpd =TimePickerDialog.newInstance(
                AddActivity.this,
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                now.get(Calendar.SECOND),
                false
        );
        notifiCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotifyMe.cancel(getApplicationContext(),"test");
            }
        });
        notifiAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dpd.show(getSupportFragmentManager(),"DatePickerDialog");
            }
        });



        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addBook(title_input.getText().toString().trim(),
                        price_input.getText().toString().trim(),
                        DATE.trim());
                Intent intent = new Intent(AddActivity.this,MainActivity.class);
                startActivity(intent);

            }
        });




    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        now.set(Calendar.YEAR,year);
        now.set(Calendar.MONTH,monthOfYear);
        now.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        tpd.show(getSupportFragmentManager(), "Timepickerdialog");
        Log.d(TAG, "onDateSet: dd.MM.yyyy: " + days + "." + months + "." + years);
        String date = numf.format(days) + "." + numf.format(months) + "." + years;
        DATE = date;
        date_input.setText(DATE);
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        now.set(Calendar.HOUR_OF_DAY,hourOfDay);
        now.set(Calendar.MINUTE,minute);
        now.set(Calendar.SECOND,second);
        createNotificationChannel();

    }


    private void createNotificationChannel() {
        String id ="my_cahennel_id_01";
        NotificationManager manager =(NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = manager.getNotificationChannel(id);
            if (channel==null){
                channel=new NotificationChannel(id,"Channel Title",NotificationManager.IMPORTANCE_HIGH);

                channel.setDescription("[Channel description]");
                channel.enableVibration(true);
                channel.setVibrationPattern(new long[]{10,100,20,34});
                channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
                manager.createNotificationChannel(channel);
            }
        }
        Intent notificationIntent = new Intent(this,TestActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(notificationIntent);
        PendingIntent contentIntent =
                stackBuilder.getPendingIntent(0,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id)
                .setSmallIcon(R.drawable.mainlogo1)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .setBigContentTitle(title_input.getText().toString()))
                .setContentText(price_input.getText().toString())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVibrate(new long[]{10,100,20,34})
                .setAutoCancel(false);
        builder.setContentIntent(contentIntent);
        NotificationManagerCompat n = NotificationManagerCompat.from(getApplicationContext());
        n.notify(1,builder.build());


    }



}