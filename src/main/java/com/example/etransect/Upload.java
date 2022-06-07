package com.example.etransect;

import com.google.firebase.database.Exclude;

public class Upload {

    private String mName,mImage_url,mkey,mdescription,mprice,mCategory,seller;
    private int likes ;
    public  Upload(){
        //empty constructure
    }

    public  Upload(String name,String url,String description,String price,String Category,String seller_){
        if(name.trim().equals("")) {
            name = "No Name";
        }if(description.trim().equals("")) {
            description = "No Description";
        }if(price.trim().equals("")) {
            price = "R0";
        }if(Category.trim().equals("")) {
            Category = "No Name";
        }if(seller_.trim().equals("")) {
            seller_ = "No Name";
        }

        mName = name;
        mdescription= description;
        mprice = price;
        mCategory = Category;
        mImage_url = url;
        this.likes = 0;
        seller = seller_;
    }

    public String getmName(){
        return mName;
    }
    public String getSeller(){ return seller; }
    public String getMdescription(){ return mdescription; }
    public String getMprice(){ return mprice; }
    public String getmCatagory(){ return mCategory; }
    public int getLikes(){ return likes; }
    public void setmImage_url(String url){
        mImage_url = url;
    }
    public String getmImage_url(){
        return mImage_url;
    }
    public void setmName(String name){
        mName = name;
    }
    public void setSeller(String seller_){ seller = seller_;}
    @Exclude
    public String getKey(){
        return mkey;
    }
    @Exclude
    public  void  setMkey(String mkey){
        this.mkey = mkey;
    }
}
