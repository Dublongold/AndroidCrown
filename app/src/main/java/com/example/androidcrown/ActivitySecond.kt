package com.example.androidcrown

import android.Manifest;
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.KeyEvent
import android.webkit.CookieManager
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrown.models.UrlContainer
import org.koin.android.ext.android.inject
import java.io.File
import java.io.IOException

class ActivitySecond: AppCompatActivity() {
    private val urlContainer:UrlContainer by inject()

    var mainWebView: WebView? = null
    var mainFilePathCallback: ValueCallback<Array<Uri>>? = null
    var calback: Uri? = null

    private var lerfi: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        mainWebView = findViewById(R.id.mainWebView)
        lerfi = urlContainer.url
        configureWebView()
        onBackPressedDispatcher.addCallback {
            if(mainWebView!!.canGoBack()) {
                mainWebView!!.goBack()
            }
        }
    }

    private fun configureWebView() {
        configureSetting()
        CookieManager.getInstance().run {
            setAcceptCookie(true)
            setAcceptThirdPartyCookies(mainWebView, true)
        }

        mainWebView!!.webChromeClient = object : WebChromeClient() {
            override fun onShowFileChooser(
                webView: WebView,
                filePathCallback: ValueCallback<Array<Uri>>,
                fileChooserParams: FileChooserParams
            ): Boolean {
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                mainFilePathCallback = filePathCallback
                return true
            }
        }
        mainWebView!!.webViewClient = MainWebViewClient()
        mainWebView!!.loadUrl(lerfi!!)
    }

    private fun configureSetting() {
        mainWebView!!.settings.run {
            allowContentAccess = true; allowFileAccess = true
            javaScriptCanOpenWindowsAutomatically = true; allowFileAccessFromFileURLs = true
            mixedContentMode = 0; cacheMode = WebSettings.LOAD_DEFAULT
            domStorageEnabled = true; javaScriptEnabled = true
            databaseEnabled = true; allowUniversalAccessFromFileURLs = true
            useWideViewPort = true; loadWithOverviewMode = true
            userAgentString = mainWebView!!.settings.userAgentString.replace("; wv", "")
        }
    }

    inner class MainWebViewClient : WebViewClient() {
        private var content: Boolean? = null
        private var method: String? = null
        override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
            val uri = request.url.toString()
            return if (uri.indexOf("/") != -1) {
                if (uri.indexOf("intent://ti/p/") != -1 && uri.indexOf("#") != -1) {
                    var newUr = "line://ti/p/@"
                    newUr += uri.split("@".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1].split("#Inten".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[0]
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(newUr)))
                    true
                } else {
                    content = !uri.contains("http")
                    if (!content!!) {
                        content!!
                    } else {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                        startActivity(intent)
                        true
                    }
                }
            } else true
        }

        override fun onReceivedLoginRequest(
            view: WebView,
            realm: String,
            account: String?,
            args: String
        ) {
            method = "OnReceivedLoginReq"
            super.onReceivedLoginRequest(view, realm, account, args)
        }
    }

    val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean? ->
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null
        try {
            photoFile = File.createTempFile("file", ".jpg", getExternalFilesDir(Environment.DIRECTORY_PICTURES))
        } catch (_: IOException) {}
        takePictureIntent.putExtra(
            MediaStore.EXTRA_OUTPUT,
            Uri.fromFile(photoFile)
        )
        calback = Uri.fromFile(photoFile)
        val old = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE); type = "*/*"
        }
        val intentArray = arrayOf(takePictureIntent)
        val chooserIntent = Intent(Intent.ACTION_CHOOSER).apply {
            putExtra(Intent.EXTRA_INTENT, old)
            putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray)
        }
        startActivityForResult(chooserIntent, 1)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (mainFilePathCallback == null) return
        if (resultCode == -1) {
            if (data != null) {
                val d = data.dataString
                if (d != null) {
                    val u = Uri.parse(d)
                    mainFilePathCallback!!.onReceiveValue(arrayOf(u))
                } else {
                    if (calback != null) {
                        mainFilePathCallback!!.onReceiveValue(arrayOf(calback!!))
                    } else {
                        mainFilePathCallback!!.onReceiveValue(null)
                    }
                }
            } else {
                if (calback != null) {
                    mainFilePathCallback!!.onReceiveValue(arrayOf(calback!!))
                } else {
                    mainFilePathCallback!!.onReceiveValue(null)
                }
            }
        } else {
            mainFilePathCallback!!.onReceiveValue(null)
        }
        mainFilePathCallback = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mainWebView!!.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        mainWebView!!.restoreState(savedInstanceState)
    }
}