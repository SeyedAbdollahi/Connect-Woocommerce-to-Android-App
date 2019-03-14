package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Item implements Serializable {

    String name;
    @SerializedName("product_id")
    String productId;
    String quantity;
    String price;
    @SerializedName("total")
    String totalPrice;
    String image;
    String virtual;

    public Item() {
    }

    public String getName() {
        return name;
    }

    public String getProductId() {
        return productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getPrice() {
        return price;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getImage() {
        return image;
    }

    public String getVirtual() {
        return virtual;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setPrice(String price) {
        if (price.equals("")){
            price = "0";
        }
        this.price = price;
    }

    public void setTotalPrice(String totalPrice) {
        if (totalPrice.equals("")){
            totalPrice = "0";
        }
        this.totalPrice = totalPrice;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setVirtual(String virtual) {
        this.virtual = virtual;
    }
}

