package com.pakkerth.webview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebViewActivity extends AppCompatActivity {
    private WebView WebViw;
    private static final int FILECHOOSER_RESULTCODE   = 2888;
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private Fragment contentFragment;
    private ProgressDialog mProgressDialog;

    double lat = 0;
    double lng = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebViw = (WebView)findViewById(R.id.web_view);



        String url = "https://pakkerth.wordpress.com";
       setWebViw(url);


    }

    public void setWebViw(String url) {



      //  Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebViw.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            WebViw.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        //webView.getSettings().setUseWideViewPort(true);

        //Other webview settings
        WebViw.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        WebViw.setScrollbarFadingEnabled(false);
        WebViw.getSettings().setBuiltInZoomControls(true);
        //WebViw.getSettings().setAllowFileAccess(true);
        WebViw.getSettings().setSupportZoom(false);
        WebViw.getSettings().setDisplayZoomControls(false);
        WebViw.getSettings().setJavaScriptEnabled(true);
        //WebViw.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        //  WebViw.getSettings().setLoadWithOverviewMode(true);
        WebViw.getSettings().setUseWideViewPort(true);
        //WebViw.getSettings().setPluginState(WebSettings.PluginState.ON);

        WebViw.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        WebViw.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        WebViw.loadUrl(url);

        mProgressDialog = ProgressDialog.show(WebViewActivity.this, "Downloading","Please wait ...");
        mProgressDialog.show();
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);

        WebViw.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                view.loadUrl(url); //
                return true;
            }
            public void onPageFinished(WebView view, String url) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        if(requestCode==FILECHOOSER_RESULTCODE)
        {

            if (null == this.mUploadMessage) {
                return;

            }

            Uri result=null;

            try{
                if (resultCode != RESULT_OK) {

                    result = null;

                } else {

                    // retrieve from the private variable if the intent is null
                    result = intent == null ? mCapturedImageURI : intent.getData();
                }
            }
            catch(Exception e)
            {
                Toast.makeText(getApplicationContext(), "activity :"+e,
                        Toast.LENGTH_LONG).show();
            }

            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;

        }

    }

    // Open previous opened link from history on webview when back button pressed

    @Override
    // Detect when the back button is pressed
    public void onBackPressed() {

        if(WebViw.canGoBack()) {

            WebViw.goBack();

        } else {
            // Let the system handle the back button
            super.onBackPressed();
        }
    }

}
