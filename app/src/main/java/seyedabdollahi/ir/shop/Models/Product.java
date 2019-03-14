package seyedabdollahi.ir.shop.Models;

import java.io.Serializable;
import java.util.List;

public class Product implements Serializable {

    private String id;
    private String name;
    private String status;
    private String price;
    private String virtual;
    private String downloadable;
    private List<Image> images;
    private String description;

    public Product() {
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getPrice() {
        return price;
    }

    public String getVirtual() {
        return virtual;
    }

    public String getDownloadable() {
        return downloadable;
    }

    public List<Image> getImages() {
        return images;
    }

    public String getDescription() {
        return description;
    }
}
