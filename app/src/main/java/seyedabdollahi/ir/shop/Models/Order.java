package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {

    String id;
    String status;
    @SerializedName("total")
    String totalPrice;
    @SerializedName("customer_id")
    String customerId;
    @SerializedName("date_modified")
    String date;
    @SerializedName("payment_method_title")
    String paymentName;
    @SerializedName("line_items")
    List<Item> items;

    public Order() {
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getCustomerId() {
        return customerId;
    }

    public String getDate() {
        return date;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
