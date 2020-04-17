package com.example.autoclick;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import java.net.MalformedURLException;
import java.net.URL;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity{
    private WebView mwebView;
    private URL link;
    ProgressDialog progressDialog;

    int error=0;
    int clicked=0;
    private Object MainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Toast.makeText(MainActivity.this, "Running...", Toast.LENGTH_LONG).show();


        //for background
        Intent i = new Intent();
        i.setAction(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        this.startActivity(i);


        this.Loadweb();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    public void Loadweb()
    {
        mwebView =findViewById(R.id.webview);
        WebSettings webSettings = mwebView.getSettings();
        mwebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mwebView.getSettings().setJavaScriptEnabled(true);
        mwebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mwebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        mwebView.getSettings().setAppCacheEnabled(true);
        mwebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        webSettings.setUseWideViewPort(true);
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setEnableSmoothTransition(true);
        try
        {
            link= new URL("http://bept.herokuapp.com/login");
            mwebView.loadUrl(String.valueOf(link));
        }
        catch (MalformedURLException e)
        {
            Toast.makeText(getApplicationContext(), "Exception Occured", Toast.LENGTH_SHORT).show();
            notificationfinishfunction("Exception Occured");
            e.printStackTrace();
        }

        mwebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        mwebView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                mwebView.loadUrl("about:blank");

                Toast.makeText(getApplicationContext(), "Webpage Not Found / No Internet Connection", Toast.LENGTH_SHORT).show();
                notificationfinishfunction("Webpage Not Found / No Internet Connection");

                mwebView.clearCache(true);
                finish();
            }
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mwebView.setVisibility(VISIBLE);
            }

            @Override
            public void onReceivedHttpError (WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                    if(errorResponse.getStatusCode() == 404)
                    {
                        Toast.makeText(view.getContext(), "Page Not Found", Toast.LENGTH_LONG).show();
                        notificationfinishfunction("Page Not Found");

                        mwebView.clearCache(true);
                        finish();
                    }

                }
            }


            @Override
            public void onPageFinished(WebView view, String url)
            {
                if( mwebView.getUrl().equals("https://bept.herokuapp.com/login") || mwebView.getUrl().equals("http://bept.herokuapp.com/login"))
                {
                    error++;

                    if(error==1)
                    {
                        mwebView.loadUrl
                                (

                                        "javascript:(function() { " +
                                                "document.getElementById('email').value='mruduladdipalli@gmail.com';"+
                                                "document.getElementById('password').value='Ab123123';"+
                                                //"document.getElementById('submitbtn').click();"+
                                                "document.getElementById('loginform').submit();"+
                                                "})()"

                                );

                        Toast.makeText(getApplicationContext(), "Validating", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {
                        error=0;
                        //send noty validation error
                        Toast.makeText(getApplicationContext(), "Validation Error", Toast.LENGTH_SHORT).show();
                        noty("Error Occured - Validation Error");
                        mwebView.clearCache(true);
                        finish();
                    }

                }

                if(mwebView.getUrl().equals("http://bept.herokuapp.com/logout"))
                {
                    Toast.makeText(getApplicationContext(), "Logging Out Please Wait...", Toast.LENGTH_SHORT).show();
                    recreate();
                    //or

                    mwebView.clearCache(true);
                    finish();
                }


                if(mwebView.getUrl().equals("http://bept.herokuapp.com/home"))
                {

                    //button clicked javascript here
                    clicked++;

                    if(clicked==1)
                    {
                        mwebView.loadUrl
                                (

                                        "javascript:(function() { " +
                                                "document.getElementById('homeanchor').click();"+
                                                "})()"

                                );

                    }
                    else
                    {
                        clicked=0;

                        //send noty button click successfull
                        Toast.makeText(getApplicationContext(), "Button Click Successful", Toast.LENGTH_SHORT).show();
                        noty("Button Click Successful");

                        //clear cache from session cookies
                        CookieSyncManager cookieSyncMngr = CookieSyncManager.createInstance(MainActivity.this);
                        CookieManager cookieManager = CookieManager.getInstance();
                        cookieManager.removeAllCookie();

                        mwebView.clearCache(true);
                        finish();

                    }
                }

            }
        });
    }


    public void notificationfinishfunction (String msg)
    {
        noty(msg);
        mwebView.clearCache(true);
        this.finish();

    }

    public void noty (String msg)
    {
        NotificationHelper NH = new NotificationHelper(this);
        NH.createNotification(msg,"");

    }


    @Override
    public void onBackPressed() {
        if (mwebView.canGoBack()) {
            mwebView.goBack();
        }
        else {

            mwebView.clearCache(true);
            finish();//closing app
        }
    }
}
