package id.ac.ui.cs.mobileprogramming.izzanfi.tutorial6;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ScanWifiActivity extends AppCompatActivity {
    private FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_selector);

        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        WifiFragment fragment = WifiFragment.newInstance();
        ft.replace(R.id.wifi_list_container, fragment);
        ft.commit();
    }
}
