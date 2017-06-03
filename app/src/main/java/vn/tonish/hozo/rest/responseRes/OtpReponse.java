package vn.tonish.hozo.rest.responseRes;

import vn.tonish.hozo.database.entity.UserEntity;

/**
 * Created by CanTran on 5/10/17.
 */

public class OtpReponse {

    private Token token;
    private UserEntity user;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
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
