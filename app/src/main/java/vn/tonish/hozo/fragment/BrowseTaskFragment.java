package vn.tonish.hozo.fragment;

import android.content.Intent;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import vn.tonish.hozo.R;
import vn.tonish.hozo.activity.AdvanceSettingsActivity;
import vn.tonish.hozo.view.EdittextHozo;

import static vn.tonish.hozo.R.id.edt_search;
import static vn.tonish.hozo.R.id.fr_search;
import static vn.tonish.hozo.utils.Utils.hideKeyBoard;

/**
 * Created by Can Tran on 4/11/17.
 */

public class BrowseTaskFragment extends BaseFragment implements View.OnClickListener {
    private ImageView imgSearch, imgLocation, imgControls, imgBack, imgClear;
    private FrameLayout layoutHeader, layoutSearch;
    private EdittextHozo edtSearch;
    private boolean checkView = true;
    private Animation rtAnimation;
    private Animation lanimation;


    @Override
    protected int getLayout() {
        return R.layout.search_fragment;
    }

    @Override
    protected void initView() {
        rtAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_right);
        lanimation = AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_left);
        imgSearch = (ImageView) findViewById(R.id.img_search);
        imgLocation = (ImageView) findViewById(R.id.img_location);
        imgControls = (ImageView) findViewById(R.id.img_controls);
        imgClear = (ImageView) findViewById(R.id.img_clear);
        imgBack = (ImageView) findViewById(R.id.img_back);
        layoutHeader = (FrameLayout) findViewById(R.id.browse_task_header);
        edtSearch = (EdittextHozo) findViewById(edt_search);
        layoutSearch = (FrameLayout) findViewById(fr_search);
    }

    @Override
    protected void initData() {
        openFragment(R.id.find_task_container, ListTaskFragment.class, false,true);
        imgControls.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgSearch.setOnClickListener(this);
        imgBack.setOnClickListener(this);
        imgClear.setOnClickListener(this);
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                hideKeyBoard(getActivity());
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    performSearch();
                    return true;
                }
                return false;
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (edtSearch.getText().toString().length() > 0) {
                    imgClear.setVisibility(View.VISIBLE);
                } else {
                    imgClear.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    @Override
    protected void resumeData() {

    }

    public void getData() {
    }

    @Override
    public void onRefresh() {
        super.onRefresh();
        getData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_location:
                if (checkView) {
                    openFragment(R.id.find_task_container, MapTaskFragment.class, false,true);
                    checkView = false;
                } else {
                    openFragment(R.id.find_task_container, ListTaskFragment.class, false,false);
                    checkView = true;
                }

                break;
            case R.id.img_search:
                showSearch(layoutSearch, 200, true);
                showSearch(layoutHeader, 200, false);
                break;
            case R.id.img_back:
                showSearch(layoutHeader, 200, true);
                showSearch(layoutSearch, 200, false);
                break;
            case R.id.img_clear:
                edtSearch.setText("");
                break;
            case R.id.img_controls:
                startActivity(new Intent(getContext(), AdvanceSettingsActivity.class));
                break;
        }

    }

    private void showSearch(final View view, int duration, boolean isShow) {
        if (isShow) {
            view.setVisibility(View.VISIBLE);
            view.startAnimation(rtAnimation);
        } else {
            view.startAnimation(lanimation);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    view.setVisibility(View.GONE);
                }
            }, duration);
        }
    }

}

