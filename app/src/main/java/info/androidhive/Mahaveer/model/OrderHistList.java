package info.androidhive.Mahaveer.model;

import java.util.ArrayList;

/**
 * Created by Splash New on 4/8/2015.
 */
public class OrderHistList {


    private String title, thumbnailUrl;
    private String year;
    private String rating;
    private String product_id;
    private ArrayList<String> genre;

    public OrderHistList() {
    }

    public OrderHistList(String name, String thumbnailUrl, String year, String rating, String product_id,
                    ArrayList<String> genre) {
        this.product_id=product_id;
        this.title = name;
        this.thumbnailUrl = thumbnailUrl;
        this.year = year;
        this.rating = rating;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String name) {
        this.title = name;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String pid) {
        this.product_id = pid;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }

    public void setGenre(ArrayList<String> genre) {
        this.genre = genre;
    }
}
