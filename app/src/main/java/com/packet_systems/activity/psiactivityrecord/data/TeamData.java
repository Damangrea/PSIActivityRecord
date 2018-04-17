package com.packet_systems.activity.psiactivityrecord.data;

import java.io.Serializable;

/**
 * Created by damangrea on 05/04/18.
 */

public class TeamData implements Serializable {
    private String id;
    private String team_sort_name;
    private String team_name;
    private String team_leader_id;

    public String getId() {
        return id;
    }

    public String getTeam_leader_id() {
        return team_leader_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public String getTeam_sort_name() {
        return team_sort_name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTeam_leader_id(String team_leader_id) {
        this.team_leader_id = team_leader_id;
    }

    public void setTeam_name(String team_name) {
        this.team_name = team_name;
    }

    public void setTeam_sort_name(String team_sort_name) {
        this.team_sort_name = team_sort_name;
    }
}
