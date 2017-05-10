package vn.tonish.hozo.rest.responseRes;

import vn.tonish.hozo.model.User;

/**
 * Created by tonish1 on 5/10/17.
 */

public class OtpReponse {

    private Token token;
    private User user;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "OtpReponse{" +
                "token=" + token +
                ", user=" + user +
                '}';
    }
}
