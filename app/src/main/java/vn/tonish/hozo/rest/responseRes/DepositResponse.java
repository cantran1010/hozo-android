package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 11/16/17.
 */

public class DepositResponse {

    @SerializedName("payment_url")
    private String paymentUrl;

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    @Override
    public String toString() {
        return "DepositResponse{" +
                "paymentUrl='" + paymentUrl + '\'' +
                '}';
    }

}
