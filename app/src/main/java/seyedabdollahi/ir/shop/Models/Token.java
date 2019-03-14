package seyedabdollahi.ir.shop.Models;

import com.google.gson.annotations.SerializedName;

public class Token {

    @SerializedName("user_email")
    String email;

    public Token() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
