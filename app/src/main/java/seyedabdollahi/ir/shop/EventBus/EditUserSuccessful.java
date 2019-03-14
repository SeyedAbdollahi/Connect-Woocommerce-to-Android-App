package seyedabdollahi.ir.shop.EventBus;

import seyedabdollahi.ir.shop.Models.User;

public class EditUserSuccessful {

    User user;

    public EditUserSuccessful(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
