package com.exploratory.fact_o_pedia;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.exploratory.fact_o_pedia.Models.Claims;

import java.util.List;

public class CustomAdaptor extends RecyclerView.Adapter<CustomViewHolder> {
    private Context context;
    private List<Claims> claims;
    private SelectListener listener;

    public CustomAdaptor(Context context, List<Claims> claims, SelectListener listener) {
        this.context = context;
        this.claims = claims;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomViewHolder(LayoutInflater.from(context).inflate(R.layout.fact_list_items, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.text_title.setText(claims.get(position).getText());
        holder.claim_title.setText(claims.get(position).getClaimReview().get(0).getTitle());
        holder.text_claimant.setText("Claim by "+(claims.get(position).getClaimant())+":");
        holder.claim_publisher.setText("- "+(claims.get(position).getClaimReview().get(0).getPublisher().getName()));
        holder.claim_rating.setText(claims.get(position).getClaimReview().get(0).getTextualRating());
        holder.claim_date.setText(claims.get(position).getClaimDate());

        switch (claims.get(position).getClaimReview().get(0).getTextualRating().toLowerCase())
        {
            default: holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.blue));break;
            case "pants on fire" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "extremely misleading" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "misleading" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "misattributed" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "miscaptioned" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "false" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "mostly false" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "fake" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "scam" : holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.wrong));break;
            case "mostly true" :holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.right));break;
            case "true" :holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.right));break;
            case "correct" :holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.right));break;
            case "half true" :holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.half));break;
            case "mixture" :holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.half));break;
            case "unsupported":holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.unsupported));break;
            case "no evidence":holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.unsupported));break;
            case "unproven":holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.unsupported));break;
            case "imaginary":holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.unsupported));break;
            case "outdated":holder.claim_rating.setTextColor(ContextCompat.getColor(context, R.color.unsupported));break;
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnClaimClicked(claims.get(position));
            }
        });

        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.OnButtonClicked(claims.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return claims.size();
    }
}
