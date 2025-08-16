package com.example.BloodCare; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper; // Import Looper for modern Handler usage
import androidx.appcompat.app.AppCompatActivity;

/**
 * SplashActivity is the initial screen displayed when the application starts.
 * It shows a splash screen for a defined duration and then transitions
 * to the OpeningActivity (onboarding screen).
 */
public class SplashActivity extends AppCompatActivity {

    // Duration of the splash screen in milliseconds (e.g., 3000ms = 3 seconds)
    private static final long SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the content view to your splash screen layout
        // Ensure your XML layout file is named 'activity_splash.xml' or similar
        setContentView(R.layout.activity_splash); // Assuming your splash layout is activity_splash.xml

        // Using a Handler to delay the transition to the next activity
        // Using Looper.getMainLooper() for Handler is good practice for modern Android
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // Create an Intent to start your OpeningActivity (the onboarding screen)
                // IMPORTANT: Replace 'OpeningActivity.class' if your onboarding activity has a different name
                Intent onboardingIntent = new Intent(SplashActivity.this, OpeningActivity.class);
                startActivity(onboardingIntent);

                // Finish the SplashActivity so the user cannot navigate back to it
                finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
