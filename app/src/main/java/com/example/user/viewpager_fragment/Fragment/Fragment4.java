package com.example.user.viewpager_fragment.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.support.v4.app.Fragment;
import com.example.user.viewpager_fragment.R;
import com.google.android.gms.maps.MapView;

/**
 * Created by user on 2017-11-01.
 */
//따릉이홈페이지
public class Fragment4 extends Fragment {
    private WebView mWebView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_fragment4, container, false);

        if (mWebView != null) {
            mWebView.destroy();
        }
        mWebView = (WebView) layout.findViewById(R.id.WebSite);

        setWebView1();
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
    public void setWebView1() {
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("https://www.bikeseoul.com/app/station/moveStationRealtimeStatus.do");

//        http://ec2-13-124-98-123.ap-northeast-2.compute.amazonaws.com:8080/signup
//        mWebView.getSettings().setDefaultFontSize(8);
//        mWebView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
//        mWebView.setInitialScale(70);
//        mWebView.getSettings().setUseWideViewPort(true);


    }



}


