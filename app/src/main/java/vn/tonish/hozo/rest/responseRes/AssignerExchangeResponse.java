package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 12/26/17.
 */

public class AssignerExchangeResponse {

    private int id;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    private long price;
    private boolean isChecked = true;
    @SerializedName("wage_transferred")
    private boolean wageTransferred;

    public boolean isWageTransferred() {
        return wageTransferred;
    }

    public void setWageTransferred(boolean wageTransferred) {
        this.wageTransferred = wageTransferred;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "AssignerExchangeResponse{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", price=" + price +
                ", isChecked=" + isChecked +
                ", wageTransferred=" + wageTransferred +
                '}';
    }

}
