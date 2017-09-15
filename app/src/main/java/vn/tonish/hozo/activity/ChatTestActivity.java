package vn.tonish.hozo.activity;

import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.Message;
import vn.tonish.hozo.model.Task;
import vn.tonish.hozo.utils.LogUtils;

/**
 * Created by LongBui on 9/13/17.
 */

public class ChatTestActivity extends BaseActivity {

    private static final String TAG = ChatTestActivity.class.getSimpleName();

    @Override
    protected int getLayout() {
        return R.layout.chat_activity;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
// Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        DatabaseReference journalCloudEndPoint = myRef.child("groups-task");
        // Read from the database
        journalCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

//                Log.d(TAG, "addValueEventListener dataSnapshot toString : " + dataSnapshot.toString());
//                Log.d(TAG, "addValueEventListener dataSnapshot getValue : " + dataSnapshot.getValue());

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Task task = dataSnapshot1.getValue(Task.class);
                    LogUtils.d(TAG, "addValueEventListener task : " + task.toString());
                }

//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "addValueEventListener Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "addValueEventListener Failed to read value.", error.toException());
            }
        });

        DatabaseReference journal1021 = journalCloudEndPoint.child("1021");
        journal1021.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "addValueEventListener dataSnapshot 1021 toString : " + dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query recentPostsQuery = myRef.child("task-messages").child("1022").orderByKey().endAt("-Ktuk-zg0B4FOb1n17Qh").limitToLast(10);

        recentPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                    LogUtils.d(TAG, "addValueEventListener recentPostsQuery : " + message.toString());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference messageCloudEndPoint = myRef.child("task-messages").child("1022");
        // Read from the database
        messageCloudEndPoint.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

//                Log.d(TAG, "addValueEventListener dataSnapshot toString : " + dataSnapshot.toString());
//                Log.d(TAG, "addValueEventListener dataSnapshot getValue : " + dataSnapshot.getValue());

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Message message = dataSnapshot1.getValue(Message.class);
                    LogUtils.d(TAG, "addValueEventListener message : " + message.toString());
                    LogUtils.d(TAG, "addValueEventListener key : " + dataSnapshot1.getKey());
                    LogUtils.d(TAG, "addValueEventListener value : " + dataSnapshot1.getValue());
                }

//                String value = dataSnapshot.getValue(String.class);
//                Log.d(TAG, "addValueEventListener Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "addValueEventListener Failed to read value.", error.toException());
            }
        });

        messageCloudEndPoint.limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                LogUtils.d(TAG, "messageCloudEndPoint onChildAdded , message : " + message.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Message message = dataSnapshot.getValue(Message.class);
                LogUtils.d(TAG, "messageCloudEndPoint onChildChanged , message : " + message.toString());
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
        });

//        // isert new message
        for (int i = 1; i <= 30; i++) {
            String key = messageCloudEndPoint.push().getKey();
            Message message = new Message(i, "android test post message " + i, "2017-09-20T03:15:26.897151101Z");
            messageCloudEndPoint.child(key).setValue(message);
        }

    }

    @Override
    protected void resumeData() {

    }
}
