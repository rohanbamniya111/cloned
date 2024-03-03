package com.exploratory.fact_o_pedia.Models;
import java.io.Serializable;
import java.util.List;

public class UpdatesItems {
    String title="";
    String imgLink="";
    String factChecker="";
    String time="";
    String category="";
    String link="";
    public UpdatesItems(String title,String imgLink,String factChecker,String time,String category,String link){
        this.title = title;
        this.imgLink = imgLink;
        this.factChecker = factChecker;
        this.time = time;
        this.category = category;
        this.link = link;
    }
    public String getTitle() {return title;}
    public String getImgLink() {return imgLink;}
    public String getFactChecker() {return factChecker;}
    public String getTime() {return time;}
    public String getCategory() {return category;}
    public String getLink() {return link;}

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImgLink(String imgLink) {
        this.imgLink = imgLink;
    }

    public void setFactChecker(String factChecker) {
        this.factChecker = factChecker;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
