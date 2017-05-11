package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;
import vn.tonish.hozo.dialog.ReportDialog;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.utils.Utils;

/**
 * Created by LongBui on 4/21/2017.
 */

public class CommentView extends LinearLayout implements View.OnClickListener {

    private CircleImageView imgAvatar;
    private TextViewHozo tvName, tvComment, tvTimeAgo;
    private ImageView imgAttach;

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
        ImageView imgSetting = (ImageView) findViewById(R.id.img_setting);
        imgAttach = (ImageView) findViewById(R.id.img_attach);

        imgSetting.setOnClickListener(this);
    }

    public void updateData(Comment comment) {
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
                ReportDialog reportDialog = new ReportDialog(getContext());
                reportDialog.showView();
                break;
        }
    }
}
