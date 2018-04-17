package com.packet_systems.activity.psiactivityrecord.data;

import java.io.Serializable;

/**
 * Created by damangrea on 05/04/18.
 */

public class SubTechnologyData implements Serializable {
    private String id;
    private String name;
    private String tech_id;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTech_id() {
        return tech_id;
    }

    public void setTech_id(String tech_id) {
        this.tech_id = tech_id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }
}
