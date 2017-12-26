package vn.tonish.hozo.model;

import java.io.Serializable;

/**
 * Created by LongBui on 12/26/17.
 */

public class Bank implements Serializable{

    private int id;
    private String receiver;
    private String cardNumber;
    private String bankName;
    private String vnBankName;
    private String branchName;
    private boolean isSelected = false;

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
                ", receiver='" + receiver + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", bankName='" + bankName + '\'' +
                ", vnBankName='" + vnBankName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", isSelected=" + isSelected +
                '}';
    }

}
