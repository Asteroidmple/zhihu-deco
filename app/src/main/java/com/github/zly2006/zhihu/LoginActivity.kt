package com.github.zly2006.zhihu

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.github.zly2006.zhihu.data.AccountData
import com.github.zly2006.zhihu.ui.PREFERENCE_NAME
import com.github.zly2006.zhihu.ui.components.WebviewComp
import com.github.zly2006.zhihu.ui.components.setupUpWebviewClient
import com.github.zly2006.zhihu.util.enableEdgeToEdgeCompat
import com.github.zly2006.zhihu.util.telemetry
import kotlinx.coroutines.runBlocking

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeCompat()

        var webViewLoaded = false

        setContent {
            val scope = rememberCoroutineScope()
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            ) {
                WebviewComp(
                    modifier = Modifier.fillMaxSize(),
                    useContentHeight = false, // 登录页面使用固定全屏大小
                    onLoad = { webView ->
                        if (webViewLoaded) return@WebviewComp
                        webViewLoaded = true

                        @SuppressLint("SetJavaScriptEnabled")
                        webView.settings.javaScriptEnabled = true
                        webView.settings.domStorageEnabled = true

                        // 先调用 setupUpWebviewClient 设置基础配置
                        webView.setupUpWebviewClient()

                        // 清除所有 cookies
                        CookieManager.getInstance().removeAllCookies { }
                        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true)

                        // 设置自定义 WebViewClient
                        webView.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest,
                            ): Boolean {
                                if (request.url.toString() == "https://www.zhihu.com/") {
                                    webView.settings.userAgentString = AccountData.ANDROID_USER_AGENT
                                }
                                if (request.url?.scheme == "zhihu") {
                                    return true
                                }
                                return false
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                if (url == "https://www.zhihu.com/" || url == "https://www.zhihu.com/signin") {
                                    val cookies =
                                        CookieManager
                                            .getInstance()
                                            .getCookie("https://www.zhihu.com/")
                                            .orEmpty()
                                            .split(";")
                                            .associate {
                                                it.substringBefore("=").trim() to it.substringAfter("=")
                                            }
                                    runBlocking {
                                        if (AccountData.verifyLogin(this@LoginActivity, cookies)) {
                                            val data = AccountData.loadData(this@LoginActivity)

                                            val preferences = this@LoginActivity.getSharedPreferences(
                                                PREFERENCE_NAME,
                                                MODE_PRIVATE,
                                            )
                                            print(preferences.toString())

                                            AlertDialog
                                                .Builder(this@LoginActivity)
                                                .apply {
                                                    setTitle("登录成功")
                                                    setMessage("欢迎回来，${data.username}")
                                                    setPositiveButton("OK") { _, _ ->
                                                    }
                                                }.create()
                                                .show()
                                            AccountData.saveData(this@LoginActivity, data)
                                            telemetry(this@LoginActivity, "login")
                                            // back to the main activity
                                            this@LoginActivity.finish()
                                            return@runBlocking true
                                        } else {
                                            AlertDialog
                                                .Builder(this@LoginActivity)
                                                .apply {
                                                    setTitle("登录失败")
                                                    setMessage("请检查用户名和密码")
                                                    setPositiveButton("OK") { _, _ ->
                                                    }
                                                }.create()
                                                .show()
                                            return@runBlocking false
                                        }
                                    }
                                }
                            }
                        }
                        // 加载登录页面
                        webView.loadUrl("https://www.zhihu.com/signin")
                    },
                )
            }
        }
    }
}
