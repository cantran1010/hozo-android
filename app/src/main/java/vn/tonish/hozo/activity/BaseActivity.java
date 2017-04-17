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
import android.widget.EditText;
import android.widget.ProgressBar;

import vn.tonish.hozo.R;

public abstract class BaseActivity extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = BaseActivity.class.getName();
    FragmentManager fragmentManager;


    private SwipeRefreshLayout swipeRefreshLayout;

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

    public void setBackButton() {

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

        fragmentManager = getSupportFragmentManager();
        setContentView(getLayout());
        initView();
        initData();
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

    @Override
    public void finish() {
        super.finish();
        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startActivityAndClearAllTask(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        super.startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void startActivityAndClearAllTask(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivityAndClearAllTask(intent);
    }

    public void startActivityDefault(Intent intent) {
        super.startActivity(intent);
    }

    public void startActivity(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startActivity(intent);
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, boolean addBackStack) {
        openFragment(resId, fragmentClazz, null, addBackStack);
    }

    public void openFragmentBundle(int resId, Class<? extends Fragment> fragmentClazz, Bundle bundle, boolean addBackStack) {
        openFragment(resId, fragmentClazz, bundle, addBackStack);
    }

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack) {
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
                transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout, R.anim.fadein, R.anim.fadeout);
                transaction.replace(resId, fragment, tag);

                if (addBackStack) {
                    transaction.addToBackStack(tag);
                }
                transaction.commitAllowingStateLoss();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
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

}
