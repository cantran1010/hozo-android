package vn.tonish.hozo.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import vn.tonish.hozo.view.TextViewHozo;

import java.util.ArrayList;
import java.util.List;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.NotificationAdapter;
import vn.tonish.hozo.view.DividerItemDecoration;
import vn.tonish.hozo.model.Notification;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;

/**
 * Created by Admin on 4/4/2017.
 */

public class InboxFragment extends BaseFragment {

    protected LinearLayoutManager linearLayoutManager;
    private NotificationAdapter adapter;
    protected RecyclerView lvList;
    private List<Notification> notifications;


    protected TextViewHozo tv_notify;

    @Override
    protected int getLayout() {
        return vn.tonish.hozo.R.layout.fragment_notify;
    }

    @Override
    protected void initView() {
        tv_notify = (TextViewHozo) findViewById(R.id.tv_notify);

        linearLayoutManager = new LinearLayoutManager(getActivity());
        lvList = (RecyclerView) findViewById(R.id.lvList);
        notifications = new ArrayList<>();
        adapter = new NotificationAdapter(getActivity(), notifications);
        lvList.setLayoutManager(linearLayoutManager);
        lvList.addItemDecoration(new DividerItemDecoration(getActivity()));
        lvList.setAdapter(adapter);
        dummyData();

        lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {

            }
        });
        createSwipeToRefresh();
    }


    public void dummyData() {
        for (int i = 0; i < 10; i++) {
            Notification notification = new Notification();
            notification.setId(i);
            notification.setContent("Bạn nhận được 1 tin nhắn từ Hozo");
            notification.setCreated_date("2017-04-18T03:48:10+00:00");
            notification.setTitle("Bạn nhận được 1 tin nhắn từ Hozo");
            notifications.add(notification);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    @Override
    public void onRefresh() {
        super.onRefresh();

    }
}
