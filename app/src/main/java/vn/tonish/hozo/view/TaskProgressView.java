package vn.tonish.hozo.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import vn.tonish.hozo.R;

/**
 * Created by LongBui on 5/11/2017.
 */

public class TaskProgressView extends LinearLayout {

    private TextViewHozo tvNumberEmploy, tvNumberRemain, tvNumberBid;

    public TaskProgressView(Context context) {
        super(context);
        init();
    }

    public TaskProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TaskProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public TaskProgressView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        layoutInflater.inflate(R.layout.task_progress_view, this, true);

        tvNumberEmploy = (TextViewHozo) findViewById(R.id.tv_number_employ);
        tvNumberRemain = (TextViewHozo) findViewById(R.id.tv_number_remain);
        tvNumberBid = (TextViewHozo) findViewById(R.id.tv_number_bid);
    }

    public void updateData(int numberBid, int numberRemain, int numberEmploy) {
        String s1 = numberBid + getContext().getString(R.string.number_bit_footer_des);
        String s2 = numberEmploy + getContext().getString(R.string.number_employ_footer_des);

        tvNumberBid.setText(s1);
        tvNumberEmploy.setText(s2);
        tvNumberRemain.setText(getContext().getString(R.string.number_remain_footer, numberRemain));
    }

}
