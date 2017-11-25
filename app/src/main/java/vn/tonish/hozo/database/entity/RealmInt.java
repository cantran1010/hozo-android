package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by CanTran on 9/23/17.
 */

public class RealmInt extends RealmObject {
    private int val;

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

}
