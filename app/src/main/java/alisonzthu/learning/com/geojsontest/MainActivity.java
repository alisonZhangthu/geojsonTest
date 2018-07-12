package alisonzthu.learning.com.geojsontest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

/**
 * Draw a vector polygon on a map with the Mapbox Android SDK.
 */
public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private static final String geojsonSourceId = "west russia";
    private static final String geojsonLayerId = "west russia layer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Mapbox access token is configured here. This needs to be called either in your application
        // object or in the same activity which contains the mapview.
        Mapbox.getInstance(this, getString(R.string.my_mapbox_access_token));

        // This contains the MapView in XML and needs to be called after the access token is configured.
        setContentView(R.layout.activity_main);

        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                drawPolygon(mapboxMap);
            }
        });
    }

    private void drawPolygon(MapboxMap mapboxMap) {
        try {
            InputStream inputStream = getAssets().open("west_russia.json");
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

            StringBuilder sb = new StringBuilder();
            int cp;
            while( (cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            inputStream.close();

            // Parse JSON
            GeoJsonSource source = new GeoJsonSource(geojsonSourceId, sb.toString());
            mapboxMap.addSource(source);

            FillLayer layer = new FillLayer(geojsonLayerId, geojsonSourceId);
            layer.setProperties(fillOpacity(0.5f), fillColor("#f00"));
            mapboxMap.addLayer(layer);
        } catch (Exception e) {
            Log.e("alison", "error reading file");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
