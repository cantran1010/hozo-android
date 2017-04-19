package vn.tonish.hozo.model;

/**
 * Created by ADMIN on 4/19/2017.
 */

public class Album {
    private long id;
    private String name;
    private String coverPath;

    public Album() {

    }

    public Album(long id, String name, String coverPath) {
        this.id = id;
        this.name = name;
        this.coverPath = coverPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }
}
