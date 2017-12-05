package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.ReportDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/21/2017.
 */

public class CommentBigView extends LinearLayout implements View.OnClickListener {
    private static final String TAG = CommentBigView.class.getSimpleName();
    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvComment, tvTimeAgo, tvAnswer, tvPoster;
    private ImageView imgAttach;
    private ImageView imgSetting;
    private Comment comment;
    private int commentType;
    private int commentCountVisibility;
    private int posterId;

    public interface AnswerListener {
        void onAnswer();
    }

    private AnswerListener answerListener;

    public AnswerListener getAnswerListener() {
        return answerListener;
    }

    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    public CommentBigView(Context context) {
        super(context);
        initView();
    }

    public CommentBigView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommentBigView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private CommentBigView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.view_big_comment, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);
        imgAvatar.setOnClickListener(this);

        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvComment = (TextViewHozo) findViewById(R.id.tv_comment);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        imgSetting = (ImageView) findViewById(R.id.img_setting);
        imgAttach = (ImageView) findViewById(R.id.img_attach_show);
        imgAttach.setOnClickListener(this);

        tvAnswer = findViewById(R.id.tv_answer);
        tvAnswer.setOnClickListener(this);

        imgSetting.setOnClickListener(this);
        tvPoster = findViewById(R.id.tv_poster);
    }

    public void updateData(final Comment comment) {
        LogUtils.d(TAG, "update Data comment : " + comment.toString());

        this.comment = comment;
        Utils.displayImageAvatar(getContext(), imgAvatar, comment.getAvatar());
        tvName.setText(comment.getFullName());
        tvComment.setText(comment.getBody());
        tvTimeAgo.setText(DateTimeUtils.getTimeAgo(comment.getCreatedAt(), getContext()));
        LogUtils.d(TAG, "update Data time ago : " + DateTimeUtils.getTimeAgo(comment.getCreatedAt(), getContext()));

        if (comment.getBody().equals("")) tvComment.setVisibility(View.GONE);

        if (comment.getImgAttach() != null && !comment.getImgAttach().trim().equals("") && !comment.getImgAttach().equals("null")) {
            imgAttach.setVisibility(View.VISIBLE);
            Utils.displayImage(getContext(), imgAttach, comment.getImgAttach());
        } else imgAttach.setVisibility(View.GONE);

        if (getCommentType() == View.VISIBLE) {
            imgSetting.setVisibility(View.VISIBLE);
            tvAnswer.setVisibility(View.VISIBLE);

            if (comment.getAuthorId() == UserManager.getMyUser().getId())
                imgSetting.setVisibility(View.GONE);
            else imgSetting.setVisibility(View.VISIBLE);

        } else {
            imgSetting.setVisibility(View.GONE);
            tvAnswer.setVisibility(View.GONE);
        }

        if (comment.getAuthorId() == getPosterId())
            tvPoster.setVisibility(VISIBLE);
        else
            tvPoster.setVisibility(GONE);

//        if (getCommentCountVisibility() == View.GONE) {
//            tvCommentCount.setVisibility(View.GONE);
//        } else {
//
//            if (comment.getRepliesCount() > 2) {
//                tvCommentCount.setText(getContext().getString(R.string.comment_count, comment.getRepliesCount() - 2));
//                tvCommentCount.setVisibility(View.VISIBLE);
//            } else
//                tvCommentCount.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.img_setting:
                showPopupMenu();
                break;

            case R.id.img_avatar:
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(Constants.USER_ID, comment.getAuthorId());
                intent.putExtra(Constants.IS_MY_USER, comment.getAuthorId() == UserManager.getMyUser().getId());
                getContext().startActivity(intent);
                break;

            case R.id.img_attach_show:
                Intent intentViewImage = new Intent(getContext(), PreviewImageActivity.class);
                intentViewImage.putExtra(Constants.EXTRA_IMAGE_PATH, comment.getImgAttach());
                getContext().startActivity(intentViewImage);
                break;

            case R.id.tv_answer:
//            case R.id.tv_comment_count:
                if (answerListener != null) answerListener.onAnswer();

//                Intent intentAnswer = new Intent(getContext(), CommentsAnswerActivity.class);
//                commentType = getContext().getString(R.string.comment_setting_visible);
//                intentAnswer.putExtra(Constants.TASK_ID_EXTRA, comment.getTaskId());
//                intentAnswer.putExtra(Constants.COMMENT_STATUS_EXTRA, commentType);
//                intentAnswer.putExtra(Constants.COMMENT_VISIBILITY, View.VISIBLE);
//                intentAnswer.putExtra(Constants.COMMENT_EXTRA, comment);
//                getContext().startActivity(intentAnswer);
                break;

        }
    }

    private void showPopupMenu() {
        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getContext(), imgSetting);

        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_comment, popup.getMenu());

//        if (TaskManager.getTaskById(comment.getTaskId()).getPoster().getId() == UserManager.getMyUser().getId())
//            popup.getMenu().findItem(R.id.answer).setVisible(true);
//        else
//            popup.getMenu().findItem(R.id.answer).setVisible(false);

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.report:
                        ReportDialog reportDialog = new ReportDialog(getContext(), comment);
                        reportDialog.showView();
                        break;

//                    case R.id.answer:
//                        Intent intentAnswer = new Intent();
//                        intentAnswer.setAction("MyBroadcast");
//                        intentAnswer.putExtra(Constants.COMMENT_EXTRA, comment);
//                        getContext().sendBroadcast(intentAnswer);
//                        break;

                }
                return true;
            }
        });
        popup.show();//showing popup menu
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public int getCommentCountVisibility() {
        return commentCountVisibility;
    }

    public void setCommentCountVisibility(int commentCountVisibility) {
        this.commentCountVisibility = commentCountVisibility;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

}
