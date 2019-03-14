package seyedabdollahi.ir.shop.Models;

import java.util.ArrayList;
import java.util.List;

public class AddressList {

    private List<Address> addressList;

    public AddressList() {
        addressList = new ArrayList<Address>();
    }

    public List<Address> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<Address> addressList) {
        this.addressList = addressList;
    }
}
