package com.jpardogo.android.listbuddies.models;

import java.io.Serializable;

/**
 * Created by creditcloud on 8/18/16.
 */
public class ImageBean implements Serializable{
    String url;

    String comment;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString(){
        return "url:"+url+",comment:"+comment;
    }
    public ImageBean(String url,String comment){
        this.url = url;
        this.comment = comment;
    }
}
