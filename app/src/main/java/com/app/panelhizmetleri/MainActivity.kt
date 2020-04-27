package com.app.panelhizmetleri

import android.net.http.SslError
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.*
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val url = "https://panelhizmetleri.net"

    private val webViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (webView != null) {
                webView.loadUrl(url)
            }
            return true
        }

        override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
            handler.cancel()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView(){
        try {
            webView.settings.javaScriptEnabled = true
            webView.settings.useWideViewPort = true
            webView.settings.loadWithOverviewMode = true
            webView.settings.builtInZoomControls = true
            if (18 < Build.VERSION.SDK_INT ){
                //18 = JellyBean MR2, KITKAT=19
                webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            }
            if (Build.VERSION.SDK_INT >= 19) {
                webView.settings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            }
            webView.webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                        origin: String,
                        callback: GeolocationPermissions.Callback
                ) {
                    callback.invoke(origin, true, false)
                }

                override fun onProgressChanged(view: WebView, progress: Int) {
                    if (pBar != null) {
                        if (progress < 100) {
                            pBar.visibility = View.VISIBLE
                        }

                        pBar.progress = progress
                        if (progress == 100) {
                            pBar.visibility = View.GONE
                        }
                    }
                }
            }
            webView.webViewClient = webViewClient
            webView.loadUrl(url)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
