package com.exploratory.fact_o_pedia.Models;

import java.io.Serializable;

public class Publisher implements Serializable {
    String name = "";
    String site = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
