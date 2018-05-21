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

public class ApprovalEdit extends MyForm {
    NavigationView navView;
    LocationManager locationManager;
    MyLocationListener locationListener;
    AutoCompleteTextView editCustomer, editProject;
    EditText editWorkStatus, editWorkCategory, editScheduled, editTimeStart, editTimeEnd, editActivity, editCheckinLong, editCheckinLat, editCheckoutLong, editCheckoutLat, editSONumber, editRFPANumber, editCRMNumber, editMailLog, editTechnology, editSubTechnology, editAccountManager, editCheckinAlt, editCheckoutAlt, editCheckinTime, editCheckoutTime;
    Button btnCheckIn, btnCheckOut;
    TextInputLayout lyWorkCategory, lyScheduled, lyCustomer, lyTimeStart, lyTimeEnd, lyActivity, lyProject, lySONumber, lyRFPANumber, lyCRMNumber, lyMailLog, lyTechnology, lySubtechnology, lyAccountManager;
    ToggleButton tgle_gps, tgle_pendso, tgle_labcategory, tgle_selfstudy;
    TextView tvLocation;
    RelativeLayout rltv_geolocation, rltv_pendso, rltv_labcategory, rltv_selfstudy;
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
            editWorkStatus.setText(activityData.getActivity_status());
            editWorkCategory.setText(activityData.getActivity_type());
            editScheduled.setText(activityData.getScheduled());
            editCustomer.setText(activityData.getCustomer());
            editTimeStart.setText(activityData.getTime_start());
            editTimeEnd.setText(activityData.getTime_end());
            editActivity.setText(activityData.getActivity());
            editCheckinLong.setText(activityData.getCheckin_long());
            editCheckinLat.setText(activityData.getCheckin_lat());
            editCheckoutLong.setText(activityData.getCheckout_long());
            editCheckoutLat.setText(activityData.getCheckout_lat());
            editProject.setText(activityData.getProject());
            editSONumber.setText(activityData.getSo());
            editRFPANumber.setText(activityData.getRfpa_number());
            editCRMNumber.setText(activityData.getCrm_number());
            editMailLog.setText(activityData.getMail_log());
            editTechnology.setText(activityData.getTechnology());
            editSubTechnology.setText(activityData.getSubtechnology());
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
            Toast.makeText(this, "ERROR FETCHING DATA", Toast.LENGTH_SHORT).show();
            finish();
        }
        reDrawEditView();
    }

    private void initUI() {
        editWorkStatus = (EditText) findViewById(R.id.edit_workstatus);
        editWorkStatus.setEnabled(false);
        editWorkCategory = (EditText) findViewById(R.id.edit_workcategory);
        editWorkCategory.setEnabled(false);
        editScheduled = (EditText) findViewById(R.id.edit_scheduled);
        editScheduled.setEnabled(false);
        editCustomer = (AutoCompleteTextView) findViewById(R.id.edit_customer);
        editCustomer.setEnabled(false);
        editTimeStart = (EditText) findViewById(R.id.edit_timestart);
        editTimeStart.setEnabled(false);
        editTimeEnd = (EditText) findViewById(R.id.edit_timeend);
        editTimeEnd.setEnabled(false);
        editActivity = (EditText) findViewById(R.id.edit_activity);
        editActivity.setEnabled(false);
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
        editProject = (AutoCompleteTextView) findViewById(R.id.edit_project);
        editProject.setEnabled(false);
        editSONumber = (EditText) findViewById(R.id.edit_sonumber);
        editSONumber.setEnabled(false);
        editRFPANumber = (EditText) findViewById(R.id.edit_rfpanumber);
        editRFPANumber.setEnabled(false);
        editCRMNumber = (EditText) findViewById(R.id.edit_crmnumber);
        editCRMNumber.setEnabled(false);
        editMailLog = (EditText) findViewById(R.id.edit_maillog);
        editMailLog.setEnabled(false);
        editTechnology = (EditText) findViewById(R.id.edit_technology);
        editTechnology.setEnabled(false);
        editSubTechnology = (EditText) findViewById(R.id.edit_subtechnology);
        editSubTechnology.setEnabled(false);
        editAccountManager = (EditText) findViewById(R.id.edit_accountmanager);
        editAccountManager.setEnabled(false);

        btnCheckIn = (Button) findViewById(R.id.btn_checkin);
        btnCheckIn.setEnabled(false);
        btnCheckOut = (Button) findViewById(R.id.btn_checkout);
        btnCheckOut.setEnabled(false);

        tgle_gps = (ToggleButton) findViewById(R.id.tgle_getgps);
        tgle_gps.setChecked(false);
        tgle_gps.setEnabled(false);

        tgle_pendso = (ToggleButton) findViewById(R.id.tgle_pendingso);
        tgle_pendso.setChecked(false);
        tgle_pendso.setEnabled(false);


        tgle_labcategory = (ToggleButton) findViewById(R.id.tgle_labcategory);
        tgle_labcategory.setChecked(false);
        tgle_labcategory.setEnabled(false);

        tgle_selfstudy = (ToggleButton) findViewById(R.id.tgle_selfstudy);
        tgle_selfstudy.setChecked(false);
        tgle_selfstudy.setEnabled(false);

        lyWorkCategory = (TextInputLayout) findViewById(R.id.ly_workcategory);
        lyScheduled = (TextInputLayout) findViewById(R.id.ly_scheduled);
        lyCustomer = (TextInputLayout) findViewById(R.id.ly_customer);
        lyTimeStart = (TextInputLayout) findViewById(R.id.ly_timestart);
        lyTimeEnd = (TextInputLayout) findViewById(R.id.ly_timeend);
        lyActivity = (TextInputLayout) findViewById(R.id.ly_activity);
        lyProject = (TextInputLayout) findViewById(R.id.ly_project);
        lySONumber = (TextInputLayout) findViewById(R.id.ly_sonumber);
        lyRFPANumber = (TextInputLayout) findViewById(R.id.ly_rfpanumber);
        lyCRMNumber = (TextInputLayout) findViewById(R.id.ly_crmnumber);
        lyMailLog = (TextInputLayout) findViewById(R.id.ly_maillog);
        lyTechnology = (TextInputLayout) findViewById(R.id.ly_technology);
        lySubtechnology = (TextInputLayout) findViewById(R.id.ly_subtechnology);
        lyAccountManager = (TextInputLayout) findViewById(R.id.ly_accountmanager);

        tvLocation = (TextView) findViewById(R.id.tv_location);

        rltv_geolocation = (RelativeLayout) findViewById(R.id.rltv_geolocation);
        rltv_pendso = (RelativeLayout) findViewById(R.id.rltv_pendingso);
        rltv_labcategory = (RelativeLayout) findViewById(R.id.rltv_labcategory);
        rltv_selfstudy = (RelativeLayout) findViewById(R.id.rltv_selfstudy);
    }

    public void reDrawEditView() {
        lyWorkCategory.setVisibility(View.GONE);
        lyScheduled.setVisibility(View.VISIBLE);
        lyCustomer.setVisibility(View.GONE);
        lyTimeStart.setVisibility(View.GONE);
        lyTimeEnd.setVisibility(View.GONE);
        lyActivity.setVisibility(View.GONE);
        lyProject.setVisibility(View.GONE);
        lySONumber.setVisibility(View.GONE);
        lyRFPANumber.setVisibility(View.GONE);
        lyCRMNumber.setVisibility(View.GONE);
        lyMailLog.setVisibility(View.GONE);
        lyTechnology.setVisibility(View.GONE);
        lySubtechnology.setVisibility(View.GONE);
        lyAccountManager.setVisibility(View.GONE);

        rltv_labcategory.setVisibility(View.GONE);
        rltv_pendso.setVisibility(View.GONE);
        tvLocation.setVisibility(View.GONE);
        rltv_geolocation.setVisibility(View.GONE);
        rltv_selfstudy.setVisibility(View.GONE);

        switch (editWorkStatus.getText().toString()) {
            case "WORK": {
                lyWorkCategory.setVisibility(View.VISIBLE);
                lyTimeStart.setVisibility(View.VISIBLE);
                lyTimeEnd.setVisibility(View.VISIBLE);
                switch (editWorkCategory.getText().toString()) {
                    case "Standby On-site/Standby On-call/Monitoring":
//                        //stand by
//                        $("#tr_file_data_edit").show();
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_mail_log_edit").show();
                        lyMailLog.setVisibility(View.VISIBLE);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
//                        $("#file_data").prop('required', true);
//                        $("#desc_label").show();
                        break;
                    case "Resident":
                        //resident
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_mail_log_edit").show();
                        lyMailLog.setVisibility(View.VISIBLE);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
//                        $("#tr_crm_edit").show();
                        lyCRMNumber.setVisibility(View.VISIBLE);
//                        $("#desc_label").show();
                        tvLocation.setVisibility(View.VISIBLE);
                        rltv_geolocation.setVisibility(View.VISIBLE);
                        break;
                    case "Maintenance-Documentation/Report"://doc
                        //maintenance crm not mandatory
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
//                        $("#tr_crm_edit").show();
                        lyCRMNumber.setVisibility(View.VISIBLE);
//                        $("#desc_label").show();
//                        $("#pend_so").show();
//                        $("#pend_so_label").show();
                        break;
                    case "Maintenance-Prev Maintenance"://pm
                    case "Maintenance-Meeting/Discussion"://meeting/discussion
                    case "Maintenance-Others"://others
                        //maintenance tanpa crm
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
//                        $("#desc_label").show();
//                        $("#pend_so").show();
                        rltv_pendso.setVisibility(View.VISIBLE);
//                        $("#pend_so_label").show();
                        break;
                    case "Maintenance-Corrective":
                    case "Maintenance-Change Request":
                        //corrective & change req dengan crm
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_crm_edit").show();
                        lyCRMNumber.setVisibility(View.VISIBLE);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
//                        $("#crm").prop('required', true);
//                        $("#crm_mandatory").show();
//                        $("#desc_label").show();
//                        $("#pend_so").show();
                        rltv_pendso.setVisibility(View.VISIBLE);
//                        $("#pend_so_label").show();
                        break;
                    case "Project-Implementation/Integration/Migration/Troubleshooting":
                    case "Project-Documentation":
                    case "Project-Staging":
                    case "Project-Survey":
                    case "Project-Others":
                    case "Project-Meeting/Discussion":
                        //project
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
//                        $("#desc_label").show();
                        break;
                    case "Internal Administration (Internal paperwork such as Settle TRS/TRA)":
                        //internal administration
//                        $("#desc_label").show();
                        break;
                    case "Support Sales":
                        //support sales
//                        $("#tr_file_data_edit").show();
//                        $("#tr_mail_log_edit").show();
                        lyMailLog.setVisibility(View.VISIBLE);
//                        $("#file_data").prop('required', true);
//                        $("#account_manager").prop('required', true);
                        lyAccountManager.setVisibility(View.VISIBLE);
//                        $("#tr_account_manager_edit").show();
//                        $("#desc_label").show();
//                        //2017-05-09 team enterprise(om man, om top) support sales ada customer mandatory & so number not mandatory
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        break;
                    case "Support PS":
                    case "Support SSS":
                        //support other div
//                        $("#tr_file_data_edit").show();
//                        $("#tr_mail_log_edit").show();
                        lyMailLog.setVisibility(View.VISIBLE);
//                        $("#file_data").prop('required', true);
//                        $("#desc_label").show();
//                        //2017-05-17 semua support ada tambahan customer mandatory & so number not mandatory
//                        $("#tr_customer").show();
                        lyCustomer.setVisibility(View.VISIBLE);
//                        $("#customer").prop('required', true);
//                        $("#tr_contract_id_edit").show();
                        lyProject.setVisibility(View.VISIBLE);
//                        $("#tr_so_edit").show();
                        lySONumber.setVisibility(View.VISIBLE);
                        break;
                    case "Training/Exam":
                        //training exam
//                        $("#tr_activity_training_paid_unpaid_edit").show();
//                        $("#rfpa").prop('required', true);
                        lyRFPANumber.setVisibility(View.VISIBLE);
//                        $("#desc_label").show();
//                        paidunpaid();
                        break;
                    case "Self Study":
                        //self study
//                        $("#tr_tech_edit").show();
                        lyTechnology.setVisibility(View.VISIBLE);
//                        $("#tr_subtech_edit").show();
                        lySubtechnology.setVisibility(View.VISIBLE);
//                        $("#tr_lab_edit").show();
                        rltv_labcategory.setVisibility(View.VISIBLE);
//                        $("#tr_activity_self_study_edit").show();
//                        rltv_selfstudy.setVisibility(View.VISIBLE);
//                        $("#topic_label").show();
                        break;
                }
            }
            case "ANNUAL LEAVE":
            case "SICK LEAVE":
            case "OTHER LEAVE": {
                lyScheduled.setVisibility(View.VISIBLE);
                lyActivity.setVisibility(View.VISIBLE);
                break;
            }
        }
    }

    public void rePopulatesubTech() {
        TechnologyData technologyData = null;
        for (int i = 0; i < listTechnologyData.size(); i++) {
            technologyData = listTechnologyData.get(i);
            if (technologyData.getName().equals(editTechnology.getText().toString())) {
                break;
            }
        }
        List<SubTechnologyData> subTechnologyDataList = technologyData.getListSubtech();
        final String[] subTechnology = new String[subTechnologyDataList.size()];
        for (int i = 0; i < subTechnologyDataList.size(); i++) {
            subTechnology[i] = subTechnologyDataList.get(i).getName();
        }
        final ArrayAdapter<String> subTechnologyAdapter = new ArrayAdapter<String>(ApprovalEdit.this, android.R.layout.simple_spinner_dropdown_item, subTechnology);
        new AlertDialog.Builder(ApprovalEdit.this)
                .setTitle("Select Sub Technology")
                .setAdapter(subTechnologyAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        editSubTechnology.setText(subTechnology[which].toString());
                        dialog.dismiss();
                    }
                }).create().show();
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
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_approve_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ic_menu_approve: {
                Map<String, String> params = new HashMap<>();
                params.put("id", idActivity);
                menu_process = act_approve;
                submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/mobile_activity_approve/", params, "Save Draft");
                break;
            }
            case R.id.ic_menu_reject: {
                //show alert dialog
                final EditText editText = new EditText(this);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Reject Reason");
                builder.setMessage("Reason for rejected activity");
                builder.setView(editText);
                builder.setPositiveButton("Reject", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map<String, String> params = new HashMap<>();
                        params.put("id", idActivity);
                        params.put("remark", editText.getText().toString());
                        submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/mobile_activity_reject/", params, "Save Draft");
                        menu_process = act_reject;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
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
                                Toast.makeText(ApprovalEdit.this, "Accuracy :" + location.getAccuracy(), Toast.LENGTH_SHORT).show();
                                sendLocationtoServer();
                                locationManager.removeUpdates(this);
                            } else {
                                Toast.makeText(ApprovalEdit.this, "Distance difference over 30 meters than registered location", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            editLatitude.setText("" + location.getLatitude());
                            editLongitude.setText("" + location.getLongitude());
                            editAltitude.setText("" + location.getAltitude());
                            editTime.setText(DateFormat.getDateTimeInstance().format(new Date()));
                            Toast.makeText(ApprovalEdit.this, "Accuracy :" + location.getAccuracy(), Toast.LENGTH_SHORT).show();
                            sendLocationtoServer();
                            locationManager.removeUpdates(this);
                        }
                    } else {
                        Toast.makeText(ApprovalEdit.this, "Accuracy :" + location.getAccuracy() + ", please get into open space to get better accuracy", Toast.LENGTH_SHORT).show();
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
        switch (editWorkStatus.getText().toString()) {
            case "WORK": {
                switch (editWorkCategory.getText().toString()) {
                    case "Standby On-site/Standby On-call/Monitoring":
//                        //stand by
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
//                        $("#file_data").prop('required', true);
                        break;
                    case "Resident":
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
                        break;
                    case "Maintenance-Documentation/Report"://doc
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
                        break;
                    case "Maintenance-Prev Maintenance"://pm
                    case "Maintenance-Meeting/Discussion"://meeting/discussion
                    case "Maintenance-Others"://others
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
                        break;
                    case "Maintenance-Corrective":
                    case "Maintenance-Change Request":
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
//                        $("#crm").prop('required', true);
                        if (!emptyValidation(editCRMNumber)) {
                            bValidate = false;
                        }
                        break;
                    case "Project-Implementation/Integration/Migration/Troubleshooting":
                    case "Project-Documentation":
                    case "Project-Staging":
                    case "Project-Survey":
                    case "Project-Others":
                    case "Project-Meeting/Discussion":
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
                        break;
                    case "Support Sales":
                        // $("#file_data").prop('required', true);
//                        $("#account_manager").prop('required', true);
                        if (!emptyValidation(editAccountManager)) {
                            bValidate = false;
                        }
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
                        break;
                    case "Support PS":
                    case "Support SSS":
//                        $("#file_data").prop('required', true);
//                        $("#customer").prop('required', true);
                        if (!emptyValidation(editCustomer)) {
                            bValidate = false;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return bValidate;
    }

    private void sendLocationtoServer() {
        String urlChecklocation = (location_process == act_checkin ? "https://ssportal-tbs-2.packet-systems.com/mobile/mobile_checkin_submit/" : "https://ssportal-tbs-2.packet-systems.com/mobile/mobile_checkout_submit/");

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
