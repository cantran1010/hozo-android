package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 11/3/17.
 */

public class PromotionResponse {

    private int id;
    private String phone;
    @SerializedName("full_name")
    private String fullName;
    private int balance;
    @SerializedName("transaction")
    private TransactionResponse transactionResponse;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public TransactionResponse getTransactionResponse() {
        return transactionResponse;
    }

    public void setTransactionResponse(TransactionResponse transactionResponse) {
        this.transactionResponse = transactionResponse;
    }

    @Override
    public String toString() {
        return "PromotionResponse{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", balance=" + balance +
                ", transactionResponse=" + transactionResponse +
                '}';
    }

}
