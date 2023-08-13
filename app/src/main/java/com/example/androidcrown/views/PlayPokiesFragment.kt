package com.example.androidcrown.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.androidcrown.R
import com.example.androidcrown.viewModels.PlayPokiesViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PlayPokiesFragment: PlayFieldFragment() {

    override val viewModel: PlayPokiesViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_pokies_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slots: List<ImageView> = listOf(
            view.findViewById(R.id.slot1), view.findViewById(R.id.slot2),
            view.findViewById(R.id.slot3), view.findViewById(R.id.slot4),
            view.findViewById(R.id.slot5), view.findViewById(R.id.slot6),
            view.findViewById(R.id.slot7), view.findViewById(R.id.slot8),
            view.findViewById(R.id.slot9))

        val slotImages = listOf(
            R.drawable.poky_1, R.drawable.poky_2, R.drawable.poky_3,
            R.drawable.poky_4, R.drawable.poky_5, R.drawable.poky_6,
            R.drawable.poky_7, R.drawable.poky_8, R.drawable.poky_9
        )
        for(i in 0..8) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.pokiesEntity[i].collect {
                        slots[i].setImageResource(slotImages[it])
                    }
                }
            }
        }
    }
}