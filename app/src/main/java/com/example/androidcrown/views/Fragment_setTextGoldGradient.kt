package com.example.androidcrown.views

import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.text.TextPaint
import android.widget.TextView
import androidx.fragment.app.Fragment

fun Fragment.setTextGoldGradient(textId: Int) {
    view?.let {view ->
        val textView = view.findViewById<TextView>(textId)

        val paint: TextPaint = textView.paint
        val width = paint.measureText(textView.text.toString())

        val textShader = LinearGradient(
            0f, 0f, width, textView.textSize, intArrayOf(
                Color.parseColor("#FFC609"),
                Color.parseColor("#FFA217")
            ), null, Shader.TileMode.CLAMP
        )
        textView.paint.shader = textShader
    }
}