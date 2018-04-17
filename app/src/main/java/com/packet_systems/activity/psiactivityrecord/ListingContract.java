package com.packet_systems.activity.psiactivityrecord;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.android.volley.VolleyError;
import com.packet_systems.activity.psiactivityrecord.adapter.ContractAdapter;
import com.packet_systems.activity.psiactivityrecord.data.ContractData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListingContract extends MyForm {
    NavigationView navView;
    ContractAdapter contractAdapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<ContractData> contractDataList = new ArrayList<>();
    AutoCompleteTextView editCustomer, editSO;

    @Override
    public void doParsedJSON(JSONArray p_json) throws JSONException {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contract_listing);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navView = (NavigationView) findViewById(R.id.navigation_layout);
        setMenuLayout(navView);

        recyclerView = (RecyclerView) findViewById(R.id.rv_contracts);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        editCustomer = (AutoCompleteTextView) findViewById(R.id.ed_customer);
        String[] customerList = new String[listCustomerData.size()];
        for (int i = 0; i < listCustomerData.size(); i++) {
            customerList[i] = listCustomerData.get(i).getName();
        }
        editCustomer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, customerList));
        editCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContractData cd;
                ArrayList<ContractData> contractTemp = new ArrayList<>();
                int x = 0;
                for (int i = 0; i < listContractData.size(); i++) {
                    cd = listContractData.get(i);
                    if (cd.getCompany_profile().equals(editCustomer.getText().toString())) {
                        contractTemp.add(cd);
                    }
                }
                contractAdapter = new ContractAdapter(contractTemp);
                recyclerView.setAdapter(contractAdapter);
            }
        });
        editSO = (AutoCompleteTextView) findViewById(R.id.ed_sonumber);
        String[] contract = new String[listContractData.size()];
        for (int i = 0; i < listContractData.size(); i++) {
            contract[i] = listContractData.get(i).getSo();
        }
        editSO.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, contract));
        editSO.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContractData cd;
                ArrayList<ContractData> contractTemp = new ArrayList<>();
                int x = 0;
                for (int i = 0; i < listContractData.size(); i++) {
                    cd = listContractData.get(i);
                    if (cd.getSo().equals(editSO.getText().toString())) {
                        contractTemp.add(cd);
                    }
                }
                contractAdapter = new ContractAdapter(contractTemp);
                recyclerView.setAdapter(contractAdapter);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (preferences.getBoolean("login", false)) {
            Map<String, String> params = new HashMap<>();
//            params.put("id",""+preferences.getInt("userId",0));
            submitRequest("https://ssportal-tbs-2.packet-systems.com/mobile/get_contracts", params, "Activities");
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
