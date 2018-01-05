package vn.tonish.hozo.activity;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.ChatPrivateAdapter;
import vn.tonish.hozo.model.Message;

/**
 * Created by CanTran on 1/5/18.
 */

public class ChatPrivateActivity extends BaseActivity {

    private EditText metText;
    private Button mbtSent;
    private DatabaseReference mFirebaseRef;

    private List<Message> mChats;
    private RecyclerView mRecyclerView;
    private ChatPrivateAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_private_chat;
    }

    @Override
    protected void initView() {
        metText = (EditText) findViewById(R.id.etText);
        mbtSent = (Button) findViewById(R.id.btSent);
        mRecyclerView = (RecyclerView) findViewById(R.id.rvChat);
        mChats = new ArrayList<>();

    }

    @Override
    protected void initData() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //mRecyclerView.setItemAnimator(new SlideInOutLeftItemAnimator(mRecyclerView));
        mAdapter = new ChatPrivateAdapter(mChats);
        mRecyclerView.setAdapter(mAdapter);
        /**
         * Firebase - Inicialize
         */
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mFirebaseRef = database.getReference("message");

    }

    @Override
    protected void resumeData() {

    }
}
