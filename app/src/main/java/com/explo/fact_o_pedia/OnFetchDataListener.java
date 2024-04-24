package com.explo.fact_o_pedia;

import com.explo.fact_o_pedia.Models.Claims;

import java.util.List;

public interface OnFetchDataListener<FactApiResponse> {
    void onFetchData(List<Claims> claims, String message);
    void onError(String message);
}
