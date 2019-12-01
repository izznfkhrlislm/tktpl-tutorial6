package id.ac.ui.cs.mobileprogramming.izzanfi.tutorial6;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WifiFragment extends Fragment {

    public class Device {
        CharSequence name;
        String capabilities;

        public String getCapabilities() {
            return capabilities;
        }

        public CharSequence getName() {
            return name;
        }

        public void setName(CharSequence name) {
            this.name = name;
        }

        public void setCapabilities(String capabilities) {
            this.capabilities = capabilities;
        }
    }

    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 125;
    List<ScanResult> scannedWifiResultList;
    private WifiManager manager;
    List<Device> wifiScanAdapterDevices = new ArrayList<Device>();
    private String enteredWifiPassword = null;
    ScanWifiAdapter adapter;
    RecyclerView wifiListRv;
    int availableWifiNetworksCount = 0;

    public WifiFragment() {

    }

    public static WifiFragment newInstance() {
        return new WifiFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Button scanWifiBtn = (Button) getActivity().findViewById(R.id.ButtonScanWifi);
        manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (!manager.isWifiEnabled()) {
            Toast.makeText(getActivity(), "Wifi is disabled. Enabling...", Toast.LENGTH_SHORT).show();
            manager.setWifiEnabled(true);
        }

        getActivity().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                scannedWifiResultList = manager.getScanResults();
                availableWifiNetworksCount = scannedWifiResultList.size();
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        adapter = new ScanWifiAdapter(wifiScanAdapterDevices, getContext());

        wifiListRv = (RecyclerView) getActivity().findViewById(R.id.wifiRv);
        wifiListRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        wifiListRv.setAdapter(adapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        } else {
            manager.startScan();
            wifiScanAdapterDevices.clear();
            try {
                availableWifiNetworksCount = availableWifiNetworksCount - 1;
                while (availableWifiNetworksCount >= 0) {
                    Device newDeviceInstance = new Device();
                    newDeviceInstance.setName(scannedWifiResultList.get(availableWifiNetworksCount).SSID);
                    newDeviceInstance.setCapabilities(scannedWifiResultList.get(availableWifiNetworksCount).capabilities);
                    wifiScanAdapterDevices.add(newDeviceInstance);
                    adapter.notifyDataSetChanged();
                    availableWifiNetworksCount = availableWifiNetworksCount - 1;
                }
            } catch (Exception e) {
                Log.d("WiFi Selector", e.getMessage());
            }
        }
        scanWifiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.startScan();
                wifiScanAdapterDevices.clear();
                try {
                    availableWifiNetworksCount = availableWifiNetworksCount - 1;
                    while (availableWifiNetworksCount >= 0) {
                        Device newDeviceInstance = new Device();
                        newDeviceInstance.setName(scannedWifiResultList.get(availableWifiNetworksCount).SSID);
                        newDeviceInstance.setCapabilities(scannedWifiResultList.get(availableWifiNetworksCount).capabilities);
                        wifiScanAdapterDevices.add(newDeviceInstance);
                        adapter.notifyDataSetChanged();
                        availableWifiNetworksCount = availableWifiNetworksCount - 1;
                    }
                } catch (Exception e) {
                    Log.d("WiFi Selector", e.getMessage());
                }
            }
        });
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Device selectedDevice = (Device) view.findViewById(R.id.ssid_name).getTag();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                View popupWifi = inflater.inflate(R.layout.popup_prompt_password, null);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());

                alertDialog.setView(popupWifi);
                final EditText passwordInput = (EditText) popupWifi.findViewById(R.id.editTextPassword);
                TextView ssidName = (TextView) popupWifi.findViewById(R.id.textViewSSID);
                ssidName.setText(selectedDevice.getName());
                alertDialog
                        .setCancelable(false)
                        .setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        enteredWifiPassword = passwordInput.getText().toString();
                                        connectWifi(
                                            String.valueOf(selectedDevice.getName()),
                                            enteredWifiPassword, selectedDevice.capabilities);
                                    }
                                })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.cancel();
                                    }
                                });
                AlertDialog alertDialogShow = alertDialog.create();
                alertDialogShow.show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_wifi, container,false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                Map<String, Integer> permissionsMap = new HashMap<String, Integer>();
                permissionsMap.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                for (int i = 0; i < permissions.length; i++) {
                    permissionsMap.put(permissions[i], grantResults[i]);
                }
                if (permissionsMap.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    manager.startScan();
                } else {
                    Toast.makeText(getContext(), "You should enable some permissions first!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void checkPermission() {
        List<String> perissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            perissionsNeeded.add("Network");
        }

        if (permissionsList.size() > 0) {
            if (perissionsNeeded.size() > 0) {
                String message = "You need to grant access to: " + perissionsNeeded.get(0);
                for (int i = 0; i < perissionsNeeded.size(); i++) {
                    message = message + " , " + perissionsNeeded.get(i);
                }
                new AlertDialog.Builder(getActivity())
                    .setMessage(message)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            requestPermissions(permissionsList.toArray(
                                    new String[permissionsList.size()]),
                                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                        }
                    });
                return;
            }
            requestPermissions(permissionsList.toArray(
                    new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (getActivity().checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission);
                if (!shouldShowRequestPermissionRationale(permission)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void connectWifi(String ssidName, String password, String capabilities) {
        try {
            String ssid = ssidName;
            String pass = password;

            WifiConfiguration wifiConf = new WifiConfiguration();
            wifiConf.SSID = "\"" + ssid + "\"";
            wifiConf.status = WifiConfiguration.Status.ENABLED;
            wifiConf.priority = 40;

            // Wifi with WEP Security
            if (capabilities.toUpperCase().contains("WEP")) {
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wifiConf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
                wifiConf.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);

                if (pass.matches("^[0-9a-zA-Z]+$")) {
                    wifiConf.wepKeys[0] = pass;
                } else {
                    wifiConf.wepKeys[0] = "\"".concat(pass).concat("\"");
                }
                wifiConf.wepTxKeyIndex = 0;
            } else if (capabilities.toUpperCase().contains("WPA")) {
                // Wifi with WPA Security
                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);

                wifiConf.preSharedKey = "\"" + pass + "\"";
            } else {
                wifiConf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
                wifiConf.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
                wifiConf.allowedAuthAlgorithms.clear();
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
                wifiConf.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
                wifiConf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            }

            WifiManager manager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            manager.addNetwork(wifiConf);

            List<WifiConfiguration> wifiConfigurationList = manager.getConfiguredNetworks();
            for (WifiConfiguration configuration : wifiConfigurationList) {
                if (configuration.SSID != null && configuration.SSID.equals("\"" + ssid + "\"")) {
                    Log.v("Info", "SSID: " + configuration.SSID);
                    Log.v("Info", "is SSID disconnected: " + manager.disconnect());
                    Log.v("Info", "is SSID enabled: " + manager.enableNetwork(configuration.networkId, true));
                    Log.v("Info", "is SSID reconnected: " + manager.reconnect());

                    break;
                }
            }
        } catch (Exception e) {
            Log.e("Error", "Connect wifi, message: " + e.getMessage());
        }
    }
}
