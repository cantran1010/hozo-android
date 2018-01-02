package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 11/3/17.
 */

public class WalletResponse {

    private int id;
    private String phone;
    @SerializedName("full_name")
    private String fullName;
    private String avatar;
    private int balance;
    @SerializedName("balance_account")
    private int balanceAccount;
    @SerializedName("balance_cash")
    private int balanceCash;
    @SerializedName("total_account_transactions")
    private int totalAccountTransactions;
    @SerializedName("total_cash_transactions")
    private int totalCashTransactions;

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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getBalanceAccount() {
        return balanceAccount;
    }

    public void setBalanceAccount(int balanceAccount) {
        this.balanceAccount = balanceAccount;
    }

    public int getBalanceCash() {
        return balanceCash;
    }

    public void setBalanceCash(int balanceCash) {
        this.balanceCash = balanceCash;
    }

    public int getTotalAccountTransactions() {
        return totalAccountTransactions;
    }

    public void setTotalAccountTransactions(int totalAccountTransactions) {
        this.totalAccountTransactions = totalAccountTransactions;
    }

    public int getTotalCashTransactions() {
        return totalCashTransactions;
    }

    public void setTotalCashTransactions(int totalCashTransactions) {
        this.totalCashTransactions = totalCashTransactions;
    }

    @Override
    public String toString() {
        return "WalletResponse{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", fullName='" + fullName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", balance=" + balance +
                ", balanceAccount=" + balanceAccount +
                ", balanceCash=" + balanceCash +
                ", totalAccountTransactions=" + totalAccountTransactions +
                ", totalCashTransactions=" + totalCashTransactions +
                '}';
    }

}
