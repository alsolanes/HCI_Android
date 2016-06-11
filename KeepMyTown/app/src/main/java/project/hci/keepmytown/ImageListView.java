package project.hci.keepmytown;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageListView extends AppCompatActivity implements AdapterView.OnItemClickListener, OnMapReadyCallback {
    private ListView listView;

    public static final String GET_IMAGE_URL="http://keepmytown.esy.es/PhotoUpload/getAllImages.php";

    public GetAlImages getAlImages;
    public GoogleMap map;

    public static final String BITMAP_ID = "BITMAP_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list_view);

        //listView = (ListView) findViewById(R.id.listView);
        //listView.setOnItemClickListener(this);
        getURLs();

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void getImages(){
        class GetImages extends AsyncTask<Void,Void,Void>{
            ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageListView.this,"Downloading images...","Please wait...",false,false);
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                loading.dismiss();
                //Toast.makeText(ImageListView.this,"Success",Toast.LENGTH_LONG).show();
                double[] lats = GetAlImages.lats;
                double[] lngs = GetAlImages.lngs;
                String[] coords = new String[GetAlImages.lats.length];
                for (int i = 0;i<GetAlImages.lats.length;i++){
                    coords[i] = String.valueOf(lats[i])+" "+String.valueOf(lngs[i]);
                }

                for(int i = 0; i< getAlImages.lats.length;i++) {
                    //infowindow adapter
                    final Bitmap bmF = getAlImages.bitmaps[i];
                    map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                        @Override
                        public View getInfoWindow(Marker marker) {
                            return null;
                        }

                        @Override
                        public View getInfoContents(Marker marker) {
                            View myContentsView = getLayoutInflater().inflate(R.layout.info_window_layout, null);
                            ImageView imageView = (ImageView) myContentsView.findViewById(R.id.imgView_map_info_content);
                            imageView.setImageBitmap(bmF);
                            return myContentsView;
                        }
                    });


                    map.addMarker(new MarkerOptions()
                            .position(new LatLng(getAlImages.lats[i], getAlImages.lngs[i]))
                            .title("Marker"));
                }
                //CustomList customList = new CustomList(ImageListView.this,GetAlImages.imageURLs,GetAlImages.bitmaps, coords);

                //listView.setAdapter(customList);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    getAlImages.getAllImages();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
        GetImages getImages = new GetImages();
        getImages.execute();
    }

    private void getURLs() {
        class GetURLs extends AsyncTask<String,Void,String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ImageListView.this,"Loading...","Please Wait...",true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                getAlImages = new GetAlImages(s);
                getImages();
            }

            @Override
            protected String doInBackground(String... strings) {
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }
            }
        }
        GetURLs gu = new GetURLs();
        gu.execute(GET_IMAGE_URL);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent intent = new Intent(this, ViewFullImage.class);
        intent.putExtra(BITMAP_ID,i);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.map = map;
        if(getAlImages!=null){
        for(int i = 0; i< getAlImages.lats.length;i++) {

            BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(getAlImages.bitmaps[i]);

            map.addMarker(new MarkerOptions()
                    .position(new LatLng(getAlImages.lats[i], getAlImages.lngs[i]))
                    .title("Marker")
                    .icon(bd));
        }
    }}
}
