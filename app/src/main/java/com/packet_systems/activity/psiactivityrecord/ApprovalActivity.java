package com.packet_systems.activity.psiactivityrecord;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.packet_systems.activity.psiactivityrecord.adapter.ActivityAdapter;
import com.packet_systems.activity.psiactivityrecord.adapter.ApprovalAdapter;
import com.packet_systems.activity.psiactivityrecord.data.ActivityData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApprovalActivity extends MyForm {
    NavigationView navView;
    LocationManager locationManager;
    ApprovalAdapter activityAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<ActivityData> activityDataList = new ArrayList<>();
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.approval_activity_list);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navView = (NavigationView) findViewById(R.id.navigation_layout);
        setMenuLayout(navView);

//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationListener = new MyLocationListener();

        recyclerView = (RecyclerView) findViewById(R.id.rv_activity);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

//        fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(ApprovalActivity.this, ActivityEdit.class);
//                startActivity(intent);
//            }
//        });


        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void doParsedJSON(JSONArray p_json) throws JSONException {
        activityDataList = new ArrayList<>();
        ActivityData activityData;
        JSONObject jsonObject;
        for (int i = 0; i < p_json.length(); i++) {
            jsonObject = p_json.getJSONObject(i);
            activityData = new ActivityData();
            activityData.setId(jsonObject.getString("id"));
            activityData.setScheduled(jsonObject.getString("scheduled"));
            activityData.setTime_start(jsonObject.getString("time_start"));
            activityData.setTime_end(jsonObject.getString("time_end"));
            activityData.setActivity(jsonObject.getString("activity"));
            activityData.setProject(jsonObject.getString("project"));
            activityData.setCustomer(jsonObject.getString("customer"));
            activityData.setActivity_status(jsonObject.getString("status"));
            activityData.setCrm_number(jsonObject.getString("crmNumber"));
            activityData.setActivity_type(jsonObject.getString("category"));
            activityData.setMail_log(jsonObject.getString("mailLog"));
//            activityData.setass(jsonObject.getString("orderAssignment"));
            activityData.setSo(jsonObject.getString("SONumber"));
            activityData.setApproved(jsonObject.getString("approved"));
            activityData.setRfpa_number(jsonObject.getString("rfpa"));
            activityData.setLab_category(jsonObject.getString("lab"));
            activityData.setTechnology(jsonObject.getString("technology"));
            activityData.setSubtechnology(jsonObject.getString("subtechnology"));
            activityData.setCheckin_long(jsonObject.getString("checkin_long"));
            activityData.setCheckin_lat(jsonObject.getString("checkin_lat"));
            activityData.setCheckin_alt(jsonObject.getString("checkin_alt"));
            activityData.setCheckin_time(jsonObject.getString("checkin_time"));
            activityData.setCheckout_long(jsonObject.getString("checkout_long"));
            activityData.setCheckout_lat(jsonObject.getString("checkout_lat"));
            activityData.setCheckout_alt(jsonObject.getString("checkout_alt"));
            activityData.setCheckout_time(jsonObject.getString("checkout_time"));
            activityData.setEngineer_name(jsonObject.getString("name"));
            activityDataList.add(activityData);
        }
        ////////
        activityAdapter = new ApprovalAdapter(activityDataList);
        recyclerView.setAdapter(activityAdapter);
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        ActivityData activityData1 = activityAdapter.getItem(position);
                        Intent intent = new Intent(getApplicationContext(), ApprovalEdit.class);
                        intent.putExtra("ActivityData", activityData1);
                        startActivity(intent);
                    }
                })
        );
    }


    @Override
    public void doErrorResponse(VolleyError p_error) {

    }

    @Override
    public void doResponseSuccess() {

    }

    @Override
    public void doResponseErrorCode(String p_errorCode) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getBoolean("login", false)) {
            Map<String, String> params = new HashMap<>();
            params.put("id", "" + preferences.getInt("userId", 0));
            submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/get_approval", params, "Activities");
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
