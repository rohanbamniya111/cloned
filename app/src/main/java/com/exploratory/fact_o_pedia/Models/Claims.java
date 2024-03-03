package com.exploratory.fact_o_pedia.Models;

import java.io.Serializable;
import java.util.List;

public class Claims implements Serializable {
    String text = "";
    String claimant = "";
    String claimDate = "";
    List<ClaimReview> claimReview;

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public String getClaimant() {
        return claimant;
    }

    public void setClaimant(String claimant) {
        this.claimant = claimant;
    }

    public List<ClaimReview> getClaimReview() {
        return claimReview;
    }

    public void setClaimReview(List<ClaimReview> claimReview) {
        this.claimReview = claimReview;
    }

    public String getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(String claimDate) {
        this.claimDate = claimDate;
    }

    public static class ClaimReview implements Serializable{
        Publisher publisher = null;
        String url = "";
        String title = "";
        String textualRating = "";

        public Publisher getPublisher() {
            return publisher;
        }

        public void setPublisher(Publisher publisher) {
            this.publisher = publisher;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTextualRating() {
            return textualRating;
        }

        public void setTextualRating(String textualRating) {
            this.textualRating = textualRating;
        }
    }
}
