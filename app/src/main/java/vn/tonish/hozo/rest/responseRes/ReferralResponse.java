package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by CanTran on 2/7/18.
 */

public class ReferralResponse implements Serializable {
    @SerializedName("posting_bonus_amount")
    private String Price;
    @SerializedName("phone_number")
    private String phone;
    @SerializedName("referral_url")
    private String url;
    @SerializedName("referral_program_url")
    private String link;

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ReferralResponse{" +
                "Price='" + Price + '\'' +
                ", phone='" + phone + '\'' +
                ", link='" + link + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
