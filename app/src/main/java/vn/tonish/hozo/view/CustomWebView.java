/*

 */
package vn.tonish.hozo.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import vn.tonish.hozo.utils.LogUtils;

import static android.content.ContentValues.TAG;


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

    @SuppressLint("SetJavaScriptEnabled")
    private void initView() {
        this.getSettings().setJavaScriptEnabled(true);
        this.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        this.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        this.getSettings().setAppCacheEnabled(true);
        this.setWebViewClient(new CustomWebViewClient());
    }

    /**
     * Sets the WebViewClient that will receive various notifications and
     * requests. This will replace the current handler.
     *
     * @author longbd
     */
    private class CustomWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(final WebView view, final String url,
                                  Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            LogUtils.d(TAG, "onPageStarted: " + url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtils.d(TAG, "onPageFinished: " + url);

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
        }

    }

}
