package com.packet_systems.activity.psiactivityrecord.data;

import java.io.Serializable;

/**
 * Created by damangrea on 05/04/18.
 */

public class ActivityData implements Serializable {
    private String id;
    private String scheduled;
    private String activity;
    private String activity_type;
    private String activity_status;
    private String customer;
    private String so;
    private String approved;
    private String project;
    private String so_temp;
    private String time_start;
    private String time_end;
    private String rfpa_number;
    private String path;
    private String crm_number;
    private String mail_log;
    private String technology;
    private String subtechnology;
    private String lab_category;
    private String self_study_company;
    private String am;
    private String pend_so;

    public String getActivity() {
        return activity;
    }

    public String getActivity_status() {
        return activity_status;
    }

    public String getActivity_type() {
        return activity_type;
    }

    public String getApproved() {
        return approved;
    }

    public String getCrm_number() {
        return crm_number;
    }

    public String getCustomer() {
        return customer;
    }

    public String getPath() {
        return path;
    }

    public String getProject() {
        return project;
    }

    public String getMail_log() {
        return mail_log;
    }

    public String getRfpa_number() {
        return rfpa_number;
    }

    public String getScheduled() {
        return scheduled;
    }

    public String getId() {
        return id;
    }

    public String getSo() {
        return so;
    }

    public String getSo_temp() {
        return so_temp;
    }

    public String getTechnology() {
        return technology;
    }

    public String getTime_end() {
        return time_end;
    }

    public String getLab_category() {
        return lab_category;
    }

    public String getAm() {
        return am;
    }

    public String getSubtechnology() {
        return subtechnology;
    }

    public String getTime_start() {
        return time_start;
    }

    public String getPend_so() {
        return pend_so;
    }

    public String getSelf_study_company() {
        return self_study_company;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setActivity_status(String activity_status) {
        this.activity_status = activity_status;
    }

    public void setActivity_type(String activity_type) {
        this.activity_type = activity_type;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setCrm_number(String crm_number) {
        this.crm_number = crm_number;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setMail_log(String mail_log) {
        this.mail_log = mail_log;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public void setRfpa_number(String rfpa_number) {
        this.rfpa_number = rfpa_number;
    }

    public void setScheduled(String scheduled) {
        this.scheduled = scheduled;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setSo(String so) {
        this.so = so;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public void setSo_temp(String so_temp) {
        this.so_temp = so_temp;
    }

    public void setTime_end(String time_end) {
        this.time_end = time_end;
    }

    public void setTime_start(String time_start) {
        this.time_start = time_start;
    }

    public void setLab_category(String lab_category) {
        this.lab_category = lab_category;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public void setPend_so(String pend_so) {
        this.pend_so = pend_so;
    }

    public void setSelf_study_company(String self_study_company) {
        this.self_study_company = self_study_company;
    }

    public void setSubtechnology(String subtechnology) {
        this.subtechnology = subtechnology;
    }
}
