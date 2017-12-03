package com.example.user.viewpager_fragment.Fragment;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.user.viewpager_fragment.R;

/**
 * Created by user on 2017-11-01.
 */
//로그인페이지
public class Fragment5 extends Fragment {
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_fragment5, container, false);

        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = (WebView) layout.findViewById(R.id.loginPage);

        setWebView2();
        return layout;
    }
    //
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()){
//            mWebView.goBack();
//            return true;
//        }
//        return onKeyDown(keyCode, event);
//    }
//
    public void setWebView2() {
        mWebView.getSettings().setSupportMultipleWindows(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("https://www.bikeseoul.com/login.do");
    }

}
