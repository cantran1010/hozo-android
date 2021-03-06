package vn.tonish.hozo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.view.CommentView;

/**
 * Created by LongBui on 4/21/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {


    private final ArrayList<Comment> comments;
    private int commentType, posterId;

    public CommentAdapter(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.commentView.setCommentType(commentType);
        holder.commentView.setPosterId(getPosterId());
        holder.commentView.updateData(comments.get(position));
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class MyViewHolder extends BaseHolder {
        public final CommentView commentView;

        public MyViewHolder(View itemView) {
            super(itemView);
            commentView = itemView.findViewById(R.id.comment_view);
        }
    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

}