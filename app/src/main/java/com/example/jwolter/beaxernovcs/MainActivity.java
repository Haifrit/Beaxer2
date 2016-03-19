package com.example.jwolter.beaxernovcs;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    //Test
    Button testButton;
    IndoorPosition testindoorPosition;

    //Daten
    Context context;

    //Karte
    ImageView imageView;
    ImageView punkt;
    Bitmap punkt_bitmap;
    private static final int VIEW_WIDTH = 600;
    private static final int VIEW_HEIGHT = 600;

    //Bluetooth
    private static final int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter mBluetoothAdapter;

    //ForService
    private static final String TAG = "BroadcastTest";
    private Intent intent;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateUI(intent);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Daten
        context = this;
        intent = new Intent(this, ScanningLeDevicesService.class);

        // Layout Elemente erzeugen
        imageView = (ImageView) findViewById(R.id.imageView);
        punkt = (ImageView) findViewById(R.id.punkt);

        //Den Punkt laden
        punkt_bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.punkt);

        //Bluetooth
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        //Test
        testButton = (Button) findViewById(R.id.button);
        testindoorPosition = new IndoorPosition(1000, 1000, 1);
        imageView.setImageBitmap(getMapSectionByCoordinates(testindoorPosition));
        punkt.setImageBitmap(punkt_bitmap);
        Toast toast = Toast.makeText(context, "hans", Toast.LENGTH_LONG);
        toast.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast toast = Toast.makeText(context, "peter", Toast.LENGTH_LONG);
        toast.show();
        //für Test auskommentiert startService(intent);
        //für Test auskommentiert registerReceiver(broadcastReceiver, new IntentFilter(ScanningLeDevicesService.BROADCAST_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        //test unregisterReceiver(broadcastReceiver);
        //test stopService(intent);

    }

    private void updateUI(Intent intent) {
        //ArrayList locationInformationList = intent.getParcelableArrayListExtra("locationInformationList");
        IndoorPosition indoorPosition = intent.getParcelableExtra("indoorposition");
        imageView.setImageBitmap(getMapSectionByCoordinates(indoorPosition));
        punkt.setImageBitmap(punkt_bitmap);
    }

    /**
     * Schneidet ein Teilstück aus dem Kartenbild heraus welches durch z spezifiziert wird.
     * @param position erhält ein IndoorPositions onjekt welches folgende Werte enthält:
     * X-Koordinate die momentane Position des Benutzers
     * Y--Koordinate die momentane Position des Benutzers
     * Die Etage dessen Bild geladen werden muss
     * @return Ein Bitmap mit den Abmessungen VIEW_WIDTH und VIEW_HEIGHT, durch die Hilfsmethoden
     * xToCenter und yToCenter befindet sich der rote Punkt welches den Benutzerdarstellt genau in der
     * Mitte des Bildes
     *
     */
    public Bitmap getMapSectionByCoordinates (IndoorPosition position) {

        Bitmap source_bitmap = getLevel(position.getZ());

        position.setX(xToCenter(position.getX()));
        position.setY(yToCenter(position.getY()));

        Bitmap section =  Bitmap.createBitmap(source_bitmap, position.getX(), position.getY(), VIEW_WIDTH, VIEW_HEIGHT);

        source_bitmap.recycle();

        return section;

    }

    /**
     * Gibt die Karte zu der Etage zurück auf der man sich befindet. Die Etage wird durch z
     * spezifiziert.
     * @param z Die Etage auf der man sich befindet
     * @return Das Bild / die Karte der Etage auf der man sich befindet
     */
    private Bitmap getLevel(int z) {
        Bitmap level;
        switch (z) {
            case 0:
                level = BitmapFactory.decodeResource(getResources(), R.drawable.level0);
                break;
            case 1:
                level = BitmapFactory.decodeResource(getResources(), R.drawable.level1);
                break;
            case 2:
                level = BitmapFactory.decodeResource(getResources(), R.drawable.level2);
                break;
            default:
                level = BitmapFactory.decodeResource(getResources(), R.drawable.level0);
        }
        return level;
    }

    /**
     * Verschiebt die y Koordinate in die Mitte des Bildes
     * @param y die alte y Koordinate
     * @return die neue angepasste y Koordinate
     */
    private int yToCenter(int y) {
        y = y - (VIEW_HEIGHT / 2);
        if (y < 0) {
            y = 0;
        }
        return y;
    }

    /**
     * Verschiebt die x Koordinate in die Mitte des Bildes
     * @param x die alte x Koordinate
     * @return die neue x Koordinate
     */
    private int xToCenter(int x) {


        x = x - (VIEW_WIDTH / 2);
        if (x < 0) {
            x = 0;
        }
        return x;
    }

    public void testOnKlick(View view) {
        startService(intent);
        registerReceiver(broadcastReceiver, new IntentFilter(ScanningLeDevicesService.BROADCAST_ACTION));
        Toast toast = Toast.makeText(context, "clicked", Toast.LENGTH_LONG);
    }
}

