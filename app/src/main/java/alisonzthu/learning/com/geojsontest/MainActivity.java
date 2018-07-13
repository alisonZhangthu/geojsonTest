package alisonzthu.learning.com.geojsontest;

import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mapbox.geojson.Feature;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.style.layers.FillLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.List;

import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillColor;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.fillOpacity;

/**
 * Draw a vector polygon on a map with the Mapbox Android SDK.
 */
public class MainActivity extends AppCompatActivity {

    private MapView mapView;
    private MapboxMap mapboxMap;
//    private static final String geojsonSourceId = "west russia";
//    private static final String geojsonLayerId = "west russia layer";
    private static final String[] countries = {"jpn", "kor", "prk", "rus", "kaz","kgz", "mng", "chn", "twn", "brn", "idn", "khm", "tls",
                                                "lao", "mmr", "mys", "phl", "tha", "vnm"};

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
                MainActivity.this.mapboxMap = mapboxMap;
                mapboxMap.addOnMapClickListener(getOnMapClickListener());
                drawPolygon(mapboxMap);
            }
        });
    }

    private void drawPolygon(MapboxMap mapboxMap) {
        try {
            //old render west russia
//            InputStream inputStream = getAssets().open("west_russia.json");
//            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
//
//            StringBuilder sb = new StringBuilder();
//            int cp;
//            while( (cp = rd.read()) != -1) {
//                sb.append((char) cp);
//            }
//            inputStream.close();
//
//            // Parse JSON
//            GeoJsonSource source = new GeoJsonSource(geojsonSourceId, sb.toString());
//            mapboxMap.addSource(source);
//
//            FillLayer layer = new FillLayer(geojsonLayerId, geojsonSourceId);
//            layer.setProperties(fillOpacity(0.5f), fillColor("#f00"));
//            mapboxMap.addLayer(layer);

            // render east asia:
            for (String country: countries) {
                InputStream inputStream = getAssets().open(country+".json");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

                StringBuilder sb = new StringBuilder();
                int cp;
                while( (cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }
                inputStream.close();

                // Parse JSON
                GeoJsonSource source = new GeoJsonSource(country, sb.toString());
                mapboxMap.addSource(source);
                FillLayer layer = new FillLayer(country, country);
                layer.setProperties(fillOpacity(0.5f), fillColor("#f00"));
                mapboxMap.addLayer(layer);
            }

        } catch (Exception e) {
            Log.e("alison", "error reading file");
        }
    }

    private MapboxMap.OnMapClickListener getOnMapClickListener() {
        return new MapboxMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng point) {
                //
                PointF pointf = mapboxMap.getProjection().toScreenLocation(point);
                RectF rectF = new RectF(pointf.x - 10, pointf.y - 10, pointf.x + 10, pointf.y + 10);
                List<Feature> featureList = mapboxMap.queryRenderedFeatures(rectF, countries);
                if (featureList.size() > 0) {
                    for (Feature feature : featureList) {
                        Log.d("Feature found with %1$s", feature.toJson());
                        Toast.makeText(MainActivity.this, R.string.click_on_polygon_toast,
                                Toast.LENGTH_SHORT).show();
                    }
                    mapboxMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(point.getLatitude(), point.getLongitude()), 2));
                }
            }
        };
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
