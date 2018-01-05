package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import vn.tonish.hozo.model.Bank;

/**
 * Created by CanTran on 5/10/17.
 */

public class WithdrawMoneyReponse {

    private int id;
    private int amount;
    private String status;
    @SerializedName("completed_at")
    private String completedAt;
    @SerializedName("created_at")
    private String createdAt;
    private Bank bank;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    @Override
    public String toString() {
        return "WithdrawMoneyReponse{" +
                "id=" + id +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", completedAt='" + completedAt + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", bank=" + bank +
                '}';
    }

}
