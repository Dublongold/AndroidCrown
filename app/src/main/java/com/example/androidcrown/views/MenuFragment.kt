package com.example.androidcrown.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.androidcrown.R

class MenuFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.menu_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<AppCompatButton>(R.id.privacyPolicyButton).setOnClickListener {
            findNavController().apply {
                navigate(R.id.goToPrivacy)
            }
        }
        view.findViewById<AppCompatButton>(R.id.playPokiesButton).setOnClickListener {
            findNavController().apply {
                navigate(R.id.goToPokies)
            }
        }
        view.findViewById<AppCompatButton>(R.id.playSlotsButton).setOnClickListener {
            findNavController().apply {
                navigate(R.id.goToSlots)
            }
        }
    }
}