package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.view.CommentView;

/**
 * Created by CanTran on 14/05/2017.
 */

public class CommentsAdapter extends BaseAdapter<Comment, CommentsAdapter.WorkHolder, LoadingHolder> {

    private final List<Comment> comments;
    private final Context context;
    private int commentType, posterId;

    public CommentsAdapter(Context context, List<Comment> comments) {
        super(context, comments);
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_comments;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public WorkHolder returnItemHolder(View view) {
        return new WorkHolder(view);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkHolder) {
            ((WorkHolder) holder).commentView.setCommentType(commentType);
            ((WorkHolder) holder).commentView.setPosterId(getPosterId());
            ((WorkHolder) holder).commentView.updateData(comments.get(position));
        }
    }

    class WorkHolder extends BaseHolder {
        public final CommentView commentView;


        public WorkHolder(View itemView) {
            super(itemView);
            commentView = itemView.findViewById(R.id.comments_view);
        }


    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

}
