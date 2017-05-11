package vn.tonish.hozo.rest.responseRes;

/**
 * Created by LongBui on 5/11/2017.
 */

public class Poster {

    private String fullName;
    private Double posterAverageRating;
    private Integer verify;
    private String avatar;
    private Integer id;

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

}
