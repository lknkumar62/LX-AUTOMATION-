package com.example.rumbleview;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ViewCounterService extends Service {
    private static final String TAG = "ViewCounterService";
    private ScheduledExecutorService scheduler;
    private DeviceEmulator deviceEmulator;
    private RumbleBypass rumbleBypass;
    private UniqueViewManager uniqueViewManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service created");
        deviceEmulator = new DeviceEmulator(this);
        rumbleBypass = new RumbleBypass(this);
        uniqueViewManager = new UniqueViewManager(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");
        
        String videoUrl = intent.getStringExtra("video_url");
        if (videoUrl != null) {
            // Start the view counter with real device emulation and unique view logic
            startViewCounter(videoUrl);
        }
        
        return START_STICKY;
    }

    private void startViewCounter(String videoUrl) {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                // Emulate real device behavior
                deviceEmulator.simulateRealDevice();
                
                // Bypass algorithm with unique view logic
                rumbleBypass.bypassAlgorithm(videoUrl);
                
                // Generate unique views (avoiding duplicate detection)
                uniqueViewManager.generateUniqueViews(videoUrl);
                
                Log.d(TAG, "View counter tick - Emulating real device, bypassing algorithm and generating unique views");
            } catch (Exception e) {
                Log.e(TAG, "Error in view counter", e);
            }
        }, 0, 30, TimeUnit.SECONDS); // Run every 30 seconds
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed");
        if (scheduler != null) {
            scheduler.shutdownNow();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}