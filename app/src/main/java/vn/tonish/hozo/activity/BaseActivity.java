package vn.tonish.hozo.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import vn.tonish.hozo.R;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;
import vn.tonish.hozo.view.EdittextHozo;
import vn.tonish.hozo.view.TextViewHozo;

/**
 * Created by LongBui on 4/12/17.
 */
public abstract class BaseActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = BaseActivity.class.getName();
    private FragmentManager fragmentManager;
    private ProgressDialog progressDialog;
    private TransitionScreen transitionScreen;

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void resumeData();

    @Override
    public void onRefresh() {

    }

    public void createSwipeToRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swpRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    protected void setBackButtonHozo() {

        findViewById(R.id.btnBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            getWindow().setStatusBarColor(getResources().getColor(R.color.pink));
//        }

        if (getIntent().hasExtra(Constants.TRANSITION_EXTRA))
            transitionScreen = (TransitionScreen) getIntent().getSerializableExtra(Constants.TRANSITION_EXTRA);

        LogUtils.d(TAG, "BaseActivity , transitionScreen : " + transitionScreen);

        fragmentManager = getSupportFragmentManager();
        setContentView(getLayout());
        initView();
        initData();
    }


    public void onStopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeData();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    protected void setTitleHeader(String text) {
        TextViewHozo tv_title = (TextViewHozo) findViewById(R.id.tvTitleHeader);
        tv_title.setText(text.trim());
    }

    @Override
    public void finish() {
        super.finish();
        if (transitionScreen != null)
            TransitionScreen.overridePendingTransitionOut(this, transitionScreen);
    }

    protected void startActivityForResult(Intent intent, int requestCode, TransitionScreen transitionScreen) {
        intent.putExtra(Constants.TRANSITION_EXTRA, transitionScreen);
        startActivityForResult(intent, requestCode);
        TransitionScreen.overridePendingTransition(this, transitionScreen);
    }

    public void startActivity(Intent intent, TransitionScreen transitionScreen) {
        intent.putExtra(Constants.TRANSITION_EXTRA, transitionScreen);
        startActivity(intent);
        TransitionScreen.overridePendingTransition(this, transitionScreen);
    }

    protected void startActivityAndClearAllTask(Intent intent, TransitionScreen transitionScreen) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.TRANSITION_EXTRA, transitionScreen);
        startActivity(intent);
        TransitionScreen.overridePendingTransition(this, transitionScreen);
    }

    public void startActivityAndClearAllTask(Class<?> cls, TransitionScreen transitionScreen) {
        Intent intent = new Intent(this, cls);
        startActivityAndClearAllTask(intent, transitionScreen);
    }

    public void startActivity(Class<?> cls, TransitionScreen transitionScreen) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
        TransitionScreen.overridePendingTransition(this, transitionScreen);
    }

    public void startActivity(Class<?> cls, Bundle bundle, TransitionScreen transitionScreen) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent, transitionScreen);
    }

    void openFragment(int resId, Class<? extends Fragment> fragmentClazz, boolean addBackStack, TransitionScreen transitionScreen) {
        openFragment(resId, fragmentClazz, null, addBackStack, transitionScreen);
    }

    public void openFragmentBundle(int resId, Class<? extends Fragment> fragmentClazz, Bundle bundle, boolean addBackStack, TransitionScreen transitionScreen) {
        openFragment(resId, fragmentClazz, bundle, addBackStack, transitionScreen);
    }

    private void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack, TransitionScreen transitionScreen) {
        FragmentManager manager = getSupportFragmentManager();
        String tag = fragmentClazz.getName();
        try {
            Fragment fragment;
            try {
                fragment = fragmentClazz.newInstance();
                if (args != null) {
                    fragment.setArguments(args);
                }
                FragmentTransaction transaction = manager.beginTransaction();
                TransitionScreen.setCustomAnimationsFragment(transaction,transitionScreen);
                transaction.replace(resId, fragment, tag);

                if (addBackStack) {
                    transaction.addToBackStack(tag);
                }
                transaction.commitAllowingStateLoss();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // hide keyboard when touch outside edit_text
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EdittextHozo && !view.getClass().getName().startsWith("android.webkit.")) {
            int scr_coord[] = new int[2];
            view.getLocationOnScreen(scr_coord);
            float x = ev.getRawX() + view.getLeft() - scr_coord[0];
            float y = ev.getRawY() + view.getTop() - scr_coord[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            fragmentManager.popBackStack();
        }
    }

    protected void showLoadingProgress(String msg) {
        progressDialog = new ProgressDialog(BaseActivity.this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }

    protected void hideLoadingProgress() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
