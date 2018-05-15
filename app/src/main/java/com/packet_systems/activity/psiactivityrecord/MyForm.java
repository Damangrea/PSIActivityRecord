package com.packet_systems.activity.psiactivityrecord;

/**
 * Created by damangrea on 04/04/18.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.packet_systems.activity.psiactivityrecord.data.ActivityData;
import com.packet_systems.activity.psiactivityrecord.data.ContractData;
import com.packet_systems.activity.psiactivityrecord.data.CustomerData;
import com.packet_systems.activity.psiactivityrecord.data.TeamData;
import com.packet_systems.activity.psiactivityrecord.data.TechnologyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class MyForm extends AppCompatActivity {
    static final int act_draft = 0;
    static final int act_submit = 1;
    static final int act_delete = 2;
    static final int act_checkin = 3;
    static final int act_checkout = 4;
    static final int act_approve = 5;
    static final int act_reject = 6;
    static int menu_process, location_process;
    protected DrawerLayout drawerLayout;
    protected ActionBarDrawerToggle drawerToggle;
    TelephonyManager tmPhone;
    static List<ActivityData> listActivityData;
    static List<ContractData> listContractData;
    static List<TeamData> listTeamData;
    static List<CustomerData> listCustomerData;
    static List<TechnologyData> listTechnologyData;

    SharedPreferences preferences;

    public abstract void doParsedJSON(JSONArray p_json) throws JSONException;

    public abstract void doErrorResponse(VolleyError p_error);

    public abstract void doResponseSuccess();

    public abstract void doResponseErrorCode(String p_errorCode);

    public void displayForm(int p_view) {
        setContentView(p_view);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tmPhone = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                            return myTrustedAnchors;
                        }

                        @Override
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        }
                    }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void setMenuLayout(NavigationView navView) {
        System.out.println("SET LISTENER");
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                System.out.println("ON ITEM SELECT");
                switch (item.getItemId()) {
                    case (R.id.activity_list): {
                        Intent intent = new Intent(getApplicationContext(), ListingActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case (R.id.contract_list): {
                        Intent intent = new Intent(getApplicationContext(), ListingContract.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    case (R.id.logout): {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                }
                return true;
            }
        });
    }

    public boolean onParamValidation() {
        return true;
    }

    ;

    public void submitRequest(String url, final Map<String, String> p_request, final String p_sIdLog) {
        if (onParamValidation()) {
            StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(p_sIdLog, "response :" + response);
                            doResponse(response, p_sIdLog);
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
                    return p_request;
                }
            };
            Volley.newRequestQueue(this).add(postRequest);
        }
    }

    ;

    public void submitSystemLogs(String url, final Map<String, String> p_request, final String p_sIdLog) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(p_sIdLog, "response :" + response);
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
                return p_request;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    ;

    public void doResponse(String p_response, String p_sIdLog) {
        try {
            JSONObject jsonObj = new JSONObject(p_response);
            String transResponse = jsonObj.getString("errorcode");
            if (transResponse.equals("1")) {
                doResponseSuccess();

                JSONArray jsonArray = jsonObj.getJSONArray("data");
                Log.d(p_sIdLog, "data length: " + jsonArray.length());

                if (jsonArray.length() > 0) {
                    doParsedJSON(jsonArray);
                }
            } else {
                doResponseErrorCode(transResponse);
            }


        } catch (JSONException e) {
            Log.d(p_sIdLog, "errorJSON : " + e.toString());
            doResponseErrorJSON();
            Toast.makeText(getBaseContext(), p_sIdLog + " : Error Response From Server", Toast.LENGTH_LONG);
        }
    }

    protected void doResponseErrorJSON() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        System.out.println("ALL PREF: " + preferences.getAll().toString());
        if (preferences.getString("IMEI", "").length() > 0) {
            Map<String, String> params = new HashMap<>();
            //check root
            if (isRooted()) {
                params.put("root", "detected");
            }
            //check mock location
            if (Settings.Secure.getString(getApplicationContext().getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
                params.put("mock", "detected");
                //list mock apps
                params.put("mock_list", areThereMockPermissionApps(getApplicationContext()));
            }
            if (params.size() > 0) {
                //input IMEI and userId
                params.put("IMEI", preferences.getString("IMEI", ""));
                params.put("user_id", "" + preferences.getInt("userId", 0));
                params.put("device_date", DateFormat.getDateTimeInstance().format(new Date()));

                //submit log to server

                submitSystemLogs("https://ssportal-tbs-2.packet-systems.com/mobile/mobile_systems_submit/", params, "SystemParameter");
            }
        }
        if (Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION).equals("1")) {
            //go to mock location setting

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("MOCK LOCATION SETTING ON");
            builder.setMessage("Silahkan menonaktifkan settingan mock location pada handset");
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS));
                }
            });
            builder.show();
        }
    }

    public static boolean isRooted() {

        // get from build info
        String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return true;
        }

        // check if /system/app/Superuser.apk is present
        try {
            File file = new File("/system/app/Superuser.apk");
            if (file.exists()) {
                return true;
            }
        } catch (Exception e1) {
            // ignore
        }

        // try executing commands
        return canExecuteCommand("/system/xbin/which su")
                || canExecuteCommand("/system/bin/which su") || canExecuteCommand("which su");
    }

    // executes a command on the system
    private static boolean canExecuteCommand(String command) {
        boolean executedSuccesfully;
        try {
            Runtime.getRuntime().exec(command);
            executedSuccesfully = true;
        } catch (Exception e) {
            executedSuccesfully = false;
        }

        return executedSuccesfully;
    }

    public static String areThereMockPermissionApps(Context context) {
        int count = 0;
        String appsPackageName = "";

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages =
                pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName,
                        PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i]
                                .equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {
                            count++;
                            appsPackageName += ";" + applicationInfo.packageName;
                        }
                    }
                }
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("Got exception ", e.getMessage());
            }
        }

        if (count > 0)
            return appsPackageName;
        return "";
    }

    public static double distance(double lat1, double lat2
            , double lon1, double lon2
//            , double el1, double el2
    ) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters
        return distance;
//        double height = el1 - el2;
//
//        distance = Math.pow(distance, 2) ;//+ Math.pow(height, 2);
//
//        return Math.sqrt(distance);
    }
}
