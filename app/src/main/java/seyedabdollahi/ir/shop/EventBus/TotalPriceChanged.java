package seyedabdollahi.ir.shop.EventBus;

public class TotalPriceChanged {

    private String totalPrice;

    public TotalPriceChanged(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
