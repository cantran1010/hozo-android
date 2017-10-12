package vn.tonish.hozo.model;

/**
 * Created by LongBui on 10/12/17.
 */

public class Payment {

    private int id;
    private boolean type;
    private int price;
    private String date;
    private String content;

    public Payment() {

    }

    public Payment(int id, boolean type, int price, String date, String content) {
        this.id = id;
        this.type = type;
        this.price = price;
        this.date = date;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", type=" + type +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
