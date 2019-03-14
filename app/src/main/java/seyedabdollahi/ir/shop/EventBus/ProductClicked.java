package seyedabdollahi.ir.shop.EventBus;

import seyedabdollahi.ir.shop.Models.Product;

public class ProductClicked {

    Product product;

    public ProductClicked(Product product) {
        this.product = product;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
