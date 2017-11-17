package vn.altalab.app.crmvietpack;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.Normalizer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import vn.altalab.app.crmvietpack.presenter.LocationAdapter;
import vn.altalab.app.crmvietpack.utility.MySingleton;

public class MapActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    EditText myFilter;
    Button btSearch;
    private PullToRefreshListView lstLocation;
    public static List<vn.altalab.app.crmvietpack.object.Location> locations;
    LocationAdapter locationAdapter;
    private static final String PREFS_NAME = "CRMVietPrefs";
    private SharedPreferences settings;

    private GoogleMap myMap;
    private ProgressDialog myProgress;
    private static final String MYTAG = "MYTAG";
    private Double Lat, Lng;
    String fullName, date;
    Marker currentMarker;
    android.app.AlertDialog.Builder builder;
    // Mã yêu cầu uhỏi người dùng cho phép xem vị trí hiện tại của họ (***).
    // Giá trị mã 8bit (value < 256).
    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    ImageView imgbtnsearch;
    String adress;
    GoogleApiClient mGoogleApiClient;
    Location myLocation = null;
    LocationManager locationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Dialog();
        } else {
            map();
        }
    }

    public void map() {
        if (settings == null) {
            settings = getSharedPreferences(PREFS_NAME, 0);
        }
        locations = new ArrayList<>();
        doGetLocation(0);


        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Map Loading ...");
        myProgress.setMessage("Please wait...");
        myProgress.setCancelable(true);

        // Hiển thị Progress Bar
        myProgress.show();

        // Tạo Progress Bar

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        SupportMapFragment mapFragment
                = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);


        // Sét đặt sự kiện thời điểm GoogleMap đã sẵn sàng.
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                onMyMapReady(googleMap);


            }
        });
//search place
        iniView();

///==================================================================

//        lstLocation = (PullToRefreshListView) findViewById(R.id.lstLocation);

        lstLocation = (PullToRefreshListView) findViewById(R.id.lstLocation);
        lstLocation.getRefreshableView().setDividerHeight(5);
//        locations = new ArrayList<>();

        locationAdapter = new LocationAdapter(this, R.layout.new_list_item_location, locations);


        lstLocation.setMode(PullToRefreshBase.Mode.BOTH);


//        doGetLocation(0);

        lstLocation.setAdapter(locationAdapter);


        lstLocation.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                locations.clear();
                locationAdapter.notifyDataSetChanged();
                doGetLocation(0);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                if ("".equals(settings.getString("api_server", ""))) {
                    Toast.makeText(refreshView.getContext(), refreshView.getContext().getResources().getString(R.string.login_empty_api_server), Toast.LENGTH_SHORT).show();
                    return;
                }
                doGetLocation(locations.size());
            }
        });
        lstLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                myMap.clear();
                Lat = locations.get(position - 1).getLat();
                Lng = locations.get(position - 1).getLng();
//                date = locations.get(position - 1).getDate();
                fullName = locations.get(position - 1).getUserName();
                adress = locations.get(position - 1).getAdressloca();
                if (Lat != 0 & Lng != 0) {
                    otherPlace();
                }

            }
        });

        myFilter = (EditText) findViewById(R.id.edtSearch1);
        myFilter.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        myFilter.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    doFilter(0);
                    //tắt bàn phím sau khi click
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


        btSearch = (Button) findViewById(R.id.btSearch);


    }
//lấy tên địa chỉ trên google map

    private String getAddress(double LATITUDE, double LONGITUDE) {

        String location_string = "";
        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng=" + LATITUDE + "," + LONGITUDE + "&sensor=true");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject = new JSONObject(stringBuilder.toString());


            JSONObject location = jsonObject.getJSONArray("results").getJSONObject(0);
            location_string = new String(location.getString("formatted_address").getBytes("ISO-8859-1"), "UTF-8");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return location_string;


//        String strAdd = "";
//        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
//        try {
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null) {
//                String address = addresses.get(0).getAddressLine(0);
//
//                if (!address.equals("null")) {
//                    strAdd = address;
//                } else {
//                    strAdd = "";
//                }
//
//            } else {
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return strAdd;


    }


    public void Dialog() {
        builder = new android.app.AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Chú ý !");
        builder.setMessage("Bạn chưa bật GPS  ! ");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                map();
//                finish();
//                Intent startMain = new Intent(Intent.ACTION_MAIN);
//                startMain.addCategory(Intent.CATEGORY_HOME);
//                startActivity(startMain);
            }

        });
        builder.create().show();
    }

    //search place (tim kiem dia diem tren google)
    private void iniView() {
        imgbtnsearch = (ImageView) findViewById(R.id.imgbtnsearch);
        imgbtnsearch.setOnClickListener(Click_Search);
    }

    final View.OnClickListener Click_Search = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                        .build(MapActivity.this);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
            } catch (GooglePlayServicesRepairableException e) {
            } catch (GooglePlayServicesNotAvailableException e) {
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Toast.makeText(this, place.getName(), Toast.LENGTH_LONG).show();
                myMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
                myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 17));
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }
    //------------------------------------

    private void onMyMapReady(GoogleMap googleMap) {

        // Lấy đối tượng Google Map ra:
        myMap = googleMap;

        // Thiết lập sự kiện đã tải Map thành công
        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {

                // Đã tải thành công thì tắt Dialog Progress đi
                myProgress.dismiss();

                // Hiển thị vị trí người dùng.
                askPermissionsAndShowMyLocation();
//                showMyLocation();

            }
        });
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myMap.setMyLocationEnabled(true);
    }

    private void askPermissionsAndShowMyLocation() {


        // Với API >= 23, phải hỏi người dùng cho phép xem vị trí của họ.
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);


            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Các quyền cần người dùng cho phép.
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION};

                // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
                ActivityCompat.requestPermissions(this, permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

                return;
            }
        }

        // Hiển thị vị trí trong list định vị lên bản đồ.
//        this.showOtherLocation();
        this.showMyLocation();
    }


    // Khi người dùng trả lời yêu cầu cấp quyền (cho phép hoặc từ chối).
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {


                // Chú ý: Nếu yêu cầu bị bỏ qua, mảng kết quả là rỗng.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "đã từ chối !", Toast.LENGTH_LONG).show();

                    // Hiển thị vị trí hiện thời trên bản đồ.
                    this.showMyLocation();
                } else {
                    Toast.makeText(this, "đã được cho phép !", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }


    // Chỉ gọi phương thức này khi đã có quyền xem vị trí người dùng.
    private void showMyLocation() {

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            if (Lat != null & Lng != null) {
                LatLng latLng = new LatLng(Lat, Lng);
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)             // Sets the center of the map to location user
                        .zoom(13)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                MarkerOptions option = new MarkerOptions();
                option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location));
                option.title(settings.getString("userName", ""));
                option.position(latLng);
                currentMarker = myMap.addMarker(option);
                currentMarker.showInfoWindow();
            }
            // GPS mat ket noi
        } else {
            LatLng latLng = new LatLng(21.028471, 105.843056);

            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)             // Sets the center of the map to location user
                    .zoom(13)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                    .build();
            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        }
// Load tất cả vị trí ra màn hình
//        placeMarker();
//        } else {
//////            Toast.makeText(this, "Location not found!", Toast.LENGTH_LONG).show();
//////            Log.i(MYTAG, "Location not found");
//        }

//        doGetLocation(0);
    }

    //location of others-------------------------------------
    public void otherPlace() {

        LatLng latLng = new LatLng(Lat, Lng);


        myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng)             // Sets the center of the map to location user
                .zoom(15)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
                .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                .build();                   // Creates a CameraPosition from the builder
        myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // Thêm Marker cho Map:
        MarkerOptions option = new MarkerOptions();
        option.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location));
//        option.title(fullName + " ( " + date + " )");
        option.title(fullName);
//        option.snippet(adress);
        option.position(latLng);
        currentMarker = myMap.addMarker(option);
        currentMarker.showInfoWindow();

//        currentMarker = myMap.addMarker(option);
//
//        myMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//                currentMarker.showInfoWindow();
//                return true;
//            }
//        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MapActivity.this, CrmMainActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    //==============================================================================================


    public void doGetLocation(final int index) {
        if (index == 0) {
            locations.clear();
        }

        String url = settings.getString("api_server", "") + "/api/v1/checkin?index=" + String.valueOf(index) + "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "");

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    JSONArray array = jsonObject.optJSONArray("checkin");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);


                        vn.altalab.app.crmvietpack.object.Location location = new vn.altalab.app.crmvietpack.object.Location();

                        location.setUserId(object.optLong("USER_ID"));
                        location.setLat(object.optDouble("LATITUDE"));
                        location.setLng(object.optDouble("LONGITUDE"));
                        try {
                            location.setDate(simpleDateFormat.parse(object.optString("UPD_DTTM")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        location.setUserName(object.optString("USER_NAME"));
                        location.setUserFullname(object.optString("USER_FULL_NAME"));
                        if (object.optDouble("LATITUDE") != 0 & object.optDouble("LONGITUDE") != 0
                                & object.optDouble("LATITUDE") != 777 & object.optDouble("LONGITUDE") != 777) {
                            location.setAdressloca(getAddress(object.optDouble("LATITUDE"), object.optDouble("LONGITUDE")));


                        } else {
                            location.setAdressloca("Chưa xác định được vị trí !");

                        }
                        locations.add(location);





                    }


                    locationAdapter.notifyDataSetChanged();
                }
                if (lstLocation.isRefreshing()) {
                    lstLocation.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (lstLocation.isRefreshing()) {
                    lstLocation.onRefreshComplete();
                }
                Log.e("loadalllocation", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);


    }


    public void clickSearch(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        doFilter(0);
    }

    public static String KeyAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

    public void doFilter(final int index) {
        String KeyWord = myFilter.getText().toString();
        myProgress = new ProgressDialog(this);
        myProgress.setTitle("Đang tìm ...");
        myProgress.setCancelable(true);

        // Hiển thị Progress Bar
        myProgress.show();


        locations.clear();
        locationAdapter.notifyDataSetChanged();
        String url = settings.getString("api_server", "") + "/api/v1/checkin/search?index_search=" + String.valueOf(index)
                + "&USER_ID=" + settings.getString(getResources().getString(R.string.user_id_object), "")
                + "&keyword=" + KeyAccent(KeyWord);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                if (jsonObject != null && !"".equals(jsonObject.toString()) && "success".equals(jsonObject.optString("messages"))) {

                    final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

                    JSONArray array = jsonObject.optJSONArray("checkin");
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject object = array.optJSONObject(i);


                        vn.altalab.app.crmvietpack.object.Location location = new vn.altalab.app.crmvietpack.object.Location();
                        location.setUserId(object.optLong("USER_ID"));
                        location.setLat(object.optDouble("LATITUDE"));
                        location.setLng(object.optDouble("LONGITUDE"));
                        try {
                            location.setDate(simpleDateFormat.parse(object.optString("UPD_DTTM")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        location.setUserName(object.optString("USER_NAME"));
                        location.setUserFullname(object.optString("USER_FULL_NAME"));
                        if (object.optDouble("LATITUDE") != 0 & object.optDouble("LONGITUDE") != 0
                                & object.optDouble("LATITUDE") != 777 & object.optDouble("LONGITUDE") != 777) {
                            location.setAdressloca(getAddress(object.optDouble("LATITUDE"), object.optDouble("LONGITUDE")));
                        } else {
                            location.setAdressloca("Chưa xác định được vị trí !");

                        }
                        locations.add(location);



                    }

                    locationAdapter.notifyDataSetChanged();
                    myProgress.dismiss();
                }
                if (lstLocation.isRefreshing()) {
                    lstLocation.onRefreshComplete();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                if (lstLocation.isRefreshing()) {
                    lstLocation.onRefreshComplete();
                }
                myProgress.dismiss();
                Log.e("loadalllocation", volleyError.toString());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=UTF-8");
                headers.put("User-agent", System.getProperty("http.agent"));
                return headers;
            }
        };

        int socketTimeout = 60000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        MySingleton.getInstance(this).addToRequestQueue(request);
    }


    public Marker placeMarker() {
        Marker m = null;
//        Double kinhdo = Double.valueOf(location.getLng());
//        Double vido = Double.valueOf(location.getLat());
        //Toast.makeText(MainActivity.this,""+kinhdo+vido,Toast.LENGTH_LONG).show();
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getLat() != 0 & locations.get(i).getLng() != 0) {
                LatLng Place = new LatLng(locations.get(i).getLat(), locations.get(i).getLng());
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(shopPlace, 15));

                m = myMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_action_location))
                        .title(locations.get(i).getUserName())
                        .position(Place));
            }

        }
        return m;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        myLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (myLocation != null) {

            Lat = myLocation.getLatitude();
            Lng = myLocation.getLongitude();

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            mGoogleApiClient.connect();
        }

        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

}
