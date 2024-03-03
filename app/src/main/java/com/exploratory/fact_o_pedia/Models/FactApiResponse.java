package com.exploratory.fact_o_pedia.Models;

import java.io.Serializable;
import java.util.List;

public class FactApiResponse implements Serializable {
    List<Claims> claims;

    public List<Claims> getClaims() {
        return claims;
    }

    public void setClaims(List<Claims> claims) {
        this.claims = claims;
    }
}
