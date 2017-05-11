package vn.tonish.hozo.fragment;


import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.WorkAdapter;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;

import static vn.tonish.hozo.R.id.lv_list;

/**
 * Created by Can Tran on 04/05/2017.
 */

public class ListTaskFragment extends BaseFragment {
    private RecyclerView lvList;
    private WorkAdapter workAdapter;
    private LinearLayoutManager lvManager;
    private List<TaskResponse> workList;

    @Override
    protected int getLayout() {
        return R.layout.fragment_list_task;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(lv_list);
    }

    @Override
    protected void initData() {
        lvManager = new LinearLayoutManager(getActivity());
        workList = new ArrayList<>();


        lvList.setLayoutManager(lvManager);

        for (int i = 0; i < 10; i++) {
//            Work work = new Work();
//            work.setId(i);
//            work.setName("Hey ! Are you free tonight!");
//            work.setTime("2017-04-18T03:48:10+00:00");
//            work.setNew(true);
//            work.setDescription("15 phut truoc . Ha Noi . Phan loai : Cong nghe");
//            work.setPrice("500000");
//            workList.add(work);
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
//                            Work work = new Work();
//                            work.setId(i);
//                            work.setName("Hey ! Are you free tonight!");
//                            work.setTime("2017-04-18T03:48:10+00:00");
//                            work.setNew(true);
//                            work.setDescription("15 phut truoc . Ha Noi . Phan loai : Cong nghe");
//                            work.setPrice("500000");
//                            workList.add(work);
                        }
                        workAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });

    }

    @Override
    protected void resumeData() {

    }
}
