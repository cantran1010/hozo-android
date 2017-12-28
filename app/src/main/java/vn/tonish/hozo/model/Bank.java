package vn.tonish.hozo.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 12/26/17.
 */

public class Bank implements Serializable{

    private int id;
    @SerializedName("bank_key")
    private String bankKey;
    @SerializedName("bank_name")
    private String bankName;
    @SerializedName("bank_trading_name")
    private String vnBankName;
    @SerializedName("name")
    private String receiver;
    @SerializedName("number")
    private String cardNumber;
    @SerializedName("branch")
    private String branchName;
    @SerializedName("created_at")
    private String createdAt;
    private boolean isSelected = false;

    public String getBankKey() {
        return bankKey;
    }

    public void setBankKey(String bankKey) {
        this.bankKey = bankKey;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getVnBankName() {
        return vnBankName;
    }

    public void setVnBankName(String vnBankName) {
        this.vnBankName = vnBankName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "id=" + id +
                ", bankKey='" + bankKey + '\'' +
                ", bankName='" + bankName + '\'' +
                ", vnBankName='" + vnBankName + '\'' +
                ", receiver='" + receiver + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", branchName='" + branchName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

}
