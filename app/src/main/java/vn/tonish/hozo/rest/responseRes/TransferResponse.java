package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 1/11/18.
 */

public class TransferResponse {

    private long id;
    private String phone;
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("balance_account")
    private long balanceAccount;
    @SerializedName("balance_cash")
    private long balanceCash;
    private String avatar;
    @SerializedName("total_account_transactions")
    private long totalAccountTransactions;
    @SerializedName("total_cash_transactions")
    private long totalCashTransactions;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(long balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public long getBalanceCash() {
        return balanceCash;
    }

    public void setBalanceCash(long balanceCash) {
        this.balanceCash = balanceCash;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getTotalAccountTransactions() {
        return totalAccountTransactions;
    }

    public void setTotalAccountTransactions(long totalAccountTransactions) {
        this.totalAccountTransactions = totalAccountTransactions;
    }

    public long getTotalCashTransactions() {
        return totalCashTransactions;
    }

    public void setTotalCashTransactions(long totalCashTransactions) {
        this.totalCashTransactions = totalCashTransactions;
    }

    @Override
    public String toString() {
        return "TransferResponse{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", balanceAccount=" + balanceAccount +
                ", balanceCash=" + balanceCash +
                ", avatar='" + avatar + '\'' +
                ", totalAccountTransactions=" + totalAccountTransactions +
                ", totalCashTransactions=" + totalCashTransactions +
                '}';
    }

}
