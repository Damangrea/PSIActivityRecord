package com.packet_systems.activity.psiactivityrecord.data;

import java.io.Serializable;

/**
 * Created by damangrea on 05/04/18.
 */

public class ContractData implements Serializable {
    private String id;
    private String contract_no;
    private String so;
    private String po;
    private String note;
    private String project_name;
    private String company_profile;
    private String account_manager_id;
    private String primary_engineer_id;

    public String getSo() {
        return so;
    }

    public String getAccount_manager_id() {
        return account_manager_id;
    }

    public String getCompany_profile() {
        return company_profile;
    }

    public String getContract_no() {
        return contract_no;
    }

    public String getId() {
        return id;
    }

    public String getNote() {
        return note;
    }

    public String getPo() {
        return po;
    }

    public String getPrimary_engineer_id() {
        return primary_engineer_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public void setAccount_manager_id(String account_manager_id) {
        this.account_manager_id = account_manager_id;
    }

    public void setCompany_profile(String company_profile) {
        this.company_profile = company_profile;
    }

    public void setContract_no(String contract_no) {
        this.contract_no = contract_no;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPo(String po) {
        this.po = po;
    }

    public void setPrimary_engineer_id(String primary_engineer_id) {
        this.primary_engineer_id = primary_engineer_id;
    }

    public String getStringConcat() {
        return project_name + "(" + so + ")";
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }
}
