package seyedabdollahi.ir.shop.EventBus;

public class SendComment {

    private String productId;
    private String name;
    private String email;
    private String review;

    public SendComment(String productId, String name, String email, String review) {
        this.productId = productId;
        this.name = name;
        this.email = email;
        this.review = review;
    }

    public String getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getReview() {
        return review;
    }
}
