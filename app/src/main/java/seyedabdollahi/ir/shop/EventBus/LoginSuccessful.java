package seyedabdollahi.ir.shop.EventBus;

import seyedabdollahi.ir.shop.Models.User;

public class LoginSuccessful {

    User user;

    public LoginSuccessful(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
