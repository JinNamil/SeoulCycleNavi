package com.example.user.viewpager_fragment.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.user.viewpager_fragment.R;

/**
 * Created by user on 2017-11-14.
 */

public class LoginActivity extends Activity{
    private String id;
    private WebView mWebView;
    private final Handler handler = new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        mWebView = (WebView)findViewById(R.id.LoginSite);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("http://ec2-13-124-98-123.ap-northeast-2.compute.amazonaws.com:8080/login");
        mWebView.setWebViewClient(new WebViewClientClass());
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);  //자바스크립트 허용
        mWebView.setWebChromeClient(new WebChromeClient());//크롬
        mWebView.addJavascriptInterface(new JSInterface(this), "JSInterface");
        mWebView.getSettings().setDefaultFontSize(8);
        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
        mWebView.setInitialScale(70);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setSupportZoom(true);

    }


    //액티비티 이동 메소드

    public  void intent_temp()
    {
        Intent intent= new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    public class JSInterface {
        Context mContext;

        JSInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void toastMe(String text) {
            text=id;
     //       Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
            intent_temp();
        }

    }
    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
