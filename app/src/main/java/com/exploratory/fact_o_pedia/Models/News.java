package com.exploratory.fact_o_pedia.Models;

import java.util.List;

public class News {
    List<Comments> comments;

    public News() {
    }

    public News(List<Comments> comments) {
        this.comments = comments;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public void setComments(List<Comments> comments) {
        this.comments = comments;
    }
}
