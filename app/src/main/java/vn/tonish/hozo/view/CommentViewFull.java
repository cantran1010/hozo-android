package vn.tonish.hozo.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.CommentAdapter;
import vn.tonish.hozo.model.Comment;

/**
 * Created by LongBui on 4/21/2017.
 */

public class CommentViewFull extends RelativeLayout {

    private RecyclerView rcvComment;

    public CommentViewFull(Context context) {
        super(context);
        init();
    }

    public CommentViewFull(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CommentViewFull(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CommentViewFull(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.comment_view_full, this, true);

        rcvComment = (RecyclerView) findViewById(R.id.rcv_comment);
    }

    public void updateData(ArrayList<Comment> comments) {
        CommentAdapter commentAdapter = new CommentAdapter(comments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rcvComment.setLayoutManager(layoutManager);
        rcvComment.setAdapter(commentAdapter);
    }
}
