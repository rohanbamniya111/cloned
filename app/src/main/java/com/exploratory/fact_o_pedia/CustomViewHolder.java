package com.exploratory.fact_o_pedia;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class CustomViewHolder extends RecyclerView.ViewHolder {
    TextView text_title, claim_title, text_claimant, claim_publisher, claim_rating, claim_date;
    CardView cardView;
    ImageButton imageButton;
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        text_title = itemView.findViewById(R.id.text_title);
        claim_title = itemView.findViewById(R.id.claim_title);
        text_claimant = itemView.findViewById(R.id.text_claimant);
        claim_publisher = itemView.findViewById(R.id.claim_publisher);
        claim_rating = itemView.findViewById(R.id.claim_rating);
        claim_date = itemView.findViewById(R.id.claim_date);
        cardView = itemView.findViewById(R.id.click);
        imageButton = itemView.findViewById(R.id.imageButton);
    }
}
