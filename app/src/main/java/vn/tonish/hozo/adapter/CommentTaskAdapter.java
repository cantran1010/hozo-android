package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.view.CommentBigView;

/**
 * Created by LongBui on 4/19/2017.
 */

public class CommentTaskAdapter extends BaseAdapter<Comment, CommentTaskAdapter.WorkHolder, LoadingHolder> {

    private final List<Comment> comments;
    private final Context context;
    private int commentType;

    public CommentTaskAdapter(Context context, List<Comment> comments) {
        super(context, comments);
        this.context = context;
        this.comments = comments;
    }

    public interface AnswerListener {
        public void onAnswer(int position);
    }

    private AnswerListener answerListener;

    public AnswerListener getAnswerListener() {
        return answerListener;
    }

    public void setAnswerListener(AnswerListener answerListener) {
        this.answerListener = answerListener;
    }

    @Override
    public int getItemLayout() {
        return R.layout.item_comment_task;
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkHolder) {
            ((WorkHolder) holder).commentBigView.setCommentType(getCommentType());
            ((WorkHolder) holder).commentBigView.updateData(comments.get(position));
            ((WorkHolder) holder).commentBigView.setAnswerListener(new CommentBigView.AnswerListener() {
                @Override
                public void onAnswer() {
                    if (answerListener != null)
                        answerListener.onAnswer(holder.getAdapterPosition());
                }
            });
            CommentAdapter commentAdapter = new CommentAdapter((ArrayList<Comment>) comments.get(position).getComments());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            ((WorkHolder) holder).recyclerView.setLayoutManager(layoutManager);
//        commentAdapter.setCommentType(getCommentType());
            ((WorkHolder) holder).recyclerView.setAdapter(commentAdapter);
        }
    }

    class WorkHolder extends BaseHolder {
        public final CommentBigView commentBigView;
        public final RecyclerView recyclerView;

        public WorkHolder(View itemView) {
            super(itemView);
            commentBigView = itemView.findViewById(R.id.comment_big_view);
            recyclerView = itemView.findViewById(R.id.rcv_comment);
        }

    }

    public int getCommentType() {
        return commentType;
    }

    public void setCommentType(int commentType) {
        this.commentType = commentType;
    }
}