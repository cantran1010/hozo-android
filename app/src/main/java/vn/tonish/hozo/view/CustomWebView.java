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

import vn.tonish.hozo.R;
import vn.tonish.hozo.dialog.AlertDialogOkAndCancel;
import vn.tonish.hozo.utils.DialogUtils;
import vn.tonish.hozo.utils.LogUtils;
import vn.tonish.hozo.utils.ProgressDialogUtils;

import static android.content.ContentValues.TAG;


/**
 * Created by LongBui on 5/18/17.
 */
public class CustomWebView extends WebView {

    private static final String RECHARGE_SUCCESS_LINK = "https://staging-api.hozo.vn/v1/web/wallet/callbacks/1pay?_success=1";

    public interface WebviewListener {
        void onSuccess();

        void onFail();
    }

    private WebviewListener webviewListener;

    public WebviewListener getWebviewListener() {
        return webviewListener;
    }

    public void setWebviewListener(WebviewListener webviewListener) {
        this.webviewListener = webviewListener;
    }

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
        this.getSettings().setDefaultTextEncodingName("utf-8");
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

        String url;

        @Override
        public void onPageStarted(final WebView view, final String url,
                                  Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            ProgressDialogUtils.showProgressDialog(getContext());
            LogUtils.d(TAG, "onPageStarted: " + url);

            if (!url.startsWith("data:text/html"))
                this.url = url;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            LogUtils.d(TAG, "onPageFinished: " + url);
            ProgressDialogUtils.dismissProgressDialog();

            if (url.equals(RECHARGE_SUCCESS_LINK)) {
                if (webviewListener != null) webviewListener.onSuccess();
            }

        }

        @Override
        public void onReceivedError(final WebView view, final WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            ProgressDialogUtils.dismissProgressDialog();

            view.loadData(getContext().getString(R.string.webview_error), "text/html; charset=utf-8", "utf-8");

//            view.loadUrl("about:blank");
            DialogUtils.showRetryDialog(getContext(), new AlertDialogOkAndCancel.AlertDialogListener() {
                @Override
                public void onSubmit() {
                    view.loadUrl(url);
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

}
