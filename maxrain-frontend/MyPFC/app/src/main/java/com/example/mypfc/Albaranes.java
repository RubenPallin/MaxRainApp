package com.example.mypfc;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Albaranes extends AppCompatActivity {

    private TextView albaranesEditText;
    private TextView fecha1TextView;
    private TextView fecha2TextView;
    private TextView fechaNumEditText;
    private TextView transportistaEditText;
    private TextView expedicionBultoEditText;
    private TextView numEditText;
    private TextView currentDateTextView; // Add a TextView to display the current date


    private TextView albaranesValueTextView;
    private TextView fechaValueTextView;
    private TextView transportistaValueTextView;
    private TextView expedicionBultoValueTextView;
    private TextView numValueTextView;

    private static final String APPLICATION_ID = "com.example.mypfc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albaranes);

        fecha1TextView = findViewById(R.id.fecha1TextView);
        fecha2TextView = findViewById(R.id.fecha2TextView);
        fechaNumEditText = findViewById(R.id.fechaTextView);
        numEditText = findViewById(R.id.numTextView);
        transportistaEditText = findViewById(R.id.transportistaTextView);
        expedicionBultoEditText = findViewById(R.id.expedicionBultoTextView);


        fechaValueTextView = findViewById(R.id.fechaValueTextView);
        transportistaValueTextView = findViewById(R.id.transportistaValueTextView);
        expedicionBultoValueTextView = findViewById(R.id.expedicionBultoValueTextView);
        numValueTextView = findViewById(R.id.numValueTextView);


        Toolbar toolbarAlb = findViewById(R.id.toolbar_albaranes);
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

        fecha1TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fecha1TextView);
            }
        });

        fecha2TextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(fecha2TextView);
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

        // Set albaran data manually
        setAlbaranData();

        // Set on click listener to download PDF
        numValueTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPdf();
            }
        });
        // Underline the albaranes value
        underlineTextView(numValueTextView);
    }

    private void updateCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = dateFormat.format(currentDate);

        fecha2TextView.setText(formattedDate);
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
    private void setAlbaranData() {
        String numeroAlbaran = "MXN/1";
        String fecha = "05/06/2024";
        String transportista = "SEUR";
        String numeroExpedicion = "12345";

        numValueTextView.setText(numeroAlbaran);
        fechaValueTextView.setText(fecha);
        transportistaValueTextView.setText(transportista);
        expedicionBultoValueTextView.setText(numeroExpedicion);

    }
    private void downloadPdf() {
        try {
            // File name for the downloaded PDF
            String archivoNombre = "albaran.pdf";

            // Copy the PDF from the assets folder to the external storage
            InputStream inputStream = getAssets().open(archivoNombre);
            File archivo = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), archivoNombre);
            FileOutputStream outputStream = new FileOutputStream(archivo);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();

            // Create a URI for the file
            Uri fileUri = FileProvider.getUriForFile(this, APPLICATION_ID + ".provider", archivo);

            // Create an Intent to view the PDF
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

            // Start the Intent to view the PDF
            startActivity(intent);
        } catch (IOException e) {
            Log.e("Albaranes", "Error downloading PDF", e);
        }
    }
    private void underlineTextView(TextView textView) {
        textView.setPaintFlags(textView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        textView.setTextColor(Color.BLUE);
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