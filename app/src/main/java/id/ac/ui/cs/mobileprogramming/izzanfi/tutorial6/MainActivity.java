package id.ac.ui.cs.mobileprogramming.izzanfi.tutorial6;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button wifiSelectorBtn = findViewById(R.id.wifi_selector);
        Button jniLibsBtn = findViewById(R.id.jni);

        wifiSelectorBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToWifiSelector();
            }
        });

        jniLibsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigateToJniSimulation();
            }
        });

    }

    private void navigateToWifiSelector() {
        Intent intent = new Intent(this, ScanWifiActivity.class);
        startActivity(intent);
    }

    private void navigateToJniSimulation() {
        Intent intent = new Intent(this, JniActivity.class);
        startActivity(intent);
    }

}
