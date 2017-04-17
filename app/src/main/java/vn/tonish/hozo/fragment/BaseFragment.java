package vn.tonish.hozo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.BaseActivity;

public abstract class BaseFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void resumeData();

    private View view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }

        try {
            view = inflater.inflate(getLayout(), container, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initView();
        initData();
        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

    }

    public SwipeRefreshLayout swipeRefreshLayout;

    public void createSwipeToRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swpRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        resumeData();
    }

    protected View findViewById(int id) {
        return view.findViewById(id);
    }

    protected void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        intent.putExtras(bundle);
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    protected void startActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack) {

        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            BaseActivity baseActivity = (BaseActivity) activity;
            baseActivity.openFragmentBundle(resId, fragmentClazz, args, true);
        }
    }

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, boolean addBackStack) {
        openFragment(resId, fragmentClazz, null, addBackStack);
    }

}
