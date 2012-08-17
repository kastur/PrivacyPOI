package android.override.poi;

import android.app.Activity;
import android.content.Intent;

/**
 * Created with IntelliJ IDEA.
 * User: k
 * Date: 8/17/12
 * Time: 2:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleActivity extends Activity {
    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = new Intent(this, POIService.class);
        startService(intent);
    }
}