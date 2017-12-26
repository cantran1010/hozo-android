package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 12/26/17.
 */

public class BankResponse {
    private String key;
    private String name;
    @SerializedName("trading_name")
    private String tradingNamel;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTradingNamel() {
        return tradingNamel;
    }

    public void setTradingNamel(String tradingNamel) {
        this.tradingNamel = tradingNamel;
    }

    @Override
    public String toString() {
        return "BankResponse{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", tradingNamel='" + tradingNamel + '\'' +
                '}';
    }

}
