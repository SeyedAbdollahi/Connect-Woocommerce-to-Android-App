package seyedabdollahi.ir.shop.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductsList implements Serializable {

    private List<Product> productList;

    public ProductsList() {
        productList = new ArrayList<Product>();
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }
}
