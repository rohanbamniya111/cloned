package com.exploratory.fact_o_pedia;

import com.exploratory.fact_o_pedia.Models.Claims;

public interface SelectListener {
    void OnClaimClicked(Claims claims);

    void OnButtonClicked(Claims claims);
}
