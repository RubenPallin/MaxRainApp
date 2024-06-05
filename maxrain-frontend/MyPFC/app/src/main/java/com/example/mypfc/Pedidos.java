package com.example.mypfc;

import android.app.DatePickerDialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Pedidos extends AppCompatActivity {

    private TextView albaranesEditText;
    private TextView fecha1Texto;
    private TextView fecha2Texto;
    private TextView fechaNumTexto;
    private TextView transportista2EditText;
    private TextView expedicion2EditText;
    private TextView currentDateTextView; // Add a TextView to display the current date

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        albaranesEditText = findViewById(R.id.pedidosTextView);
        fecha1Texto = findViewById(R.id.fecha1Texto);
        fecha2Texto = findViewById(R.id.fecha2Texto);
        fechaNumTexto = findViewById(R.id.fechaNum2TextView);
        transportista2EditText = findViewById(R.id.transportista2TextView);
        expedicion2EditText = findViewById(R.id.expedicion2TextView);


        Toolbar toolbarAlb = findViewById(R.id.toolbar_pedidos);
        setSupportActionBar(toolbarAlb);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
            final Drawable upArrow = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.abc_ic_ab_back_material);
            if (upArrow != null) {
                upArrow.setColorFilter(ContextCompat.getColor(this, R.color.white), android.graphics.PorterDuff.Mode.SRC_ATOP);
                actionBar.setHomeAsUpIndicator(upArrow);
            }
        }

        fecha1Texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fecha1Texto);
            }
        });

        fecha2Texto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fecha2Texto);
            }
        });

        // Update the current date text view
        updateCurrentDate();

        // Schedule the daily update task
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                updateCurrentDate();
            }
        }, 0, 24, TimeUnit.HOURS); // Run every 24 hours
    }

    private void updateCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);

        fecha1Texto.setText(formattedDate);
        fecha2Texto.setText(formattedDate);
    }

    private void showDatePickerDialog(final TextView textView) {
        CalendarioFragment datePickerFragment = new CalendarioFragment(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                textView.setText(DateFormat.getDateInstance().format(calendar.getTime()));
            }
        });
        datePickerFragment.show(getSupportFragmentManager(), "datePicker");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}