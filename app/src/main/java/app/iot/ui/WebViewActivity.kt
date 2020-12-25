package app.iot.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import app.iot.AppConstant.URL
import app.iot.BR
import app.iot.R
import app.iot.common.util.AppInfoUtils
import app.iot.net.RetrofitProvider
import app.iot.token
import app.iot.viewmodel.WebViewViewModel
import kotlinx.android.synthetic.main.activity_web_view.*


class WebViewActivity : BaseActivity<WebViewViewModel>() {
    override fun getLayoutId(): Int {
        return R.layout.activity_web_view
    }

    override fun initView() {
        mBinding.setVariable(BR.data, mViewModel?.titleViewModel)
        mViewModel?.titleViewModel?.context = mBinding.root.context

        webView.webChromeClient = object : WebChromeClient() {
            override fun onReceivedTitle(view: WebView, title: String) {
                super.onReceivedTitle(view, title)
                mViewModel?.titleViewModel?.title?.value = title
            }
        }

        webView.webViewClient = object : WebViewClient() {
            /**
             * 防止加载网页时调起系统浏览器
             */
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

                if (url.isNullOrEmpty()) {
                    return false
                }

                try {
                    val uri = Uri.parse(url)
                    if (uri.scheme != "http" && uri.scheme != "https") {
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                        return true
                    }

                    uri.lastPathSegment?.let {
                        if (it.contains(".apk")) {
                            val intent = Intent(Intent.ACTION_VIEW, uri)
                            startActivity(intent)
                            return true
                        }
                    }
                } catch (ignore: Exception) {
                }

                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                progressBar.visibility = View.GONE
            }

        }

        val webSetting = webView.settings
        webSetting.javaScriptEnabled = true
        webSetting.javaScriptCanOpenWindowsAutomatically = true

        webSetting.allowFileAccess = true
        webSetting.allowContentAccess = true
        webSetting.allowUniversalAccessFromFileURLs = true
        webSetting.allowFileAccessFromFileURLs = true

        webSetting.cacheMode = WebSettings.LOAD_DEFAULT
        webSetting.layoutAlgorithm = WebSettings.LayoutAlgorithm.NARROW_COLUMNS
        webSetting.setSupportZoom(false)
        webSetting.builtInZoomControls = false
        webSetting.useWideViewPort = true
        webSetting.setSupportMultipleWindows(false)
        webSetting.loadWithOverviewMode = true
        webSetting.setAppCacheEnabled(true)
        webSetting.domStorageEnabled = true
        webSetting.textZoom = 100
        webSetting.setGeolocationEnabled(true)

        webSetting.displayZoomControls = false// api-11

        webSetting.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE

        //网页弹出菜单
        //registerForContextMenu(webView)

        if (intent.getStringExtra(URL).isNullOrEmpty()) {
            finish()
        } else {
            intent.getStringExtra(URL)?.let {
                val header = HashMap<String, String>()
                header[RetrofitProvider.SingletonHolder.auth] = token
                header["deviceId"] = AppInfoUtils.getDeviceId()
                header["platform"] = RetrofitProvider.SingletonHolder.client
                webView.loadUrl(it, header)
            }
        }
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            finish()
        }
    }
}

