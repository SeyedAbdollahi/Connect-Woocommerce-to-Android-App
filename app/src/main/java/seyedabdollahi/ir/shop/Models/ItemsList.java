package seyedabdollahi.ir.shop.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ItemsList implements Serializable {

    List<Item> itemList;

    public ItemsList() {
        itemList = new ArrayList<Item>();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }
}
