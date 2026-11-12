package com.example.rumbleview;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 100;
    private EditText videoUrlInput;
    private Button startButton, stopButton;
    private TextView statusText;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        videoUrlInput = findViewById(R.id.video_url_input);
        startButton = findViewById(R.id.start_button);
        stopButton = findViewById(R.id.stop_button);
        statusText = findViewById(R.id.status_text);

        prefs = getSharedPreferences("rumble_prefs", MODE_PRIVATE);

        // Check permissions
        if (checkPermissions()) {
            setupViews();
        } else {
            requestPermissions();
        }

        // Load saved URL
        String savedUrl = prefs.getString("video_url", "");
        videoUrlInput.setText(savedUrl);
    }

    private void setupViews() {
        startButton.setOnClickListener(v -> {
            String videoUrl = videoUrlInput.getText().toString().trim();
            if (videoUrl.isEmpty()) {
                Snackbar.make(findViewById(android.R.id.content), "Please enter a valid Rumble URL", Snackbar.LENGTH_LONG).show();
                return;
            }

            // Save URL
            prefs.edit().putString("video_url", videoUrl).apply();

            // Start service
            Intent serviceIntent = new Intent(this, ViewCounterService.class);
            serviceIntent.putExtra("video_url", videoUrl);
            startService(serviceIntent);

            statusText.setText("View Counter Started");
            startButton.setEnabled(false);
            stopButton.setEnabled(true);
        });

        stopButton.setOnClickListener(v -> {
            Intent serviceIntent = new Intent(this, ViewCounterService.class);
            stopService(serviceIntent);
            statusText.setText("View Counter Stopped");
            startButton.setEnabled(true);
            stopButton.setEnabled(false);
        });
    }

    private boolean checkPermissions() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE
                },
                PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupViews();
            } else {
                Toast.makeText(this, "Permissions required to run the app", Toast.LENGTH_LONG).show();
            }
        }
    }
}