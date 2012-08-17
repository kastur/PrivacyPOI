package android.override.poi;

import android.util.Log;
import com.bbn.openmap.util.quadtree.QuadTree;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.Reader;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Vector;

public class POIDatabase {
    private static final String TAG = "POIDatabase";
    private QuadTree mIndex;
    private boolean mLoaded = false;

    public void LoadFromJson(Reader reader) {
        mIndex = new QuadTree();
        final Reader final_reader = reader;
        Thread loader = new Thread() {
            @Override
            public void run() {
                JsonElement root = new JsonParser().parse(final_reader);
                int count = 0;
                for (Entry<String, JsonElement> entry : root.getAsJsonObject().entrySet()) {
                    String key = entry.getKey();
                    for (JsonElement o : entry.getValue().getAsJsonArray()) {
                        JsonArray list = o.getAsJsonArray();
                        float lon = list.get(0).getAsFloat();
                        float lat = list.get(1).getAsFloat();
                        String name = list.get(2).getAsString();
                        mIndex.put(lat, lon, key + ": " + name);
                    }
                    Log.d(TAG, "Loaded: #" + count + " : " + key);
                    count++;
                }
                mLoaded = true;
            }
        };
        loader.start();
    }

    public HashSet<String> query(float lat, float lon, float accuracy_meters) {
        if (!mLoaded) {
            return new HashSet<String>();
        }
        float eps = accuracy_meters / 10.0f / 110922.0f;
        Vector<String> vector = mIndex.get(lat + eps, lon - eps, lat - eps, lon + eps);
        HashSet<String> hashSet = new HashSet<String>(vector);
        String s = (String) mIndex.get(lat, lon, 100);
        //HashSet<String> hashSet = new HashSet<String>();
        //hashSet.add(s);
        return hashSet;
    }
}
