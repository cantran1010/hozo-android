package vn.tonish.hozo.database.entity;

import io.realm.RealmObject;

/**
 * Created by CanTran on 9/23/17.
 */

public class RealmDouble extends RealmObject {
    private double val;

    public RealmDouble() {

    }

    public double getVal() {
        return val;
    }

    public void setVal(double val) {
        this.val = val;
    }
}
