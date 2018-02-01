package vn.tonish.hozo.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.activity.profile.ProfileActivity;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.model.Member;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.utils.DateTimeUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PxUtils;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/21/2017.
 */

public class MessageAdapter extends BaseAdapter<Message, MessageAdapter.WorkHolder, LoadingHolder> {

    private static final String TAG = MessageAdapter.class.getSimpleName();
    private final List<Message> messages;
    private final Context context;
    @SuppressLint("UseSparseArrays")
    private final HashMap<Integer, Member> memberHashMap = new HashMap<>();
    private int posterId;

    private final DatabaseReference memberCloudEndPoint;

    public MessageAdapter(Context context, List<Message> messages, int posterId) {
        super(context, messages);
        this.messages = messages;
        this.context = context;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        memberCloudEndPoint = myRef.child("members");
    }

    @Override
    protected int getItemLayout() {
        return R.layout.item_message;
    }

    @Override
    protected int getLoadingLayout() {
        return R.layout.bottom_loading;
    }

    @Override
    protected MessageAdapter.WorkHolder returnItemHolder(View view) {
        return new WorkHolder(view);
    }

    @Override
    protected LoadingHolder returnLoadingHolder(View view) {
        return new LoadingHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof WorkHolder) {

            final Message message = messages.get(position);
            final WorkHolder workHolder = (WorkHolder) holder;
            LogUtils.d(TAG, "onBindViewHolder , position : " + message.toString());
            if (message.getUser_id() == UserManager.getMyUser().getId()) {
                workHolder.rightLayout.setVisibility(View.VISIBLE);
                workHolder.leftLayout.setVisibility(View.GONE);
                workHolder.tvRightMsg.setText(message.getMessage());
                if (message.getType() == 1) {
                    workHolder.tvRightMsg.setVisibility(View.GONE);
                    workHolder.imgAttachRight.setVisibility(View.VISIBLE);
                    Utils.displayImageRounded(context, workHolder.imgAttachRight, message.getMessage(), (int) PxUtils.pxFromDp(context, 10), 0);
                } else {
                    workHolder.tvRightMsg.setVisibility(View.VISIBLE);
                    workHolder.imgAttachRight.setVisibility(View.GONE);
                    workHolder.tvRightMsg.setText(message.getMessage());
                }

                if (memberHashMap.containsKey(message.getUser_id())) {
                    if (memberHashMap.get(message.getUser_id()) != null && memberHashMap.get(message.getUser_id()).getAvatar() != null)
                        Utils.displayImageAvatar(context, workHolder.imgRightAvatar, memberHashMap.get(message.getUser_id()).getAvatar());
                    String[] names = memberHashMap.get(message.getUser_id()).getFull_name().split(" ");
                    workHolder.tvRightName.setText(names[names.length - 1]);
                } else {
                    DatabaseReference memberReference = memberCloudEndPoint.child(String.valueOf(message.getUser_id()));
                    memberReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "member addValueEventListener dataSnapshot toString : " + dataSnapshot.toString());
                            Member member = dataSnapshot.getValue(Member.class);
                            memberHashMap.put(message.getUser_id(), member);
                            if (memberHashMap.get(message.getUser_id()) != null && memberHashMap.get(message.getUser_id()).getAvatar() != null)
                                Utils.displayImageAvatar(context, workHolder.imgRightAvatar, memberHashMap.get(message.getUser_id()).getAvatar());
                            String[] names = memberHashMap.get(message.getUser_id()).getFull_name().split(" ");
                            workHolder.tvRightName.setText(names[names.length - 1]);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                workHolder.imgRightAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra(Constants.USER_ID, message.getUser_id());
                        intent.putExtra(Constants.IS_MY_USER, message.getUser_id() == UserManager.getMyUser().getId());
                        context.startActivity(intent);
                    }
                });

                if (message.getUser_id() == posterId)
                    workHolder.tvPosterRight.setVisibility(View.VISIBLE);
                else workHolder.tvPosterRight.setVisibility(View.GONE);

                workHolder.tvRightTime.setText(DateTimeUtils.getTimeChat(message.getCreated_atLong(true), context));

            } else {
                workHolder.leftLayout.setVisibility(View.VISIBLE);
                workHolder.rightLayout.setVisibility(View.GONE);
                if (message.getType() == 1) {
                    workHolder.tvLeftMsg.setVisibility(View.GONE);
                    workHolder.imgAttach.setVisibility(View.VISIBLE);
                    Utils.displayImageRounded(context, workHolder.imgAttach, message.getMessage(), (int) PxUtils.pxFromDp(context, 10), 0);
                } else {
                    workHolder.tvLeftMsg.setVisibility(View.VISIBLE);
                    workHolder.imgAttach.setVisibility(View.GONE);
                    workHolder.tvLeftMsg.setText(message.getMessage());
                }
                if (memberHashMap.containsKey(message.getUser_id())) {
                    if (memberHashMap.get(message.getUser_id()) != null && memberHashMap.get(message.getUser_id()).getAvatar() != null)
                        Utils.displayImageAvatar(context, workHolder.imgLeftAvatar, memberHashMap.get(message.getUser_id()).getAvatar());
                    String[] names = memberHashMap.get(message.getUser_id()).getFull_name().split(" ");
                    workHolder.tvLeftName.setText(names[names.length - 1]);
                } else {
                    DatabaseReference memberReference = memberCloudEndPoint.child(String.valueOf(message.getUser_id()));
                    memberReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d(TAG, "member addValueEventListener dataSnapshot toString : " + dataSnapshot.toString());

                            Member member = dataSnapshot.getValue(Member.class);
                            memberHashMap.put(message.getUser_id(), member);
                            if (memberHashMap.get(message.getUser_id()) != null && memberHashMap.get(message.getUser_id()).getAvatar() != null)
                                Utils.displayImageAvatar(context, workHolder.imgLeftAvatar, memberHashMap.get(message.getUser_id()).getAvatar());
                            String[] names = memberHashMap.get(message.getUser_id()).getFull_name().split(" ");
                            workHolder.tvLeftName.setText(names[names.length - 1]);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                workHolder.imgLeftAvatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, ProfileActivity.class);
                        intent.putExtra(Constants.USER_ID, message.getUser_id());
                        intent.putExtra(Constants.IS_MY_USER, message.getUser_id() == UserManager.getMyUser().getId());
                        context.startActivity(intent);
                    }
                });

                if (message.getUser_id() == posterId)
                    workHolder.tvPosterLeft.setVisibility(View.VISIBLE);
                else workHolder.tvPosterLeft.setVisibility(View.GONE);
                workHolder.tvLeftTime.setText(DateTimeUtils.getTimeChat(message.getCreated_atLong(true), context));

            }
            workHolder.imgAttach.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentViewImage = new Intent(context, PreviewImageActivity.class);
                    intentViewImage.putExtra(Constants.EXTRA_IMAGE_PATH, message.getMessage());
                    context.startActivity(intentViewImage);
                }
            });
            workHolder.imgAttachRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intentViewImage = new Intent(context, PreviewImageActivity.class);
                    intentViewImage.putExtra(Constants.EXTRA_IMAGE_PATH, message.getMessage());
                    context.startActivity(intentViewImage);
                }
            });
        }

    }

    public int getPosterId() {
        return posterId;
    }

    public void setPosterId(int posterId) {
        this.posterId = posterId;
    }

    public class WorkHolder extends BaseHolder {
        final CircleImageView imgLeftAvatar;
        final CircleImageView imgRightAvatar;
        final LinearLayout leftLayout;
        final LinearLayout rightLayout;
        //        final ImageView imgLeftBoss;
//        final ImageView imgRightBoss;
        final TextViewHozo tvLeftName;
        final TextViewHozo tvRightName;
        final TextViewHozo tvLeftMsg;
        final TextViewHozo tvRightMsg;
        final TextViewHozo tvLeftTime;
        final TextViewHozo tvRightTime;
        final TextViewHozo tvPosterLeft;
        final TextViewHozo tvPosterRight;
        final ImageView imgAttach;
        final ImageView imgAttachRight;


        public WorkHolder(View itemView) {
            super(itemView);

            imgLeftAvatar = itemView.findViewById(R.id.img_left_thumbnail);
            imgRightAvatar = itemView.findViewById(R.id.img_right_thumbnail);
            imgAttach = itemView.findViewById(R.id.img_attach_show);
            imgAttachRight = itemView.findViewById(R.id.img_attach_show_right);
            leftLayout = itemView.findViewById(R.id.left_layout);
            rightLayout = itemView.findViewById(R.id.right_layout);

//            imgLeftBoss = itemView.findViewById(R.id.img_left_boss);
//            imgRightBoss = itemView.findViewById(R.id.img_right_boss);

            tvLeftName = itemView.findViewById(R.id.tv_left_name);
            tvRightName = itemView.findViewById(R.id.tv_right_name);

            tvLeftMsg = itemView.findViewById(R.id.tv_left_msg);
            tvRightMsg = itemView.findViewById(R.id.tv_right_msg);

            tvLeftTime = itemView.findViewById(R.id.tv_left_time);
            tvRightTime = itemView.findViewById(R.id.tv_right_time);


            tvPosterLeft = itemView.findViewById(R.id.tv_poster_left);
            tvPosterRight = itemView.findViewById(R.id.tv_poster_right);
            if (tvLeftMsg != null) {
                tvLeftMsg.setMovementMethod(LinkMovementMethod.getInstance());
            }
            if (tvRightMsg != null) {
                tvRightMsg.setMovementMethod(LinkMovementMethod.getInstance());
            }

        }


    }
}