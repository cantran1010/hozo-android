package vn.tonish.hozo.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.image.AlbumActivity;
import vn.tonish.hozo.activity.image.PreviewImageActivity;
import vn.tonish.hozo.activity.task_detail.DetailTaskActivity;
import vn.tonish.hozo.adapter.MessageAdapter;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.database.manager.UserManager;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.dialog.PickImageDialog;
import vn.tonish.hozo.model.Image;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.rest.ApiClient;
import vn.tonish.hozo.rest.responseRes.ImageResponse;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;
import vn.tonish.hozo.utils.FileUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.PreferUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 9/18/17.
 */

public class ChatActivity extends BaseTouchActivity implements View.OnClickListener {
    private static final String TAG = ChatActivity.class.getSimpleName();
    private final String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private RecyclerView rcvMessage;
    private MessageAdapter messageAdapter;
    private EdittextHozo edtMsg;
    private int taskId;
    private final List<Message> messages = new ArrayList<>();
    private boolean isLoadingMoreFromServer = true;
    private String lastKeyMsg;
    private int posterId;
    private DatabaseReference messageCloudEndPoint;
    private TextViewHozo tvTitle, tvMember;
    private boolean isLoading = false;
    private static final int PAGE_COUNT = 11;
    private ValueEventListener valueEventListener;
    private ChildEventListener childEventListener;
    private ChildEventListener memberEventListener;
    private ChildEventListener groupTaskListener;
    private Query recentPostsQuery = null;
    private ImageView imgMenu;
    private DatabaseReference memberCloudEndPoint;
    private DatabaseReference groupTaskReference;
    private TaskResponse taskResponse;
    private RelativeLayout mainLayout;
    private String imgPath = null;
    private File fileAttach;

    @Override
    protected int getLayout() {
        return R.layout.chat_activity;
    }

    @Override
    protected void initView() {
        rcvMessage = (RecyclerView) findViewById(R.id.rcv_msg);
        ImageView btnSend = (ImageView) findViewById(R.id.img_send);
        edtMsg = (EdittextHozo) findViewById(R.id.edt_comment);
        ImageView imgBack = (ImageView) findViewById(R.id.img_back);
        tvTitle = (TextViewHozo) findViewById(R.id.tv_title);
        tvTitle.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        imgMenu = (ImageView) findViewById(R.id.img_menu);
        imgMenu.setOnClickListener(this);
        edtMsg.setHint(getString(R.string.chat_hint));
        ImageView imgAttach = (ImageView) findViewById(R.id.img_attach);
        imgAttach.setOnClickListener(this);
        tvMember = (TextViewHozo) findViewById(R.id.tv_member);
        ImageView imgCall = (ImageView) findViewById(R.id.img_call);
        ImageView imgSms = (ImageView) findViewById(R.id.img_sms);
        imgCall.setOnClickListener(this);
        imgSms.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        taskResponse = (TaskResponse) getIntent().getSerializableExtra(Constants.TASK_DETAIL_EXTRA);
        taskId = taskResponse.getId();
        posterId = taskResponse.getPoster().getId();
        tvTitle.setText(taskResponse.getTitle());
        checkTask();
        LogUtils.d(TAG, "initData , taskResponse : " + taskResponse.toString());
        tvMember.setText(Utils.getMemberChat(this, taskResponse));
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
        messageCloudEndPoint = myRef.child("task-messages").child(String.valueOf(taskId));
        setUpMessageList();
        memberCloudEndPoint = myRef.child("members").child(String.valueOf(UserManager.getMyUser().getId()));
        memberEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                LogUtils.d(TAG, "memberEventListener onChildChanged , dataSnapshot : " + dataSnapshot.toString());
                //noinspection unchecked
                Map<String, Boolean> groups = (Map<String, Boolean>) dataSnapshot.getValue();
                LogUtils.d(TAG, "memberEventListener onChildChanged , groups : " + groups.toString());
                if (groups.containsKey(String.valueOf(taskId)) && !groups.get(String.valueOf(taskId))) {
                    Utils.showLongToast(ChatActivity.this, getString(R.string.kick_out_chat_content), true, false);
                    finish();
                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        memberCloudEndPoint.addChildEventListener(memberEventListener);
    }


    @Override
    protected void resumeData() {
        PreferUtils.setPushShow(this, false);
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) + ContextCompat
                .checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissions, Constants.PERMISSION_REQUEST_CODE);
        } else {
            permissionGranted();
        }
    }

    private void permissionGranted() {
        PickImageDialog pickImageDialog = new PickImageDialog(ChatActivity.this);
        pickImageDialog.setPickImageListener(new PickImageDialog.PickImageListener() {
            @Override
            public void onCamera() {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                startActivityForResult(cameraIntent, Constants.REQUEST_CODE_CAMERA);
            }

            @Override
            public void onGallery() {
                Intent intent = new Intent(ChatActivity.this, AlbumActivity.class);
                intent.putExtra(Constants.EXTRA_ONLY_IMAGE, true);
                startActivityForResult(intent, Constants.REQUEST_CODE_PICK_IMAGE, TransitionScreen.RIGHT_TO_LEFT);
            }
        });
        pickImageDialog.showView();
    }

    private Uri setImageUri() {
        File file = new File(FileUtils.getInstance().getHozoDirectory(), "image" + System.currentTimeMillis() + ".jpg");
        Uri imgUri = Uri.fromFile(file);
        this.imgPath = file.getAbsolutePath();
        return imgUri;
    }

    private void doAttachImage() {
        ProgressDialogUtils.showProgressDialog(this);
        File fileUp = Utils.compressFile(fileAttach);
        LogUtils.d(TAG, "doAttachImage , file Name : " + fileUp.getName());
        final RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), fileUp);
        MultipartBody.Part itemPart = MultipartBody.Part.createFormData("image", fileUp.getName(), requestBody);

        ApiClient.getApiService().uploadImage(UserManager.getUserToken(), itemPart).enqueue(new Callback<ImageResponse>() {
            @Override
            public void onResponse(Call<ImageResponse> call, Response<ImageResponse> response) {
                LogUtils.d(TAG, "uploadImage onResponse : " + response.body());
                LogUtils.d(TAG, "uploadImage code : " + response.code());
                if (response.code() == Constants.HTTP_CODE_CREATED) {
                    doChat(response.body().getUrl(), 1);
                    ProgressDialogUtils.dismissProgressDialog();
                } else if (response.code() == Constants.HTTP_CODE_UNAUTHORIZED) {
                    NetworkUtils.refreshToken(ChatActivity.this, new NetworkUtils.RefreshListener() {
                        @Override
                        public void onRefreshFinish() {
                            doAttachImage();
                        }
                    });
                } else if (response.code() == Constants.HTTP_CODE_BLOCK_USER) {
                    Utils.blockUser(ChatActivity.this);
                } else {
                    ProgressDialogUtils.dismissProgressDialog();
                    DialogUtils.showRetryDialog(ChatActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                        @Override
                        public void onSubmit() {
                            doAttachImage();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));

            }

            @Override
            public void onFailure(Call<ImageResponse> call, Throwable t) {
                ProgressDialogUtils.dismissProgressDialog();
                LogUtils.e(TAG, "uploadImage onFailure : " + t.getMessage());
                DialogUtils.showRetryDialog(ChatActivity.this, new AlertDialogOkAndCancel.AlertDialogListener() {
                    @Override
                    public void onSubmit() {
                        doAttachImage();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
                ProgressDialogUtils.dismissProgressDialog();
                FileUtils.deleteDirectory(new File(FileUtils.OUTPUT_DIR));
            }
        });
    }

    private void doChat(String chat, int type) {
        if (chat.equals("")) {
            Utils.showLongToast(ChatActivity.this, getString(R.string.empty_content_comment_error), true, false);
            return;
        }
        String key = messageCloudEndPoint.push().getKey();
        Message message = new Message();
        message.setUser_id(UserManager.getMyUser().getId());
        message.setMessage(chat);
        Map<String, Boolean> reads = new HashMap<>();
        reads.put(String.valueOf(UserManager.getMyUser().getId()), true);
        message.setReads(reads);
        message.setType(type);
        messageCloudEndPoint.child(key).setValue(message, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    LogUtils.d(TAG, "onComplete databaseError : " + databaseError.toString());
                    Utils.showLongToast(ChatActivity.this, getString(R.string.permission_chat_error), true, false);
                    finish();
                }
            }
        });
        message.setId(key);

        LogUtils.d(TAG, "doSend , message : " + message.toString());
        ProgressDialogUtils.dismissProgressDialog();
        imgPath = null;
        edtMsg.setText(getString(R.string.empty));
    }

    @Override
    protected void onStop() {
        super.onStop();
        PreferUtils.setPushShow(this, true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpMessageList() {
        LogUtils.d(TAG, "setUpMessageList start");
        messageAdapter = new MessageAdapter(this, messages, posterId);
        messageAdapter.setPosterId(posterId);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        rcvMessage.setHasFixedSize(true);
        rcvMessage.setLayoutManager(linearLayoutManager);
        rcvMessage.setAdapter(messageAdapter);

        edtMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    rcvMessage.smoothScrollToPosition(0);
                }
            }
        });

        mainLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.performClick();
                    Utils.hideKeyBoard(ChatActivity.this);
                    rcvMessage.requestFocus();
                    return true;
                }
                return false;
            }
        });

        rcvMessage.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    view.performClick();
                    Utils.hideKeyBoard(ChatActivity.this);
                    rcvMessage.requestFocus();
                    return true;
                }
                return false;
            }
        });

        EndlessRecyclerViewScrollListener endlessRecyclerViewScrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {

                LogUtils.d(TAG, "refreshList addOnScrollListener, page : " + page + " , totalItemsCount : " + totalItemsCount);
                if (isLoadingMoreFromServer) getMessage();

            }
        };
        rcvMessage.addOnScrollListener(endlessRecyclerViewScrollListener);

        if (childEventListener != null)
            messageCloudEndPoint.removeEventListener(childEventListener);

        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setId(dataSnapshot.getKey());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message : " + message.toString());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message id : " + dataSnapshot.getKey());
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , checkContain(message) : " + checkContain(message));
                if (checkContain(message)) return;
                messages.add(0, message);
                rcvMessage.scrollToPosition(0);//
                messageAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , messages size : " + messages.size());
                Map<String, Boolean> map = new HashMap<>();
                map.put(String.valueOf(UserManager.getMyUser().getId()), Boolean.TRUE);
                messageCloudEndPoint.child(dataSnapshot.getKey()).child("reads").child(String.valueOf(UserManager.getMyUser().getId())).setValue(true);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                Message message = dataSnapshot.getValue(Message.class);
//                LogUtils.d(TAG, "messageCloudEndPoint onChildChanged , message : " + message.toString());
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        messageCloudEndPoint.orderByKey().limitToLast(1).addChildEventListener(childEventListener);

    }

    private void getMessage() {

        if (isLoading) return;

        isLoading = true;

        valueEventListener = null;

        if (lastKeyMsg == null) {
            LogUtils.d(TAG, "getMessage start");
            recentPostsQuery = messageCloudEndPoint.orderByKey().limitToLast(PAGE_COUNT);
        } else {
            LogUtils.d(TAG, "getMessage lastKeyMsg : " + lastKeyMsg);
            recentPostsQuery = messageCloudEndPoint.orderByKey().endAt(lastKeyMsg).limitToLast(PAGE_COUNT);
        }

        if (valueEventListener != null && recentPostsQuery != null)
            recentPostsQuery.removeEventListener(valueEventListener);

        if (valueEventListener != null)
            messageCloudEndPoint.removeEventListener(valueEventListener);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Message> messagesAdded = new ArrayList<>();
                int i = 0;
                LogUtils.d(TAG, "addValueEventListener dataSnapshot.getChildrenCount() : " + dataSnapshot.getChildrenCount());
                if (dataSnapshot.getChildrenCount() > 0) setResult(Constants.RESULT_CODE_CHAT);
                if (dataSnapshot.getChildrenCount() == 0) messageAdapter.stopLoadMore();

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    if (dataSnapshot.getChildrenCount() < PAGE_COUNT) {
                        Message message = dataSnapshot1.getValue(Message.class);
                        message.setId(dataSnapshot1.getKey());
                        LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());
                        LogUtils.d(TAG, "addValueEventListener recentPostsQuery key : " + dataSnapshot1.getKey());
                        if (!checkContain(message))
                            messagesAdded.add(0, message);
                        isLoadingMoreFromServer = false;
                        messageAdapter.stopLoadMore();
                    } else {
                        if (i == 0) lastKeyMsg = dataSnapshot1.getKey();
                        else {
                            Message message = dataSnapshot1.getValue(Message.class);
                            message.setId(dataSnapshot1.getKey());
                            LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());

                            if (!checkContain(message))
                                messagesAdded.add(0, message);
                        }
                    }

                    i++;

                }

                messages.addAll(messagesAdded);
                if (messageAdapter != null) messageAdapter.notifyDataSetChanged();
                LogUtils.d(TAG, "addValueEventListener messages size : " + messages.size());
                isLoading = false;
                if (valueEventListener != null && recentPostsQuery != null)
                    recentPostsQuery.removeEventListener(valueEventListener);

                if (valueEventListener != null)
                    messageCloudEndPoint.removeEventListener(valueEventListener);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                isLoading = false;

                if (valueEventListener != null && recentPostsQuery != null)
                    recentPostsQuery.removeEventListener(valueEventListener);

                if (valueEventListener != null)
                    messageCloudEndPoint.removeEventListener(valueEventListener);
            }
        };

        valueEventListener = recentPostsQuery.addValueEventListener(valueEventListener);

    }

    private void checkTask() {
        LogUtils.d(TAG, "checkTask start , task id : " + taskResponse.getId());
        groupTaskListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                switch (dataSnapshot.getKey()) {
                    case "block":
                        boolean block = (boolean) dataSnapshot.getValue();
                        if (block) {
                            Utils.showLongToast(ChatActivity.this, getString(R.string.block_task), true, false);
                            finish();
                        }
                        break;
                    case "close":
                        boolean close = (boolean) dataSnapshot.getValue();
                        if (close) {
                            Utils.showLongToast(ChatActivity.this, getString(R.string.close_task), true, false);
                            finish();
                        }
                        break;
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                LogUtils.d(TAG, "checkTask members  add, task id : " + dataSnapshot.toString());
                switch (dataSnapshot.getKey()) {
                    case "block":
                        boolean block = (boolean) dataSnapshot.getValue();
                        if (block) {
                            Utils.showLongToast(ChatActivity.this, getString(R.string.block_task), true, false);
                            finish();
                        }
                        break;
                    case "close":
                        boolean close = (boolean) dataSnapshot.getValue();
                        if (close) {
                            Utils.showLongToast(ChatActivity.this, getString(R.string.close_task), true, false);
                            finish();
                        }
                        break;
                    case "members":
                        Map<String, Boolean> members = (Map<String, Boolean>) dataSnapshot.getValue();
                        if (members.containsKey(String.valueOf(taskResponse.getPoster().getId())) && !members.get(String.valueOf(taskResponse.getPoster().getId()))) {
                            Utils.showLongToast(ChatActivity.this, getString(R.string.kick_out_task_content), true, false);
                            finish();
                        }

                        break;

                }

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        groupTaskReference = FirebaseDatabase.getInstance().getReference().child("groups-task").child(String.valueOf(taskResponse.getId()));
        groupTaskReference.addChildEventListener(groupTaskListener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.d(TAG, "onActivityResult , requestCode : " + requestCode + " , resultCode : " + resultCode);
        if (requestCode == Constants.REQUEST_CODE_PICK_IMAGE
                && resultCode == Constants.RESPONSE_CODE_PICK_IMAGE
                && data != null) {
            ArrayList<Image> imagesSelected = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            imgPath = imagesSelected.get(0).getPath();
            fileAttach = new File(imgPath);
            doAttachImage();
        } else if (requestCode == Constants.REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            fileAttach = new File(imgPath);
            doAttachImage();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0) {
            boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
            boolean readExternalFile = grantResults[0] == PackageManager.PERMISSION_GRANTED;

            if (cameraPermission && readExternalFile) {
                permissionGranted();
            }
        }
    }

    private boolean checkContain(Message message) {
        for (int i = 0; i < messages.size(); i++)
            if (message.getId().equals(messages.get(i).getId())) return true;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (valueEventListener != null)
            messageCloudEndPoint.removeEventListener(valueEventListener);
        if (childEventListener != null)
            messageCloudEndPoint.removeEventListener(childEventListener);
        if (memberEventListener != null)
            memberCloudEndPoint.removeEventListener(memberEventListener);
        if (groupTaskListener != null && groupTaskReference != null)
            groupTaskReference.removeEventListener(groupTaskListener);
    }

    private void showMenu() {

        //Creating the instance of PopupMenu
        PopupMenu popup = new PopupMenu(this, imgMenu);
        popup.getMenuInflater().inflate(R.menu.menu_chat, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.menu_detail_task:
                        Intent intent = new Intent(ChatActivity.this, DetailTaskActivity.class);
                        intent.putExtra(Constants.TASK_ID_EXTRA, taskId);
                        startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                        break;

                }
                return true;
            }
        });

        popup.show();
    }

    private void doCall() {
        if (posterId == UserManager.getMyUser().getId()) {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
        } else {
            Utils.call(this, taskResponse.getPoster().getPhone());
        }
    }

    private void doSms() {
        if (posterId == UserManager.getMyUser().getId()) {
            Intent intent = new Intent(this, ContactActivity.class);
            intent.putExtra(Constants.TASK_DETAIL_EXTRA, taskResponse);
            startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
        } else {
            Utils.sendSms(this, taskResponse.getPoster().getPhone(), "");
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_send:
                doChat(edtMsg.getText().toString().trim(), 0);
                rcvMessage.scrollToPosition(0);
                break;

            case R.id.img_back:
                finish();
                break;

            case R.id.img_menu:
                showMenu();
                break;
            case R.id.img_attach:
                checkPermission();
                break;
            case R.id.img_attached:
                Intent imgIntent = new Intent(ChatActivity.this, PreviewImageActivity.class);
                imgIntent.putExtra(Constants.EXTRA_IMAGE_PATH, imgPath);
                startActivity(imgIntent, TransitionScreen.RIGHT_TO_LEFT);
                break;
            case R.id.img_call:
                doCall();
                break;

            case R.id.img_sms:
                doSms();
                break;

            case R.id.tv_title:
                Intent intent = new Intent(ChatActivity.this, DetailTaskActivity.class);
                intent.putExtra(Constants.TASK_ID_EXTRA, taskId);
                startActivity(intent, TransitionScreen.RIGHT_TO_LEFT);
                break;

        }
    }

}
