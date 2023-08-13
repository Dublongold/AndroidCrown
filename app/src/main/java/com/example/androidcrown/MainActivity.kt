package com.example.androidcrown

import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcrown.viewModels.PlayPokiesViewModel
import com.example.androidcrown.viewModels.PlaySlotsViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.module


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
                }
            )
        }
    }
}