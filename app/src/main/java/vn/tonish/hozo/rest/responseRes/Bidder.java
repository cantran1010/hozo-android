package vn.tonish.hozo.rest.responseRes;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Bidder {
    private String fullName;
    private Double posterAverageRating;
    private Integer verify;
    private String avatar;
    private Integer id;
    private String bidedAt;
    private String phone;

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Double getPosterAverageRating() {
        return posterAverageRating;
    }

    public void setPosterAverageRating(Double posterAverageRating) {
        this.posterAverageRating = posterAverageRating;
    }

    public Integer getVerify() {
        return verify;
    }

    public void setVerify(Integer verify) {
        this.verify = verify;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getBidedAt() {
        return bidedAt;
    }

    public void setBidedAt(String bidedAt) {
        this.bidedAt = bidedAt;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Bidder{" +
                "fullName='" + fullName + '\'' +
                ", posterAverageRating=" + posterAverageRating +
                ", verify=" + verify +
                ", avatar='" + avatar + '\'' +
                ", id=" + id +
                ", bidedAt='" + bidedAt + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
