package seyedabdollahi.ir.shop.EventBus;

import seyedabdollahi.ir.shop.Models.Address;

public class ChangeAddressList {

    private String Action;
    private Address address;
    private int position;

    public ChangeAddressList(String action, Address address, int position) {
        Action = action;
        this.address = address;
        this.position = position;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
