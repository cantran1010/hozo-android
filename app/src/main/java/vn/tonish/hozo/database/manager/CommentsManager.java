package vn.tonish.hozo.database.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;
import vn.tonish.hozo.activity.CommentsActivity;
import vn.tonish.hozo.database.entity.CommentEntity;
import vn.tonish.hozo.utils.LogUtils;

import static vn.tonish.hozo.utils.DateTimeUtils.getDateFromStringIso;

/**
 * Created by CanTran on 5/17/17.
 */
public class CommentsManager {

    private static final String TAG = CommentsManager.class.getName();

    public static void insertComments(List<CommentEntity> comments) {
        LogUtils.d(TAG, "insertComments start ");
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        for (int i = 0; i < comments.size(); i++) {
            comments.get(i).setCreatedDateAt(getDateFromStringIso(comments.get(i).getCreatedAt()));
        }
        realm.insertOrUpdate(comments);
        realm.commitTransaction();
        realm.close();
    }

    public static List<CommentEntity> getCommentsSince(Date sinceDate, int taskId) {
        LogUtils.d(TAG, "getCommentsSince  sinceDate : " + sinceDate);
        List<CommentEntity> result = new ArrayList<>();
        Realm realm = Realm.getDefaultInstance();
        RealmQuery<CommentEntity> commentRealmQuery = realm.where(CommentEntity.class).equalTo("taskId", taskId);
        if (sinceDate != null)
            commentRealmQuery = commentRealmQuery.lessThan("createdDateAt", sinceDate);
        List<CommentEntity> comments = commentRealmQuery.findAll().sort("createdDateAt", Sort.DESCENDING);
        if (comments.size() > 0) {
            if (comments.size() >= CommentsActivity.LIMIT)
                result = comments.subList(0, CommentsActivity.LIMIT);
            else result = comments;
        }
        realm.close();
        return result;
    }

}