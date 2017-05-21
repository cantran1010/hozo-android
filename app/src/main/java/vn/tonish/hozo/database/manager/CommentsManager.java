package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import vn.tonish.hozo.activity.CommentsActivity;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.utils.DateTimeUtils.getDateFromStringIso;

/**
 * Created by CanTran on 5/17/17.
 */
public class CommentsManager {

    private static final String TAG = CommentsManager.class.getName();

    public static void insertComments(List<Comment> comments) {
        LogUtils.d(TAG, "insertComments start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        for (int i = 0; i < comments.size(); i++) {
            comments.get(i).setCraeatedDateAt(getDateFromStringIso(comments.get(i).getCreatedAt()));
            realm.insertOrUpdate(comments.get(i));
        }
        realm.commitTransaction();

    }

    public static List<Comment> getAllComment() {
        LogUtils.d(TAG, "getAllComment start ");
        Realm realm = Realm.getDefaultInstance();
        return realm.where(Comment.class).findAll();
    }

    public static List<Comment> getFirstPage() {
        LogUtils.d(TAG, "getFirstPageComment start ");
        List<Comment> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<Comment> comments = realm.where(Comment.class).findAll().sort("createdAt");
        if (comments.size() > 0) {

            if (comments.size() >= CommentsActivity.LIMIT)
                result = comments.subList(0, CommentsActivity.LIMIT);
            else
                result = comments;

        }
        return result;
    }

    public static List<Comment> getCommentsSince(Date sinceDate) {

        LogUtils.d(TAG, "getCommentsSince start ");
        List<Comment> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<Comment> comments = realm.where(Comment.class).lessThan("craeatedDateAt", sinceDate).findAll().sort("createdAt");
        if (comments.size() > 0) {

            if (comments.size() >= CommentsActivity.LIMIT)
                result = comments.subList(0, CommentsActivity.LIMIT);
            else result = comments;

        }
        return result;
    }


}