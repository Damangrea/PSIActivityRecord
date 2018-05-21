package com.packet_systems.activity.psiactivityrecord;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.packet_systems.activity.psiactivityrecord.data.ActivityData;
import com.packet_systems.activity.psiactivityrecord.data.ContractData;
import com.packet_systems.activity.psiactivityrecord.data.SubTechnologyData;
import com.packet_systems.activity.psiactivityrecord.data.TechnologyData;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by damangrea on 09/04/18.
 */

public class ActivityStandbyEdit extends MyForm {
    NavigationView navView;
    LocationManager locationManager;
    MyLocationListener locationListener;
    AutoCompleteTextView editCustomer;
    EditText editScheduled, editActivity, editCheckinLong, editCheckinLat, editCheckoutLong, editCheckoutLat, editCheckinAlt, editCheckoutAlt, editCheckinTime, editCheckoutTime;
    Button btnCheckIn, btnCheckOut;
    TextInputLayout lyScheduled, lyCustomer, lyTimeStart, lyTimeEnd, lyActivity;
    TextView tvLocation;
    ToggleButton tgle_gps;
    RelativeLayout rltv_geolocation;
    String idActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView) findViewById(R.id.navigation_layout);
        setMenuLayout(navView);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initUI();
        Intent intent = getIntent();
        ActivityData activityData;
        if (intent.hasExtra("ActivityData")) {
            activityData = (ActivityData) intent.getSerializableExtra("ActivityData");
            idActivity = activityData.getId();
            editScheduled.setText(activityData.getScheduled());
            editCustomer.setText(activityData.getCustomer());
            editActivity.setText(activityData.getActivity());
            editCheckinLong.setText(activityData.getCheckin_long());
            editCheckinLat.setText(activityData.getCheckin_lat());
            editCheckoutLong.setText(activityData.getCheckout_long());
            editCheckoutLat.setText(activityData.getCheckout_lat());
            editCheckinLong.setText(activityData.getCheckin_long());
            editCheckinLat.setText(activityData.getCheckin_lat());
            editCheckinAlt.setText(activityData.getCheckin_alt());
            editCheckinTime.setText(activityData.getCheckin_time());
            editCheckoutLong.setText(activityData.getCheckout_long());
            editCheckoutLat.setText(activityData.getCheckout_lat());
            editCheckoutAlt.setText(activityData.getCheckout_alt());
            editCheckoutTime.setText(activityData.getCheckout_time());
//            editAccountManager.setText(activityData.get());

        } else {
            //new add form
            idActivity = "";
            editCheckinLong.setText("");
            editCheckinLat.setText("");
            editCheckoutLong.setText("");
            editCheckoutLat.setText("");
        }
        tgle_gps.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                        /*
                            Check GPS STATUS -> go to GPS SETTING IF DISABLED
                         */
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStandbyEdit.this);
                        builder.setTitle("GPS OFF");
                        builder.setMessage("Turn On GPS to get Location");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });
                        builder.show();
                        tgle_gps.setChecked(false);
                        return;
                    }
                    if (idActivity.length() == 0) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityStandbyEdit.this);
                        builder.setTitle("Set to Draft First");
                        builder.setMessage("Set to draft first in order get location");
                        builder.setPositiveButton("OK", null);
                        builder.show();
                        tgle_gps.setChecked(false);
                        return;
                    }
                    if (locationListener != null) {
                        locationManager.removeUpdates(locationListener);
                    }
                    //set ulang listener
                    locationListener = new MyLocationListener();
                    btnCheckIn.setEnabled(true);
                    btnCheckIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            location_process = act_checkin;
                            locationListener.setEditLongLatAlt(editCheckinLong, editCheckinLat, editCheckinAlt, editCheckinTime);
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
                        }
                    });
                    btnCheckOut.setEnabled(true);
                    btnCheckOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            location_process = act_checkout;
                            locationListener.setEditLongLatAlt(editCheckoutLong, editCheckoutLat, editCheckoutAlt, editCheckoutTime);
                            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locationListener);
                        }
                    });
                } else {
                    if (locationListener != null) {
                        locationManager.removeUpdates(locationListener);
                    }
                    btnCheckIn.setEnabled(false);
                    btnCheckOut.setEnabled(false);
                }
            }
        });
    }

    private void initUI() {

        editScheduled = (EditText) findViewById(R.id.edit_scheduled);
        editScheduled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mYear = mcurrentDate.get(Calendar.YEAR);
                int mMonth = mcurrentDate.get(Calendar.MONTH);
                int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(ActivityStandbyEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        editScheduled.setText(year + "-" + (month + 1) + "-" + day);
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });
        editCustomer = (AutoCompleteTextView) findViewById(R.id.edit_customer);
        String[] customerList = new String[listCustomerData.size()];
        for (int i = 0; i < listCustomerData.size(); i++) {
            customerList[i] = listCustomerData.get(i).getName();
        }
        editCustomer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, customerList));

        editActivity = (EditText) findViewById(R.id.edit_activity);
        editCheckinLong = (EditText) findViewById(R.id.edit_chkin_longitude);
        editCheckinLong.setEnabled(false);
        editCheckinLat = (EditText) findViewById(R.id.edit_chkin_latitude);
        editCheckinLat.setEnabled(false);
        editCheckoutLong = (EditText) findViewById(R.id.edit_chkout_longitude);
        editCheckoutLong.setEnabled(false);
        editCheckoutLat = (EditText) findViewById(R.id.edit_chkout_latitude);
        editCheckoutLat.setEnabled(false);
        editCheckinAlt = (EditText) findViewById(R.id.edit_chkin_altitude);
        editCheckoutAlt = (EditText) findViewById(R.id.edit_chkout_altitude);
        editCheckinTime = (EditText) findViewById(R.id.edit_chkin_time);
        editCheckoutTime = (EditText) findViewById(R.id.edit_chkout_time);
        editCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContractData cd;
                ArrayList<String> contractTemp = new ArrayList<>();
                int x = 0;
                for (int i = 0; i < listContractData.size(); i++) {
                    cd = listContractData.get(i);
                    if (cd.getCompany_profile().equals(editCustomer.getText().toString())) {
                        contractTemp.add(cd.getStringConcat());
                    }
                }
                String[] contracts = new String[contractTemp.size()];
                for (int i = 0; i < contractTemp.size(); i++) {
                    contracts[i] = contractTemp.get(i);
                }

            }
        });

        btnCheckIn = (Button) findViewById(R.id.btn_checkin);
        btnCheckIn.setEnabled(false);
        btnCheckOut = (Button) findViewById(R.id.btn_checkout);
        btnCheckOut.setEnabled(false);

        tgle_gps = (ToggleButton) findViewById(R.id.tgle_getgps);
        tgle_gps.setChecked(false);

        lyScheduled = (TextInputLayout) findViewById(R.id.ly_scheduled);
        lyCustomer = (TextInputLayout) findViewById(R.id.ly_customer);
        lyTimeStart = (TextInputLayout) findViewById(R.id.ly_timestart);
        lyTimeEnd = (TextInputLayout) findViewById(R.id.ly_timeend);
        lyActivity = (TextInputLayout) findViewById(R.id.ly_activity);


        tvLocation = (TextView) findViewById(R.id.tv_location);

        rltv_geolocation = (RelativeLayout) findViewById(R.id.rltv_geolocation);

    }

    @Override
    public void doParsedJSON(JSONArray p_json) throws JSONException {

    }

    @Override
    public void doErrorResponse(VolleyError p_error) {

    }

    @Override
    public void doResponseSuccess() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void doResponseErrorCode(String p_errorCode) {
        switch (p_errorCode) {
            case "0": {
                switch (menu_process) {
                    case act_draft: {
                        Toast.makeText(this, "Insert Gagal", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_submit: {
                        Toast.makeText(this, "Submit Gagal", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_delete: {
                        Toast.makeText(this, "Delete Gagal", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            }
            case "2": {
                switch (menu_process) {
                    case act_draft: {
                        Toast.makeText(this, "Tidak Ada Data yang di Update", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_submit: {
                        Toast.makeText(this, "Submit Gagal, Melewati Batas Waktu", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_delete: {
                        Toast.makeText(this, "Insert Gagal", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                break;
            }
            case "3": {
                switch (menu_process) {
                    case act_draft: {
                        break;
                    }
                    case act_submit: {
                        Toast.makeText(this, "Submit Gagal, Isi Check in dan Check Out sebelum submit", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_delete: {
                        break;
                    }
                }
                break;
            }
            case "4": {
                switch (menu_process) {
                    case act_draft: {
                        Toast.makeText(this, " Gagal, Overlapping activity", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_submit: {
                        Toast.makeText(this, "Submit Gagal, Overlapping activity", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case act_delete: {
                        break;
                    }
                }
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_menu_standby_save: {
                Map<String, String> params = new HashMap<>();
                params.put("id", idActivity);
                params.put("customer", editCustomer.getText().toString());
                params.put("scheduled", editScheduled.getText().toString());
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("contact_profile_id", "" + preferences.getInt("userId", 0));
                params.put("activity", editActivity.getText().toString());
//                params.put("checkin_long", editCheckinLong.getText().toString());
//                params.put("checkin_lat", editCheckinLat.getText().toString());
//                params.put("checkin_alt", editCheckinAlt.getText().toString());
//                params.put("checkout_long", editCheckinLong.getText().toString());
//                params.put("checkout_lat", editCheckinLat.getText().toString());
//                params.put("checkout_alt", editCheckinAlt.getText().toString());
                submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/mobile_standby_save/", params, "Save Draft");
                menu_process = act_draft;
                break;
            }
            case R.id.ic_menu_standby_delete: {
                Map<String, String> params = new HashMap<>();
                params.put("id", idActivity);
                submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/mobile_standby_delete/", params, "Delete");
                menu_process = act_delete;
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private class MyLocationListener implements LocationListener {

        EditText editLongitude, editLatitude, editAltitude, editTime;

        @Override
        public void onLocationChanged(Location loc) {
            System.out.println("LOCATION CHANGED");
            List<String> providers = locationManager.getProviders(true);
            System.out.println(providers.toString());
            Location location = null;

            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            for (int i = 0; i < providers.size(); i++) {
                location = locationManager.getLastKnownLocation(providers.get(i));

//                System.out.println(providers.get(i) + "-" + (location == null ? "NULL" : location.getLongitude() + "~" + location.getLatitude()));
//
                if (location != null) {
                    if (location.getAccuracy() < 25.0) {
                        if (preferences.getString("longitude", "").length() > 0) {
                            double longitude = Double.parseDouble(preferences.getString("longitude", ""));
                            double latitude = Double.parseDouble(preferences.getString("latitude", ""));
                            if (distance(latitude, location.getLatitude(), longitude, location.getLongitude()) < 30) {
                                editLatitude.setText("" + location.getLatitude());
                                editLongitude.setText("" + location.getLongitude());
                                editAltitude.setText("" + location.getAltitude());
                                editTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
                                Toast.makeText(ActivityStandbyEdit.this, "Accuracy :" + location.getAccuracy(), Toast.LENGTH_SHORT).show();
                                sendLocationtoServer();
                                locationManager.removeUpdates(this);
                            } else {
                                Toast.makeText(ActivityStandbyEdit.this, "Distance difference over 30 meters than registered location", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            editLatitude.setText("" + location.getLatitude());
                            editLongitude.setText("" + location.getLongitude());
                            editAltitude.setText("" + location.getAltitude());
                            editTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
                            Toast.makeText(ActivityStandbyEdit.this, "Accuracy :" + location.getAccuracy(), Toast.LENGTH_SHORT).show();
                            sendLocationtoServer();
                            locationManager.removeUpdates(this);
                        }
                    } else {
                        Toast.makeText(ActivityStandbyEdit.this, "Accuracy :" + location.getAccuracy() + ", please get into open space to get better accuracy", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

        public void setEditLongLatAlt(EditText editLongitude, EditText editLatitude, EditText editAltitude, EditText editTime) {
            this.editLongitude = editLongitude;
            this.editLatitude = editLatitude;
            this.editAltitude = editAltitude;
            this.editTime = editTime;
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {
            System.out.println("GPS Enabled");
        }

        @Override
        public void onProviderDisabled(String provider) {
            System.out.println("GPS Disabled");

        }

    }

    @Override
    protected void onPause() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (locationManager != null && locationListener != null) {
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }

    private boolean emptyValidation(EditText editText) {
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setError("Field Required!");
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean onParamValidation() {
        boolean bValidate = true;
        if (!emptyValidation(editCustomer)) {
            bValidate = false;
        }
        return bValidate;
    }

    private void sendLocationtoServer() {
        String urlChecklocation = (location_process == act_checkin ? "https://ssportal-tbs-2.packet-systems.com/mobile/mobile_standby_checkin_submit/" : "https://ssportal-tbs-2.packet-systems.com/mobile/mobile_standby_checkout_submit/");

        final Map<String, String> params = new HashMap<>();
        params.put("imei", preferences.getString("IMEI", ""));
        params.put("id_act", idActivity);
        params.put("long", (location_process == act_checkin ? editCheckinLong.getText().toString() : editCheckoutLong.getText().toString()));
        params.put("lat", (location_process == act_checkin ? editCheckinLat.getText().toString() : editCheckoutLat.getText().toString()));
        params.put("alt", (location_process == act_checkin ? editCheckinAlt.getText().toString() : editCheckoutAlt.getText().toString()));
        params.put("user_id", "" + preferences.getInt("userId", 0));
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlChecklocation,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Check Location", "response :" + response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
}
