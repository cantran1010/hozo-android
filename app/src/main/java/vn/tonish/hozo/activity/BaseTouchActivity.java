package vn.tonish.hozo.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;

import java.io.Serializable;
import java.util.Stack;

import vn.tonish.hozo.R;
import vn.tonish.hozo.broadcast.BlockBroadCastReceiver;
import vn.tonish.hozo.common.Constants;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.TransitionScreen;

/**
 * Created by LongBui on 4/12/17.
 */
public abstract class BaseTouchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = BaseTouchActivity.class.getName();
    private FragmentManager fragmentManager;
    private TransitionScreen transitionScreen;
    private SwipeRefreshLayout swipeRefreshLayout;
    private final Stack<StackEntry> fragmentsStack = new Stack<>();
    private FragmentTransaction transaction;
    private BlockBroadCastReceiver blockBroadCastReceiver = new BlockBroadCastReceiver();

    protected abstract int getLayout();

    protected abstract void initView();

    protected abstract void initData();

    protected abstract void resumeData();

    @Override
    public void onRefresh() {

    }

    void createSwipeToRefresh() {
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swpRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.hozo_bg, R.color.hozo_red, R.color.blue_2);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        if (getIntent().hasExtra(Constants.TRANSITION_EXTRA))
            transitionScreen = (TransitionScreen) getIntent().getSerializableExtra(Constants.TRANSITION_EXTRA);
        fragmentManager = getSupportFragmentManager();
        setContentView(getLayout());
        initView();
        initData();
    }


    void onStopRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeData();
        registerReceiver(blockBroadCastReceiver, new IntentFilter(Constants.BROAD_CAST_BLOCK_USER));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(blockBroadCastReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    protected void startActivity(Intent intent, TransitionScreen transitionScreen) {
        intent.putExtra(Constants.TRANSITION_EXTRA, transitionScreen);
        startActivity(intent);
        TransitionScreen.overridePendingTransition(this, transitionScreen);
    }

    private void startActivityAndClearAllTask(Intent intent, TransitionScreen transitionScreen) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constants.TRANSITION_EXTRA, transitionScreen);
        startActivity(intent);
        TransitionScreen.overridePendingTransition(this, transitionScreen);
    }

    public void startActivityAndClearAllTask(Class<?> cls, TransitionScreen transitionScreen) {
        Intent intent = new Intent(this, cls);
        startActivityAndClearAllTask(intent, transitionScreen);
    }

    void startActivity(Class<?> cls, TransitionScreen transitionScreen) {
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

    public void openFragment(int resId, Class<? extends Fragment> fragmentClazz, Bundle args, boolean addBackStack, TransitionScreen transitionScreen) {
        transaction = fragmentManager.beginTransaction();
        String tag = fragmentClazz.getName();
        Fragment fragment = null;
        try {
            fragment = fragmentClazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        if (args != null) {
            assert fragment != null;
            fragment.setArguments(args);
        }
        TransitionScreen.setCustomAnimationsFragment(transaction, transitionScreen);
        transaction.replace(resId, fragment, tag);

        if (addBackStack) {
            transaction.addToBackStack(tag);
        }
        transaction.commitAllowingStateLoss();

        fragmentsStack.push(new StackEntry(fragmentClazz.getName()));
    }

    public void showFragment(int resLayout, Class<?> newFragClass,
                             boolean putStack, Bundle bundle, TransitionScreen transitionScreen) {

        transaction = fragmentManager.beginTransaction();
        TransitionScreen.setCustomAnimationsFragment(transaction, transitionScreen);
        String newTag = newFragClass.getName();

        Fragment lastFragment = getLastFragment();
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }

        // find the fragment in fragment manager
        Fragment newFragment = fragmentManager.findFragmentByTag(newTag);
        if (newFragment != null && !newFragment.isRemoving()) {
            LogUtils.d(TAG, "showFragment , new fragment != null");
            transaction.show(newFragment).commit();
        } else {
            LogUtils.d(TAG, "showFragment , new fragment == null");
            Fragment nFrag = null;
            try {
                nFrag = (Fragment) newFragClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            assert nFrag != null;
            nFrag.setArguments(bundle);

            if (putStack) {
                transaction.add(resLayout, nFrag, newTag)
                        .addToBackStack(newTag).commit();
            } else {
                transaction.add(resLayout, nFrag, newTag).commit();
            }

        }

        fragmentsStack.push(new StackEntry(newTag));
    }

    public void removeFragment(Class<?> newFragClass) {
        String newTag = newFragClass.getName();
        Fragment fragment = fragmentManager.findFragmentByTag(newTag);
        if (fragment != null) {
            transaction = fragmentManager.beginTransaction();
            transaction.remove(fragment).commit();
        }
    }

    private Fragment getLastFragment() {
        if (fragmentsStack.isEmpty())
            return null;
        String fragTag = fragmentsStack.peek().getFragTag();
        return getSupportFragmentManager().findFragmentByTag(
                fragTag);
    }

    private static class StackEntry implements Serializable {
        private String fragTag = null;

        public StackEntry(String fragTag) {
            super();
            this.fragTag = fragTag;
        }

        public String getFragTag() {
            return fragTag;
        }
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
