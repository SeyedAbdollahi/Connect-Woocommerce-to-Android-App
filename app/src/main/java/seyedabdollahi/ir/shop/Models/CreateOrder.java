package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateOrder {

    @SerializedName("set_paid")
    private boolean setPaid;
    private Billing billing;
    @SerializedName("customer_id")
    private String customerId;
    private Shipping shipping;
    @SerializedName("line_items")
    private List<Item> itemsList;

    public CreateOrder(Billing billing, String customerId, Shipping shipping, List<Item> itemsList) {
        this.billing = billing;
        this.customerId = customerId;
        this.shipping = shipping;
        this.itemsList = itemsList;
        setPaid = true;
    }
}
