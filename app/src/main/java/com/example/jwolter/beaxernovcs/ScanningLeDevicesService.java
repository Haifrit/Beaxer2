package com.example.jwolter.beaxernovcs;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Created by J.Wolter on 17.03.2016.
 */
public class ScanningLeDevicesService extends Service{
    private static final String TAG = "BroadcastService";
    public static final String BROADCAST_ACTION = "com.example.jwolter.beaxer.ScanningLeDevicesService.displayevent";
    private final Handler handler = new Handler();
    Intent intent;

    // Bluetooth
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;
    private static final long SCAN_PERIOD = 100000;
    private Handler mHandler;
    private boolean mScanning;

    //Daten
    List<LocationInformation> locationInformationList;
    HashMap<String, MyBleDevice> deviceMap;
    Context context;
    IndoorPosition indoorposition;

    // MyBleDevice scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            BigDecimal bigDecimal_rssi = new BigDecimal(rssi);
                            MyBleDevice myBleDevice = new MyBleDevice(bigDecimal_rssi, device.getAddress());
                            deviceMap.put(myBleDevice.getAdress(), myBleDevice);

                            // Hier kann device und rssi abgefragt werden

                        }
                    });
                }
            };

    private Runnable sendUpdatesToUI = new Runnable() {
        public void run() {
            DisplayLoggingInfo();
            handler.postDelayed(this, 1000); // 1 second
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(BROADCAST_ACTION);

        //Daten
        deviceMap = new HashMap<String, MyBleDevice>();
        context = this;

        //Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mHandler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        scanLeDevice(true);
        handler.removeCallbacks(sendUpdatesToUI);
        handler.postDelayed(sendUpdatesToUI, 1000); //vllt. kürzer
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void DisplayLoggingInfo() {


        //intent.putExtra("counter", String.valueOf(++counter));
        //intent.putExtra("locationInformationList", (Parcelable) locationInformationList);

        intent.putExtra("indoorposition", new IndoorPosition(1,1,1));
        sendBroadcast(intent);
    }

    /**
     * Scannt für den vorgegebenen Zeitraum SCAN_PERIOD nach BLE Geräten
     * @param enable ob gescannt werden soll
     */
    private void scanLeDevice (final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    // Hier ist der Scan abgeschlossen
                    //Context zu übergeben könnte zu Fehlern führen
                    String restOutput = null;

                    RestClient restClient = new RestClient(buildJsonArray(), context);

                    try {
                        restOutput = restClient.execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    MyJsonConstructor myJsonConstructor = new MyJsonConstructor(restOutput);
                    indoorposition = myJsonConstructor.getIndoorPosition();
                    locationInformationList = myJsonConstructor.getListOfLocationInformation();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    /**
     * Macht aus der Map ein JSONArray und löscht alle Einträge der Map
     * @return Das JSONArray mit den Daten drin.
     */
    private JSONArray buildJsonArray() {

        JSONArray output = new JSONArray();
        JSONObject obj;

        for(Map.Entry<String, MyBleDevice> entry : deviceMap.entrySet()) {

            MyBleDevice value = entry.getValue();
            obj = new JSONObject();
            try {
                obj.put("id", value.getAdress());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                obj.put("signalstrenght", value.getSignal_strenght());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            output.put(obj);
        }
        deviceMap.clear();
        return  output;
    }

    private void runOnUiThread(Runnable runnable) {
        handler.post(runnable);
    }
}
