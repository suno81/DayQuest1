package com.example.nklee.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class LoginActivity extends AppCompatActivity {

    WebView mWebView;
    private TextView mTextMessage;
    private final Handler handler = new Handler();
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();

        test = (TextView)findViewById(R.id.test);
        mWebView = (WebView) findViewById(R.id.webview);
        WevViewInitial(mWebView);



        Button getTokenButton = (Button) findViewById(R.id.checkTokenButton);
        getTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void WevViewInitial(WebView mWebView){
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

        mWebView.loadUrl("file:///android_asset/index.html");
        mWebView.addJavascriptInterface(new Login(), "android");
    }

    private class Login {
        @JavascriptInterface
        public void login(final String arg, final String arg2) {
            handler.post(new Runnable() {
                public void run() {
                    final String email = arg;       //html(WebView)로 부터 받은값
                    final String password = arg2;   //html(WebView)로 부터 받은값
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);     //WebView로 부터 받은 값 저장
                                boolean success = jsonResponse.getBoolean("success"); //php로 부터 받은 success변수의 boolean값 호출

                                if(success){
                                    String email = jsonResponse.getString("email");
                                    String password = jsonResponse.getString("password");
                                    Intent it = new Intent(LoginActivity.this, ContActivity.class);
                                    it.putExtra("email", email);
                                    it.putExtra("password", password);
                                    LoginActivity.this.startActivity(it);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                    builder.setMessage("로그인에 실패하였습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    DBControll loginRequest = new DBControll(email, password, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);
                }
            });
        }

        @JavascriptInterface
        public void joinIntent() {
            handler.post(new Runnable() {
                public void run() {
                    Intent it = new Intent(LoginActivity.this, JoinActivity.class);
                    LoginActivity.this.startActivity(it);
                }
            });
        }

    }
}
