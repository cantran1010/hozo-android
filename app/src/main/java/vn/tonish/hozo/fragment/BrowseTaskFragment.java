package vn.tonish.hozo.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.WorkAdapter;
import vn.tonish.hozo.model.Work;
import vn.tonish.hozo.network.NetworkUtils;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;

/**
 * Created by Admin on 4/4/2017.
 * Edited by huyquynh on 19/4/2017
 */

public class BrowseTaskFragment extends BaseFragment implements NetworkUtils.NetworkListener {

    private static double lat = 21.000030;
    private static double lon = 105.837400;

    private RecyclerView lvList;
    private WorkAdapter workAdapter;
    private LinearLayoutManager lvManager;
    private List<Work> workList;

    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        lvManager = new LinearLayoutManager(getActivity());
        workList = new ArrayList<>();


        lvList.setLayoutManager(lvManager);

        for (int i = 0; i < 10; i++) {
            Work work = new Work();
            work.setId(i);
            work.setName("Hey ! Are you free tonight!");
            work.setTime("2017-04-18T03:48:10+00:00");
            work.setNew(true);
            work.setDescription("15 phut truoc . Ha Noi . Phan loai : Cong nghe");
            work.setPrice("500000");
            workList.add(work);
        }
        workAdapter = new WorkAdapter(getActivity(), workList);
        lvList.setAdapter(workAdapter);

        lvList.addOnScrollListener(new EndlessRecyclerViewScrollListener(lvManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Handler handler = new Handler();
                handler.postDelayed(new TimerTask() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            Work work = new Work();
                            work.setId(i);
                            work.setName("Hey ! Are you free tonight!");
                            work.setTime("2017-04-18T03:48:10+00:00");
                            work.setNew(true);
                            work.setDescription("15 phut truoc . Ha Noi . Phan loai : Cong nghe");
                            work.setPrice("500000");
                            workList.add(work);
                        }
                        workAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void resumeData() {

    }

    public void getData() {
        //        if (workList.size() == 0) {
//            NetworkUtils.postVolley(true, true, true, getActivity(), "", new JSONObject(), this);
//        }
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

    @Override
    public void onSuccess(JSONObject jsonResponse) {
        if (jsonResponse != null) {
            // String json = jsonResponse.toString();
        }
    }

    @Override
    public void onError() {

    }
}
