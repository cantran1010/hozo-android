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
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/19/2017.
 */

public class CommentTaskAdapter extends BaseAdapter<Comment, CommentTaskAdapter.WorkHolder, LoadingHolder> {

    private final List<Comment> comments;
    private final Context context;
    private int commentType, posterId;

    public CommentTaskAdapter(Context context, List<Comment> comments) {
        super(context, comments);
        this.context = context;
        this.comments = comments;
    }

    public interface AnswerListener {
        void onAnswer(int position);
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
            ((WorkHolder) holder).commentBigView.setPosterId(getPosterId());
            ((WorkHolder) holder).commentBigView.updateData(comments.get(position));
            ((WorkHolder) holder).commentBigView.setAnswerListener(new CommentBigView.AnswerListener() {
                @Override
                public void onAnswer() {
                    if (answerListener != null)
                        answerListener.onAnswer(holder.getAdapterPosition());
                }
            });
            CommentAdapter commentAdapter = new CommentAdapter((ArrayList<Comment>) comments.get(position).getComments());
            commentAdapter.setPosterId(getPosterId());
            commentAdapter.setCommentType(getCommentType());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
            ((WorkHolder) holder).recyclerView.setLayoutManager(layoutManager);
            commentAdapter.setCommentType(getCommentType());
            ((WorkHolder) holder).recyclerView.setAdapter(commentAdapter);

            if (comments.get(position).getRepliesCount() > 1) {
                ((WorkHolder) holder).tvCommentCount.setText(context.getString(R.string.see_all_comment, comments.get(position).getRepliesCount() - 1));
                ((WorkHolder) holder).tvCommentCount.setVisibility(View.VISIBLE);
            } else
                ((WorkHolder) holder).tvCommentCount.setVisibility(View.GONE);

            ((WorkHolder) holder).tvCommentCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (answerListener != null)
                        answerListener.onAnswer(holder.getAdapterPosition());
                }
            });

        }

    }

    class WorkHolder extends BaseHolder {
        public final CommentBigView commentBigView;
        public final RecyclerView recyclerView;
        public final TextViewHozo tvCommentCount;

        public WorkHolder(View itemView) {
            super(itemView);
            commentBigView = itemView.findViewById(R.id.comment_big_view);
            recyclerView = itemView.findViewById(R.id.rcv_comment);
            tvCommentCount = itemView.findViewById(R.id.tv_comment_count);
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