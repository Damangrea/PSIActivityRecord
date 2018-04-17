package com.packet_systems.activity.psiactivityrecord.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by damangrea on 05/04/18.
 */

public class TechnologyData implements Serializable {
    private String id;
    private String name;
    private ArrayList<SubTechnologyData> listSubtech;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setListSubtech(ArrayList<SubTechnologyData> listSubtech) {
        this.listSubtech = listSubtech;
    }

    public ArrayList<SubTechnologyData> getListSubtech() {
        return listSubtech;
    }
}
