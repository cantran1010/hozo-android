package vn.tonish.hozo.fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.NotificationRecyclerAdapter;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.model.NotificationMessage;

/**
 * Created by Admin on 4/4/2017.
 */

public class InboxFragment extends BaseFragment {
    LinearLayoutManager linearLayoutManager;
    private  ArrayList<Object> Notifications = new ArrayList<>();
    private NotificationRecyclerAdapter adapter;
    private RecyclerView  recyclerView;
    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.notifi_fragment;
    }

    @Override
    protected void initView() {
        Notifications.add(new Notification(0,"1","xem hàng họ","hàng ngon vãi","24/10/2017"));
        Notifications.add(new NotificationMessage(1,"1","type","xem hàng họ","hàng ngon vãi","24/10/2017"));
        linearLayoutManager = new LinearLayoutManager(getActivity(), OrientationHelper.VERTICAL, false);
        recyclerView = (RecyclerView) findViewById(R.id.lv_notification);
        adapter = new NotificationRecyclerAdapter(getContext(),Notifications);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);


    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

}
