package com.explo.fact_o_pedia.Models;

public class Comments {
    String name, comment,checked;

    public Comments() {
    }

    public Comments(String name, String comment,String checked) {
        this.name = name;
        this.comment = comment;
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getChecked() { return checked; }

    public void setChecked(String checked) { this.checked = checked; }
}