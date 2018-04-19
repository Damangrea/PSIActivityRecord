package com.packet_systems.activity.psiactivityrecord;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.packet_systems.activity.psiactivityrecord.data.ContractData;
import com.packet_systems.activity.psiactivityrecord.data.CustomerData;
import com.packet_systems.activity.psiactivityrecord.data.SubTechnologyData;
import com.packet_systems.activity.psiactivityrecord.data.TeamData;
import com.packet_systems.activity.psiactivityrecord.data.TechnologyData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends MyForm {
    EditText tvUserName, tvPassword;
    Button btnSubmit;

    @Override
    public void doParsedJSON(JSONArray p_json) {
        JSONObject jsonObject;
        for (int i = 0; i < p_json.length(); i++) {
            try {
                jsonObject = p_json.getJSONObject(i);

                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("login", true); // value to store
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                editor.putString("IMEI", tmPhone.getDeviceId());
                editor.putInt("userId", jsonObject.getInt("id"));
                editor.putString("userName", jsonObject.getString("name"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void doErrorResponse(VolleyError p_error) {
        System.out.println("GENERAL ERROR RESPOSE ");
    }

    @Override
    public void doResponseSuccess() {
        /*

         */
        //get all data parameter before go to main activity
        getParameter();
        ///////////
        Intent intent = new Intent(this, ListingActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void doResponseErrorCode(String p_errorCode) {
        Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void doResponseErrorJSON() {
        super.doResponseErrorJSON();
        //error retrieve
        Toast.makeText(this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("login", false); // value to store
        editor.putInt("userId", 0);
        editor.putString("userName", "");
        editor.commit();


        tvUserName = (EditText) findViewById(R.id.username);
        tvPassword = (EditText) findViewById(R.id.password);
        btnSubmit = (Button) findViewById(R.id.submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> params = new HashMap<>();
                params.put("username", tvUserName.getText().toString());
                params.put("password", tvPassword.getText().toString());
                if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                params.put("imei", tmPhone.getDeviceId());
                params.put("device_date", DateFormat.getDateTimeInstance().format(new Date()));
                submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/mobile_login/", params, "Login");
            }
        });

    }

    void getParameter() {
        String urlCustomer = "https://ssportal-tbs-2.packet-systems.com/mobile/get_customers/", urlProject = "https://ssportal-tbs-2.packet-systems.com/mobile/get_contracts/", urlTechnology = "https://ssportal-tbs-2.packet-systems.com/mobile/get_technology/", urlTeam = "https://ssportal-tbs-2.packet-systems.com/mobile/get_team/";
        StringRequest postRequest;

        postRequest = new StringRequest(Request.Method.POST, urlCustomer,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PARAM CUSTOMER", "response :" + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String transResponse = jsonObj.getString("errorcode");
                            JSONArray jsonArray = jsonObj.getJSONArray("data");
                            JSONObject jsonChild;
                            CustomerData customerData;
                            if (jsonArray.length() > 0) {
                                listCustomerData = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonChild = jsonArray.getJSONObject(i);
                                    customerData = new CustomerData();
                                    customerData.setId(jsonChild.getString("id"));
                                    customerData.setName(jsonChild.getString("name"));
                                    listCustomerData.add(customerData);
                                }
                                System.out.println("CUSTOMER DATA LENGTH: " + listCustomerData.size());
                            }
                        } catch (JSONException e) {
                            Log.d("", "errorJSON");
                            Toast.makeText(getBaseContext(), "" + " : Error Response From Server", Toast.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        Volley.newRequestQueue(this).add(postRequest);

        postRequest = new StringRequest(Request.Method.POST, urlProject,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PARAM PROJECT", "response :" + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String transResponse = jsonObj.getString("errorcode");
                            JSONArray jsonArray = jsonObj.getJSONArray("data");
                            JSONObject jsonChild;
                            ContractData contractData;
                            if (jsonArray.length() > 0) {
                                listContractData = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonChild = jsonArray.getJSONObject(i);
                                    contractData = new ContractData();
                                    contractData.setId(jsonChild.getString("id"));
//                                    contractData.setAccount_manager_id(jsonChild.getString("name"));
                                    contractData.setCompany_profile(jsonChild.getString("customer"));
//                                    contractData.setContract_no(jsonChild.getString("name"));
//                                    contractData.setNote(jsonChild.getString("name"));
//                                    contractData.setPo(jsonChild.getString("name"));
                                    contractData.setProject_name(jsonChild.getString("project"));
                                    contractData.setSo(jsonChild.getString("so"));
                                    listContractData.add(contractData);
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("", "errorJSON");
                            Toast.makeText(getBaseContext(), "" + " : Error Response From Server", Toast.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        Volley.newRequestQueue(this).add(postRequest);

//        postRequest= new StringRequest(Request.Method.POST, urlTeam,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("PARAM ", "response :" + response);
//                        try {
//                            JSONObject jsonObj = new JSONObject(response);
//                            String transResponse = jsonObj.getString("errorcode");
//                            JSONArray jsonArray = jsonObj.getJSONArray("data");
//                            JSONObject jsonChild;
//                            TeamData teamData;
//                            if(jsonArray.length()>0){
//                                listTeamData = new ArrayList<>();
//                                for (int i = 0; i <jsonArray.length() ; i++) {
//                                    jsonChild = jsonArray.getJSONObject(i);
//                                    teamData = new TeamData();
//                                    teamData.setId(jsonChild.getString("id"));
//                                    teamData.setTeam_leader_id(jsonChild.getString("name"));
//                                    teamData.setTeam_name(jsonChild.getString("name"));
//                                    teamData.setTeam_sort_name(jsonChild.getString("name"));
//                                    listTeamData.add(teamData);
//                                }
//                            }
//                        } catch (JSONException e) {
//                            Log.d("", "errorJSON");
//                            Toast.makeText(getBaseContext(),""+" : Error Response From Server",Toast.LENGTH_LONG);
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        error.printStackTrace();
//                    }
//                }
//        );
//        Volley.newRequestQueue(this).add(postRequest);
        postRequest = new StringRequest(Request.Method.POST, urlTechnology,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PARAM ", "response :" + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String transResponse = jsonObj.getString("errorcode");
                            JSONArray jsonArray = jsonObj.getJSONArray("data");
                            JSONObject jsonChild;
                            TechnologyData technologyData;
                            if (jsonArray.length() > 0) {
                                listTechnologyData = new ArrayList<>();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonChild = jsonArray.getJSONObject(i);
                                    technologyData = new TechnologyData();
                                    technologyData.setId(jsonChild.getString("id"));
                                    technologyData.setName(jsonChild.getString("name"));
                                    listTechnologyData.add(technologyData);
                                }
                                getParameterSubTech();
                            }
                        } catch (JSONException e) {
                            Log.d("", "errorJSON");
                            Toast.makeText(getBaseContext(), "" + " : Error Response From Server", Toast.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void getParameterSubTech() {
        String urlSubTechnology = "https://ssportal-tbs-2.packet-systems.com/mobile/get_subtechnology/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, urlSubTechnology,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("PARAM ", "response :" + response);
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String transResponse = jsonObj.getString("errorcode");
                            JSONArray jsonArray = jsonObj.getJSONArray("data");
                            JSONObject jsonChild;
                            SubTechnologyData subTechnologyData;
                            TechnologyData technologyData;
                            if (jsonArray.length() > 0) {
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    jsonChild = jsonArray.getJSONObject(i);
                                    subTechnologyData = new SubTechnologyData();
                                    subTechnologyData.setId(jsonChild.getString("id"));
                                    subTechnologyData.setName(jsonChild.getString("name"));
                                    subTechnologyData.setTech_id(jsonChild.getString("technology"));
                                    for (int j = 0; j < listTechnologyData.size(); j++) {
                                        technologyData = listTechnologyData.get(j);
                                        if (technologyData.getName().equals(subTechnologyData.getTech_id())) {
                                            technologyData.addSubtech(subTechnologyData);
                                            break;
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("", "errorJSON");
                            Toast.makeText(getBaseContext(), "" + " : Error Response From Server", Toast.LENGTH_LONG);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        );
        Volley.newRequestQueue(this).add(postRequest);
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*
        Saved for production
            if(isRooted()){
                btnSubmit.setEnabled(false);
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("ROOT DETECTED");
                builder.setMessage("Aplikasi ini tidak diperuntukkan untuk Handset yang di ROOT");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                        homeIntent.addCategory( Intent.CATEGORY_HOME );
                        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(homeIntent);
                    }
                });
                builder.show();
            }
        */
    }
}
