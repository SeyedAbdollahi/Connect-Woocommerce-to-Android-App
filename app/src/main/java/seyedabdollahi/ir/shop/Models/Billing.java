package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

public class Billing {

    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("address_1")
    private String address1;
    @SerializedName("address_2")
    private String address2;
    private String city;
    private String state;
    @SerializedName("postcode")
    private String postCode;
    private String country;
    private String email;
    private String phone;

    public Billing(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        country = "IR";
    }


}
