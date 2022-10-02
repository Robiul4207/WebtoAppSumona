package com.robiultec.webtoappsumona;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.webkit.WebSettingsCompat;
import androidx.webkit.WebViewFeature;

import android.Manifest;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;

import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    WebView webView,gameWebView;
    String USER_AGENT_ = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";

    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    LinearLayout successfullLayout,nointernerLayout,offlineGameLayout;
    Button reloadBtnId,offlineGameBtn;
    ActionBar actionBar;
    Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.fish);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        /*
        //hide actionbar
        try {
            this.getSupportActionBar().hide();
        }catch (Exception e){
            Toast.makeText(this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
         */
        /*
        // hide notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        */
        /*
        // full screen activity
        getSupportActionBar().hide();
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
         //  method 2 full screen activity
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        try {
            this.getSupportActionBar().hide();
        }catch (Exception e){
            Toast.makeText(this, "error"+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        */
        // change actionbar/statusbar color
        actionBar= getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#40442891")));
        //actionBar.setTitle("Sumon");
        // change notification bar color
        if(Build.VERSION.SDK_INT>=21){
            window=this.getWindow();
            //window.setStatusBarColor(this.getResources().getColor(R.color.notificationBar));
           window.setStatusBarColor(Color.parseColor("#0AC4C4"));

        }


        webView=findViewById(R.id.webviewId);
        gameWebView=findViewById(R.id.gameWebViewId);
        swipeRefreshLayout=findViewById(R.id.reloadId);
        progressBar=findViewById(R.id.progressbarId);
        reloadBtnId=findViewById(R.id.refreshId);
        offlineGameBtn=findViewById(R.id.offlineGame);
        successfullLayout=findViewById(R.id.webViewLinearLayout);
        nointernerLayout=findViewById(R.id.nointernetLayout);
        offlineGameLayout=findViewById(R.id.offlineGameLaypot);
       webView. getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        // new setting
        webView.getSettings().setBlockNetworkLoads (false);
        webView.getSettings().setMediaPlaybackRequiresUserGesture(false);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setFitsSystemWindows(false); // your preferences

        webView.getSettings().setUserAgentString(USER_AGENT_);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        chekConnection();

        if (Build.VERSION.SDK_INT >= 19) {
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        webView.getSettings().setUserAgentString(getString(R.string.app_name));

        ///-----------------

        webView.loadUrl(getString(R.string.web_link));
        offlineGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                webView.setVisibility(View.GONE);
                nointernerLayout.setVisibility(View.GONE);
                offlineGameLayout.setVisibility(View.VISIBLE);
                gameWebView.getSettings().setJavaScriptEnabled(true);
                gameWebView.loadUrl("file:///android_asset/index.html");
                gameWebView.requestFocus(View.FOCUS_DOWN);
            }
        });
        reloadBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chekConnection();

            }
        });
        webView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if(webView.getScrollY()==0){
                    swipeRefreshLayout.setEnabled(true);
                }else{
                    swipeRefreshLayout.setEnabled(false);

                }
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                chekConnection();
                webView.reload();
            }
        });
        //adding back button to tool bar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayShowHomeEnabled(true);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE );
                super.onPageFinished(view, url);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

//            @Override
//            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
//                super.onReceivedError(view, request, error);
//                successfullLayout.setVisibility(View.GONE);
//                nointernerLayout.setVisibility(View.VISIBLE);
//            }
        });




        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if(newProgress==100){
                    //setTitle(view.getTitle());
                    //setTitle(view.getOriginalUrl());
                    setTitle(view.getUrl());


                }else {
                    setTitle("Loading...");
                    //progressBar.setVisibility(View.VISIBLE);

                }
            }
        });

        swipeRefreshLayout.setColorSchemeColors(
                getResources().getColor(android.R.color.holo_blue_dark),
                getResources().getColor(android.R.color.holo_red_dark),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_light)

        );
        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart (String url, String userAgent, String contentDisposition, String
                    mimetype,long contentLength){

                //Checking runtime permission for devices above Marshmallow.
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {

                        downloadDialog(url, userAgent, contentDisposition, mimetype);

                    } else {


                        //requesting permissions.
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

                    }
                } else {
                    //Code for devices below API 23 or Marshmallow

                    downloadDialog(url, userAgent, contentDisposition, mimetype);

                }

            }
        });

    }// on create End


    private void chekConnection() {
        ConnectivityManager manager= (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info= manager.getActiveNetworkInfo();
        if(info !=null){
            if(info.isConnected()){
                successfullLayout.setVisibility(View.VISIBLE);
                nointernerLayout.setVisibility(View.GONE);
                webView.reload();

            }else{
                successfullLayout.setVisibility(View.GONE);
                nointernerLayout.setVisibility(View.VISIBLE);

            }

        }else{
            successfullLayout.setVisibility(View.GONE);
            nointernerLayout.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menutwo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {

//            case R.id.refresh:
//                webview.reload();
//                Toast.makeText(MainActivity.this, "history Done", Toast.LENGTH_SHORT).show();
//                break;
//
//            case R.id.history:
//                Intent his = new Intent(MainActivity.this, HistoryActivity.class);
//                startActivity(his);
//                break;
//
//            case R.id.setting:
//                Intent intent = new Intent(MainActivity.this, BrowserSettings.class);
//                startActivity(intent);
//                break;
//
//            case R.id.favouritestar:
//                Intent book = new Intent(MainActivity.this, Bookmarks.class);
//                startActivity(book);
//                break;
//
//            case R.id.download:
//                Intent download = new Intent(MainActivity.this, DownloadWithPauseResmueNew.class);
//                startActivity(download);
//                break;

//

            case R.id.googleId:
                domainName("http://www.google.com/");
                break; 
                
            case R.id.facebookId:
                domainName("http://www.facebook.com/");
                break; 
                case R.id.youtubeId:
                domainName("http://www.youtube.com/");
                break;
                
            case R.id.desktopMode:
                setDesktopmode(webView, true);
                break;
            case R.id.mobileMode:
                setDesktopmode(webView, false);
                break;

            case R.id.landScapeId:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                break;
            case R.id.portraitModeId:
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                break;
            case R.id.ScanqrCode:
                Intent i = new Intent(MainActivity.this, ScanQrCode.class);
                startActivity(i);
                break;
            case R.id.qrCode:
                Intent qrcode = new Intent(MainActivity.this, ScanQrCode2.class);
                startActivity(qrcode);
                break;

             case R.id.lightId:
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(webView.getSettings(),WebSettingsCompat.FORCE_DARK_OFF);
                }
                break;
             case R.id.darkId:
                if(WebViewFeature.isFeatureSupported(WebViewFeature.FORCE_DARK)){
                    WebSettingsCompat.setForceDark(webView.getSettings(),WebSettingsCompat.FORCE_DARK_ON);
                }
                break;




            case R.id.ExitWebBrowser:
                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(R.drawable.ic_round_exit)
                        .setTitle("Exit")
                        .setMessage("Are You Want to Exit This App")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                finish();
                            }
                        }).setNeutralButton("Help", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Pres Yes to exit this app", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
                break;

            case R.id.aboutdeveloper:
                Intent about = new Intent(MainActivity.this, AboutDeveloper.class);
                startActivity(about);
                break;
        }
        return true;
    }

    private void domainName(String s) {
        Intent intent= new Intent(getApplicationContext(),MainActivity2.class);
        intent.putExtra("domain",s);
        startActivity(intent);
    }

    private void setDesktopmode(WebView webView, boolean b) {
        String newUserAgent = webView.getSettings().getUserAgentString();
        if (b) {
            try {
                String ua = webView.getSettings().getUserAgentString();
                String androidDoString = webView.getSettings().getUserAgentString()
                        .substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView.getSettings().getUserAgentString()
                        .replace(androidDoString, "(x11; Linux x86_64)");

            } catch (Exception e) {
                e.printStackTrace();

            }
        } else {
            newUserAgent = null;
        }
        webView.getSettings().setUserAgentString(newUserAgent);
        webView.getSettings().setUseWideViewPort(b);
        webView.getSettings().setLoadWithOverviewMode(b);
        webView.reload();
    }
//----- download dialog
public void downloadDialog(final String url, final String userAgent, String contentDisposition, String mimetype) {
    //getting filename from url.
    final String filename = URLUtil.guessFileName(url, contentDisposition, mimetype);
    //alertdialog
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    //title of alertdialog
    builder.setTitle("Downloading");
    //message of alertdialog
    builder.setMessage("We are trying to download: "+ ' ' + filename);
    //if Yes button clicks.

    builder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //DownloadManager.Request created with url.

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            //cookie
            String cookie = CookieManager.getInstance().getCookie(url);
            //Add cookie and User-Agent to request
            request.addRequestHeader("Cookie", cookie);
            request.addRequestHeader("User-Agent", userAgent);
            //file scanned by MediaScannar
            request.allowScanningByMediaScanner();
            //Download is visible and its progress, after completion too.
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            //DownloadManager created
            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            //Saving files in Download folder
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename);
            //download enqued
            downloadManager.enqueue(request);


            BroadcastReceiver onComplete=new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                    Toast.makeText(getApplicationContext(), "Download Complete", Toast.LENGTH_SHORT).show();
                }
            };

            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        }
    });
    builder.setNegativeButton("NOT NOW", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            //cancel the dialog if Cancel clicks
            dialog.cancel();
            webView.goBack();
        }

    });
    //alertdialog shows.
    builder.show();

}

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            //finish();
            AlertDialog.Builder builder= new AlertDialog.Builder(this);
            builder.create();
            builder.setTitle("Confirmation");
            builder.setMessage("Dou you want to exit ?");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        }
    }


}