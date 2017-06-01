/*

 */
package vn.tonish.hozo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebSettings;
import android.webkit.WebView;


/**
 * Created by LongBui on 5/18/17.
 */
public class CustomWebView extends WebView {

    public CustomWebView(Context context) {
        super(context);
        initView();
    }

    public CustomWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        this.getSettings().setJavaScriptEnabled(true);
        this.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.getSettings().setAppCacheEnabled(true);
    }

}
