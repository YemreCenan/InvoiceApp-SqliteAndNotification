package com.example.invoiceapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class UpdateActivity extends AppCompatActivity {

    public String DATE;
    private static final String TAG = "Fatura Hatırlatıcı";
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    DecimalFormat numf = new DecimalFormat("00");
    EditText title_input, price_input,date_input;
    Button update_button, delete_button;

    String id, title, price, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        title_input = findViewById(R.id.title_input2);
        price_input = findViewById(R.id.price_input2);
        date_input = findViewById(R.id.date_input2);
        update_button = findViewById(R.id.update_button);
        delete_button = findViewById(R.id.delete_button);
        DATE=sdf.format(Calendar.getInstance().getTime());
        date_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(UpdateActivity.this, R.style.my_dialog_theme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        Log.d(TAG, "onDateSet: dd.MM.yyyy: " + day + "." + month + "." + year);
                        String date = numf.format(day) + "." + numf.format(month) + "." + year;
                        DATE = date;



                    }
                }, year, month, day).show();

            }
        });

        //First we call this
        getAndSetIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                date_input.setText(DATE);
                String date1= DATE;
                //And only then we call this
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                title = title_input.getText().toString().trim();
                price = price_input.getText().toString().trim();
                date = date1.trim();
                myDB.updateData(id, title, price, date);
                Intent intent = new Intent(UpdateActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmDialog();
                NotifyMe.cancel(getApplicationContext(),"test");
            }

        });

    }

    void getAndSetIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
                getIntent().hasExtra("price") && getIntent().hasExtra("date")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            title = getIntent().getStringExtra("title");
            price = getIntent().getStringExtra("price");
            date = getIntent().getStringExtra("date");

            //Setting Intent Data
            title_input.setText(title);
            price_input.setText(price);
            date_input.setText(date);
            Log.d("stev", title+" "+price+" "+date);
        }else{
            Toast.makeText(this, "Kayıt Yok.", Toast.LENGTH_SHORT).show();
        }
    }

    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title +"  "+"Silinsin mi ?" );
        builder.setMessage(title +"Kayıdı silmek istediğinize emin misiniz ?" );
        builder.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
                finish();
            }
        });
        builder.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}

