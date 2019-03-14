package seyedabdollahi.ir.shop.Responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import seyedabdollahi.ir.shop.Models.Product;

public class ProductsResponse {

    @SerializedName("")
    private List<Product> array;

    public ProductsResponse() {
    }

    public List<Product> getArray() {
        return array;
    }

    public void setArray(List<Product> array) {
        this.array = array;
    }
}
