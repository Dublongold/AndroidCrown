package com.example.androidcrown

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.androidcrown.models.UrlContainer
import com.example.androidcrown.network.MainRetrofitClient
import com.example.androidcrown.viewModels.*
import com.example.androidcrown.views.MenuFragment
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.androidx.viewmodel.ext.android.getViewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module


class MainActivity : AppCompatActivity() {
    private lateinit var urlContainer: UrlContainer
    var endLoadingCallback: (() -> Unit)? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("Start", "Start")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)
        val w = window
        w.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        startKoin {
            modules (
                module {
                    viewModelOf(::PlayPokiesViewModel)
                    viewModelOf(::PlaySlotsViewModel)
                    viewModelOf(::LoadingViewModel)
                    single { urlContainer }
                }
            )
        }

        lifecycleScope.launch {
            val response = MainRetrofitClient.CLIENT.get()
            val body = response.string()
            if(!body.isNullOrEmpty()) {
                val body2 = body
                    .replace("pusk", "\"pusk\"")
                    .replace("link", "\"link\"")
                val pusk = Json.decodeFromString<MainRetrofitClient.Pusk>(body2)
                if(pusk.link != null) {
//                    urlContainer = UrlContainer("https://google.com")
                    urlContainer = UrlContainer(pusk.link)
                    startActivity(Intent(this@MainActivity, ActivitySecond::class.java))
                } else {
                    endLoadingCallback?.invoke()
                }
            }
        }
    }
}