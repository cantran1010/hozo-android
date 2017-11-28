package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

/**
 * Created by LongBui on 11/3/17.
 */

public class TransactionResponse {
    private int id;
    private int balance;
    private int amount;
    private String type;
    private String provider;
    private String method;
    private String promotion;
    private String status;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("task_id")
    private int taskId;
    @SerializedName("task_title")
    private String taskName;
    @SerializedName("user_id")
    private int userId;
    @SerializedName("user_full_name")
    private String fullName;
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "id=" + id +
                ", balance=" + balance +
                ", amount=" + amount +
                ", type='" + type + '\'' +
                ", provider='" + provider + '\'' +
                ", method='" + method + '\'' +
                ", promotion='" + promotion + '\'' +
                ", status='" + status + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", taskId=" + taskId +
                ", taskName='" + taskName + '\'' +
                ", userId=" + userId +
                ", fullName='" + fullName + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
