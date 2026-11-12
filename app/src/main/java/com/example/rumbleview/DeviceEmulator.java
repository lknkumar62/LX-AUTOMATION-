package com.example.rumbleview;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Random;

public class DeviceEmulator {
    private static final String TAG = "DeviceEmulator";
    private Context context;
    private Random random;

    public DeviceEmulator(Context context) {
        this.context = context;
        this.random = new Random();
    }

    public void simulateRealDevice() {
        try {
            // Generate realistic device properties
            String deviceId = generateDeviceId();
            String model = Build.MODEL;
            String brand = Build.BRAND;
            String androidVersion = Build.VERSION.RELEASE;
            
            Log.d(TAG, "Simulating real device: " + model + " (" + brand + ") Android " + androidVersion);
            
            // Simulate network behavior
            simulateNetworkConditions();
            
            // Simulate user interaction patterns
            simulateUserBehavior();
            
        } catch (Exception e) {
            Log.e(TAG, "Error simulating device", e);
        }
    }

    private String generateDeviceId() {
        // Generate a realistic device ID that looks like it's from a real device
        StringBuilder deviceId = new StringBuilder();
        deviceId.append("Android-");
        deviceId.append(Build.MODEL.replaceAll("[^a-zA-Z0-9]", "").toLowerCase());
        deviceId.append("-").append(random.nextInt(1000000));
        return deviceId.toString();
    }

    private void simulateNetworkConditions() {
        // Simulate different network conditions to appear more realistic
        int networkType = random.nextInt(4); // 0-3 for different network types
        
        switch (networkType) {
            case 0: Log.d(TAG, "Simulating WiFi connection"); break;
            case 1: Log.d(TAG, "Simulating 4G connection"); break;
            case 2: Log.d(TAG, "Simulating 3G connection"); break;
            case 3: Log.d(TAG, "Simulating 2G connection"); break;
        }
    }

    private void simulateUserBehavior() {
        // Simulate realistic user interaction patterns
        int action = random.nextInt(5);
        
        switch (action) {
            case 0: Log.d(TAG, "Simulating user watching video"); break;
            case 1: Log.d(TAG, "Simulating user scrolling through content"); break;
            case 2: Log.d(TAG, "Simulating user interacting with controls"); break;
            case 3: Log.d(TAG, "Simulating user pausing video"); break;
            case 4: Log.d(TAG, "Simulating user resuming video"); break;
        }
    }
}