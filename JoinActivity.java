package com.example.nklee.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;


@SuppressLint({ "SetJavaScriptEnabled", "JavascriptInterface" })
public class JoinActivity extends AppCompatActivity {

    WebView mWebView;
    private TextView mTextMessage;
    private final Handler handler = new Handler();
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        //뒤로가기 버튼 삭제
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        test = (TextView)findViewById(R.id.test);
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

        mWebView.loadUrl("file:///android_asset/join.html");
        mWebView.addJavascriptInterface(new Join(), "android");

    }
    private class Join {
        @JavascriptInterface
        public void join(final String arg, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                public void run() {
                    final String email = arg;
                    final String password = arg2;
                    final String name = arg3;
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if(success){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                    builder.setMessage("회원 등록에 성공했습니다.")
                                            .setPositiveButton("확인", null)
                                            .create()
                                            .show();
                                    Intent it = new Intent(JoinActivity.this, LoginActivity.class);
                                    JoinActivity.this.startActivity(it);
                                }else{
                                    AlertDialog.Builder builder = new AlertDialog.Builder(JoinActivity.this);
                                    builder.setMessage("회원 등록에 실패했습니다.")
                                            .setNegativeButton("다시 시도", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    };
                    DBControll joinRequest = new DBControll(email, password, name, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(JoinActivity.this);
                    queue.add(joinRequest);
                }
            });
        }
    }
    //뒤로가기
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return this.onOptionsItemSelected(item);
    };
}
