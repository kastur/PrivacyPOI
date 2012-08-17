package android.override.poi;

import android.app.Service;
import android.content.Intent;
import android.content.res.AssetManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;

public class POIService extends Service {
    private static final String TAG = "POIService";
    private LocationManager mLocationManager;
    private POIDatabase mDB;
    private static final int MSG_LOCATION_CHANGED = 1;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // TODO: Make LOCATION_SERVICE into NATIVE_LOCATION_SERVICE later.
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, mListener);
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 0, mListener);
        mLocationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 5000, 0, mListener);

        AssetManager assetManager = getAssets();
        mDB = new POIDatabase();
        try {
            InputStreamReader reader =
                    new InputStreamReader(assetManager.open("los-angeles.amenities.json"));
            mDB.LoadFromJson(reader);
        } catch (IOException ex) {
            Log.d(TAG, "IOException!");
        }
    }

    private LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Location new_location = new Location(location);
            Message m = mHandler.obtainMessage(MSG_LOCATION_CHANGED, new_location);
            mHandler.sendMessage(m);
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    };

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOCATION_CHANGED:
                    handleLocationChanged((Location) msg.obj);

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private void handleLocationChanged(Location location) {
        float lat = (float) location.getLatitude();
        float lon = (float) location.getLongitude();
        float accuracy_meters = location.getAccuracy();
        HashSet<String> set = mDB.query(lat, lon, accuracy_meters);

        if (set.size() > 0) {
            Log.d(TAG, "----FOUND: places" + set.size() + " within " + accuracy_meters + " meters.");
            for (String s : set) {
                Log.v(TAG, "place >> " + s);
            }
        }

    }
}
