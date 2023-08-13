package com.example.androidcrown.views

import android.media.Image
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
import com.example.androidcrown.viewModels.PlaySlotsViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class PlaySlotsFragment: PlayFieldFragment() {

    override val viewModel: PlaySlotsViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.play_slots_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val slots: List<ImageView> = listOf(
            view.findViewById(R.id.slotMachine1),
            view.findViewById(R.id.slotMachine2),
            view.findViewById(R.id.slotMachine3),
        )

        val slotImages: List<Int> = listOf(
            R.drawable.slots_element_1, R.drawable.slots_element_2, R.drawable.slots_element_3,
            R.drawable.slots_element_4, R.drawable.slots_element_5, R.drawable.slots_element_6,
            R.drawable.slots_element_7, R.drawable.slots_element_8, R.drawable.slots_element_9)

        for(i in slots.indices) {
            viewLifecycleOwner.lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.slotMachineEntity.elements[i].collect {
                        slots[i].setImageResource(slotImages[it])
                    }
                }
            }
        }
    }
}