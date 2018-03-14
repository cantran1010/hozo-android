package vn.tonish.hozo.rest.responseRes;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by LongBui on 6/15/17.
 */

public class BlockResponse implements Serializable {

    @SerializedName("is_block")
    private boolean isBlock;
    @SerializedName("email_support")
    private String emailSupport;
    @SerializedName("hotline_phone")
    private String hotlinePhone;
    @SerializedName("link_support")
    private String linkSupport;
    private String reason;
    @SerializedName("block_expired")
    private String blockExpired;

    public boolean isBlock() {
        return isBlock;
    }

    public void setBlock(boolean block) {
        isBlock = block;
    }

    public String getBlockExpired() {
        return blockExpired;
    }

    public void setBlockExpired(String blockExpired) {
        this.blockExpired = blockExpired;
    }

    public String getEmailSupport() {
        return emailSupport;
    }

    public void setEmailSupport(String emailSupport) {
        this.emailSupport = emailSupport;
    }

    public String getHotlinePhone() {
        return hotlinePhone;
    }

    public void setHotlinePhone(String hotlinePhone) {
        this.hotlinePhone = hotlinePhone;
    }

    public boolean getIsBlock() {
        return isBlock;
    }

    public void setIsBlock(boolean isBlock) {
        this.isBlock = isBlock;
    }

    public String getLinkSupport() {
        return linkSupport;
    }

    public void setLinkSupport(String linkSupport) {
        this.linkSupport = linkSupport;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "BlockResponse{" +
                "isBlock=" + isBlock +
                ", emailSupport='" + emailSupport + '\'' +
                ", hotlinePhone='" + hotlinePhone + '\'' +
                ", linkSupport='" + linkSupport + '\'' +
                ", reason='" + reason + '\'' +
                ", blockExpired='" + blockExpired + '\'' +
                '}';
    }

}
