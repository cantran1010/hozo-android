package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Comment;
import vn.tonish.hozo.view.CommentView;

/**
 * Created by CanTran on 5/16/17.
 */

public class CommentAdapter extends BaseAdapter<Comment, CommentAdapter.MyViewHolder, LoadingHolder> {


    private ArrayList<Comment> comments;


    public CommentAdapter(Context context, ArrayList<Comment> comments) {
        super(context, comments);
        this.comments = comments;
    }


    @Override
    public int getItemLayout() {
        return R.layout.item_freebackview;
    }

    @Override
    public int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    public MyViewHolder returnItemHolder(View view) {
        return new MyViewHolder(view);
    }

    @Override
    public LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder holder_ = (MyViewHolder) holder;
        holder_.commentView.updateData(comments.get(position));

    }

    @Override
    public int getItemCount() {
        return comments.size();
    }


    public class MyViewHolder extends BaseHolder {
        public final CommentView commentView;


        public MyViewHolder(View itemView) {
            super(itemView);
            commentView = (CommentView) itemView.findViewById(R.id.comment_view);
        }
    }
}