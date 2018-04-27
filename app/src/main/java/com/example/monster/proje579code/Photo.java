package com.example.monster.proje579code;

/**
 * Created by usama on 4/15/18.
 */

public class Photo {


    //private final int imageResource;
    private boolean isFavorite = false;
    private final String imageUrl;

    public Photo(String imageUrl) {
    //public Photo(int name, int author, int imageResource, String imageUrl) {
        //this.imageResource = imageResource;
        this.imageUrl = imageUrl;
    }



    //public int getImageResource() {
    //    return imageResource;
    //}

    public boolean getIsFavorite() {
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void toggleFavorite() {
        isFavorite = !isFavorite;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}