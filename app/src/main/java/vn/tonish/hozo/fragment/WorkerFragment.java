package vn.tonish.hozo.fragment;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import vn.tonish.hozo.R;
import vn.tonish.hozo.adapter.WorkAdapter;
import vn.tonish.hozo.rest.responseRes.TaskResponse;
import vn.tonish.hozo.utils.EndlessRecyclerViewScrollListener;

/**
 * Created by CanTran on 23/04/2017.
 */

public class WorkerFragment extends BaseFragment implements View.OnClickListener {
    private RecyclerView lvList;
    private WorkAdapter workAdapter;
    private LinearLayoutManager lvManager;
    private List<TaskResponse> workList;

    @Override
    public void onClick(View view) {

    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_worker;
    }

    @Override
    protected void initView() {
        lvList = (RecyclerView) findViewById(R.id.lvList);
        lvManager = new LinearLayoutManager(getActivity());
        workList = new ArrayList<>();
        lvList.setLayoutManager(lvManager);
        workAdapter = new WorkAdapter(getActivity(), workList);

    }

    @Override
    protected void initData() {
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
