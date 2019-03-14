package seyedabdollahi.ir.shop.EventBus;

import java.util.List;

import seyedabdollahi.ir.shop.Models.Item;
import seyedabdollahi.ir.shop.Models.ItemsList;

public class DetailOrderClicked {

    ItemsList itemsList;
    String totalPrice;

    public DetailOrderClicked(ItemsList itemsList, String totalPrice) {
        this.itemsList = itemsList;
        this.totalPrice = totalPrice;
    }

    public ItemsList getItemsList() {
        return itemsList;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setItemsList(ItemsList itemsList) {
        this.itemsList = itemsList;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
