package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

public class Shipping {

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    private String company;
    @SerializedName("address_1")
    private String address1;
    @SerializedName("address_2")
    private String address2;
    private String city;
    private String state;
    @SerializedName("postcode")
    private String postCode;
    private String country;

    public Shipping(String address1, String city, String state, String postCode) {
        this.address1 = address1;
        this.city = city;
        this.state = state;
        this.postCode = postCode;
        country = "IR";
    }
}
