package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by CanTran on 9/23/17.
 */

public class RealmInt extends RealmObject {
    private Integer val;

    public RealmInt() {

    }

    public Integer getVal() {
        return val;
    }

    public void setVal(Integer val) {
        this.val = val;
    }
}
