package com.example.nklee.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
public class ContActivity extends AppCompatActivity {

    WebView mWebView;
    private TextView mTextMessage;
    private final Handler handler = new Handler();
    private Spinner spinnerLocationProvider = null;
    private String locationProvider = null;
    private Location lastKnownLocation = null;
    private LocationManager lm = null;
    private String posX = null;
    private String posY = null;

    TextView test;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    mTextMessage.setText(R.string.title_dashboard);
                    return true;
                case R.id.navigation_notifications:
                    mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content);

        test = (TextView) findViewById(R.id.test);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSet = mWebView.getSettings();   //웹뷰 설정 인스턴트화
        webSet.setJavaScriptEnabled(true);   //자바스크립트 활성화
        webSet.setUseWideViewPort(false);   //뷰 포트 활성화
        webSet.setBuiltInZoomControls(false);   //줌인 기능 비활성화
        webSet.setAllowUniversalAccessFromFileURLs(true);    //HTML 파일 로드를 위한 소스. 이 부분 대문에 젤리빈 이상만 지원
        webSet.setJavaScriptCanOpenWindowsAutomatically(true);   //window.open 메소드 사용을 위한 부분 1
        webSet.setSupportMultipleWindows(true);   //window.open 메소드 사용을 위한 부분 2
        webSet.setSaveFormData(false);   //폼 데이터 저장 안함
        webSet.setSavePassword(false);   //비번 저장 안함
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl("file:///android_asset/prd_list.html");
        mWebView.addJavascriptInterface(new MapAct(), "android");



        locationProvider = "GPS";

        // Update location to get.
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        lm.removeUpdates(locationListener);    // Stop the update if it is in progress.
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 100, locationListener);
    }

    private class MapAct {
        @JavascriptInterface
        public void myPosition() {
            handler.post(new Runnable() {
                public void run() {
                    mWebView.loadUrl("javascript:setMessage('" + posX +"','"+ posY + "')");
                }
            });
        }

        @JavascriptInterface
        public void ShopShow() {
            handler.post(new Runnable() {
                public void run() {

                }
            });
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            LocationManager lm = (LocationManager)getSystemService(Context. LOCATION_SERVICE);

            // Get the last location, and update UI.
            lastKnownLocation = location;
            posX = "" + lastKnownLocation .getLatitude();
            posY = "" + lastKnownLocation .getLongitude();

            // Stop the update to prevent changing the location.
            lm.removeUpdates( this );
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}

    };
}
