package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.Sort;
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
        }
        realm.insertOrUpdate(comments);
        realm.commitTransaction();

    }

    public static List<Comment> getFirstPage(int taskId) {
        LogUtils.d(TAG, "getFirstPageComment start ");
        List<Comment> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<Comment> comments = realm.where(Comment.class).equalTo("taskId", taskId).findAll().sort("createdAt", Sort.DESCENDING);
        if (comments.size() > 0) {

            if (comments.size() >= CommentsActivity.LIMIT)
                result = comments.subList(0, CommentsActivity.LIMIT);
            else
                result = comments;

        }
        return result;
    }

    public static List<Comment> getCommentsSince(Date sinceDate, int taskId) {

        LogUtils.d(TAG, "getCommentsSince start ");
        List<Comment> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        List<Comment> comments = realm.where(Comment.class).equalTo("taskId", taskId).lessThan("craeatedDateAt", sinceDate).findAll().sort("createdAt", Sort.DESCENDING);
        if (comments.size() > 0) {

            if (comments.size() >= CommentsActivity.LIMIT)
                result = comments.subList(0, CommentsActivity.LIMIT);
            else result = comments;

        }
        return result;
    }

    public static void deleteAll() {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.where(Comment.class).findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

}