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
import vn.tonish.hozo.activity.ReportTaskActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/21/2017.
 */

public class CommentView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvComment, tvTimeAgo;
    private ImageView imgAttach;
    private ImageView imgSetting;
    private Comment comment;

    public CommentView(Context context) {
        super(context);
        initView();
    }

    public CommentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CommentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.comment_view, this, true);
        imgAvatar = (CircleImageView) findViewById(R.id.img_avatar);


        tvName = (TextViewHozo) findViewById(R.id.tv_name);
        tvComment = (TextViewHozo) findViewById(R.id.tv_comment);
        tvTimeAgo = (TextViewHozo) findViewById(R.id.tv_time_ago);
        imgSetting = (ImageView) findViewById(R.id.img_setting);
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        imgSetting.setOnClickListener(this);
    }

    public void updateData(Comment comment) {
        this.comment = comment;
        Utils.displayImageAvatar(getContext(), imgAvatar, comment.getAvatar());
        tvName.setText(comment.getFullName());
        tvComment.setText(comment.getBody());
        tvTimeAgo.setText(comment.getCreatedAt());
        Utils.displayImage(getContext(), imgAttach, comment.getImgAttach());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_setting:
//                ReportDialog reportDialog = new ReportDialog(getContext());
//                reportDialog.showView();
                showPopupMenu();
                break;
        }
    }

    private void showPopupMenu() {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(getContext(), imgSetting);

        if (comment.getAuthorId() == UserManager.getUserLogin(getContext()).getId()) {
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_comment, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {

                    switch (item.getItemId()) {

                        case R.id.report:
                            Intent intent = new Intent(getContext(), ReportTaskActivity.class);
                            intent.putExtra(Constants.COMMENT_EXTRA, comment);
                            getContext().startActivity(intent);
                            break;

                        case R.id.answer:
                            Intent intentAnswer = new Intent();
                            intentAnswer.setAction("MyBroadcast");
                            intentAnswer.putExtra(Constants.COMMENT_EXTRA, comment);
                            getContext().sendBroadcast(intentAnswer);
                            break;

                    }
                    return true;
                }
            });
        } else {
            //Inflating the Popup using xml file
            popup.getMenuInflater().inflate(R.menu.menu_report, popup.getMenu());
            //registering popup with OnMenuItemClickListener
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.report:
                            Intent intent = new Intent(getContext(), ReportTaskActivity.class);
                            intent.putExtra(Constants.COMMENT_EXTRA, comment);
                            getContext().startActivity(intent);
                            break;
                    }
                    return true;
                }
            });
        }
        popup.show();//showing popup menu
    }
}
