package com.example.sensores;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    SensorManager mSensorManager;
    Sensor mLuz, mProx, mGravidade, mAcelerometro;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private Location location;
    //private LocationManager locationManager;
    double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout ll = findViewById(R.id.layoutt);
        ll.setBackgroundColor(getResources().getColor(R.color.minhaCor));

        Log.i("Mensagem", "Bom dia");
        //pedirPermissao();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    public void ListarSensores(View view) {
        List<Sensor> lista = mSensorManager.getSensorList(Sensor.TYPE_ALL);
        Iterator<Sensor> iterator = lista.iterator();
        String sensores = " ";
        while (iterator.hasNext()) {
            Sensor sensor = iterator.next();
            sensores += " - " + sensor.getName() + "\n";
        }
        Log.i("Sensores","Meus sensores: \n"+sensores);
    }

    public void LUMINOSIDADE(View view) {
        mLuz = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(new LuzSensor(), mLuz, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void LOCALIZAR(View view) {

        pedirPermissao();

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case 1: {
                if(grantResults.length > 0 &&
                        grantResults[0]==PackageManager.PERMISSION_GRANTED)
                    configurarServico();
                else
                    Toast.makeText(this, "Não vai funcionar!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class LuzSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            Log.i("Sensores","Luminosidade: "+vl);
        }
    }

    public void PROXIMIDADE(View view) {
        mProx = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        mSensorManager.registerListener(new ProxSensor(), mProx, SensorManager.SENSOR_DELAY_FASTEST);
    }
    class ProxSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            Log.i("Sensores","Proximidade: "+vl);

        }
    }

    public void ACELEROMETRO(View view) {
        mAcelerometro = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(new AcelerometroSensor(), mAcelerometro, SensorManager.SENSOR_DELAY_FASTEST);
    }
    class AcelerometroSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            float v2 = event.values[1];
            float v3 = event.values[2];
            Log.i("Sensores","Acelerômetro: Eixo X: "+vl+", eixo Y: "+v2+", eixo Z: "+v3);
        }
    }


    public void GRAVIDADE(View view) {
        mGravidade = mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        mSensorManager.registerListener(new GravidadeSensor(), mGravidade, SensorManager.SENSOR_DELAY_NORMAL);
    }
    class GravidadeSensor implements SensorEventListener {
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }

        public void onSensorChanged(SensorEvent event) {
            float vl = event.values[0];
            float v2 = event.values[1];
            float v3 = event.values[2];
            Log.i("Sensores","Gravidade X: "+vl+", gravidade Y: "+v2+", gravidade Z: "+v3);
        }
    }

    private void pedirPermissao(){
        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) !=
                        PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, 1);
        }
        else
            configurarServico();
    }

    public void configurarServico(){
        try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    atualizar(location);
                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                }

                @Override
                public void onProviderEnabled(String provider) {  }

                @Override
                public void onProviderDisabled(String provider) {  }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch(SecurityException ex){
        }
    }

    public void atualizar(Location location){
        Double latPoint = location.getLatitude();
        Double longPoint = location.getLongitude();

        Log.i("Sensores", "lat "+latPoint);
        Log.i("Sensores", "lon "+longPoint);

    }
}
