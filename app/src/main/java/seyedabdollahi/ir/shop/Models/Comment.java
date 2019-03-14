package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comment implements Serializable {

    private String id;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("reviewer")
    private String name;
    @SerializedName("date_created")
    private String date;
    @SerializedName("reviewer_email")
    private String email;
    @SerializedName("review")
    private String text;

    public Comment() {
    }

    public String getId() {
        return id;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setText(String text) {
        this.text = text;
    }
}
