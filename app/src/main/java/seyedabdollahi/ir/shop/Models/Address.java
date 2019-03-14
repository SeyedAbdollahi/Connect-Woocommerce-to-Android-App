package seyedabdollahi.ir.shop.Models;

public class Address {

    String state;
    private String city;
    private String postCode;
    private String address;
    private boolean checked;

    public Address(String state, String city, String postCode, String address, boolean checked) {
        this.state = state;
        this.city = city;
        this.postCode = postCode;
        this.address = address;
        this.checked = checked;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
