package seyedabdollahi.ir.shop.EventBus;

import seyedabdollahi.ir.shop.Models.Item;

public class DeleteItemClicked {

    Item item;

    public DeleteItemClicked(Item item) {
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
