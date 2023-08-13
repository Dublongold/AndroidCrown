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
import com.example.androidcrown.viewModels.PlayFieldViewModel
import com.example.androidcrown.viewModels.PlaySlotsViewModel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class PlayFieldFragment: Fragment() {
    protected abstract val viewModel: PlayFieldViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Set go back button
        view.findViewById<AppCompatButton>(R.id.goToMenuButton).setOnClickListener {
            findNavController().popBackStack()
        }
        // Find info elements
        val betInfo = view.findViewById<TextView>(R.id.betInfo)
        val balanceInfo = view.findViewById<TextView>(R.id.balanceInfo)
        val autoSpinInfo = view.findViewById<TextView>(R.id.autoSpinInfo)
        // Set gold text for info elements.
        setTextGoldGradient(betInfo.id)
        setTextGoldGradient(balanceInfo.id)
        setTextGoldGradient(autoSpinInfo.id)
        // Set event listeners for a minus and add buttons.
        view.findViewById<AppCompatButton>(R.id.minusButton).setOnClickListener {
            viewModel.minusBet()
        }
        view.findViewById<AppCompatButton>(R.id.plusButton).setOnClickListener {
            viewModel.addBet()
        }
        // Watch balance
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.balance.collect {
                    balanceInfo.text = getString(R.string.balance_count, it)
                }
            }
        }
        // Watch bet.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.bet.collect {
                    betInfo.text = getString(R.string.bet_count, it)
                }
            }
        }
        // Watch auto spin count.
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.autoSpinCount.collect {
                    autoSpinInfo.text = when (it) {
                        -1 -> {
                            view.findViewById<AppCompatButton>(R.id.stopAutoSpinButton).isEnabled = false
                            view.findViewById<AppCompatButton>(R.id.startAutoSpinButton).isEnabled = true
                            getString(R.string.autospin_text, "DISABLED")
                        }
                        0 -> {
                            getString(R.string.autospin_text, "THE LAST")
                        }
                        else -> {
                            getString(R.string.autospin_text, "$it TIMES")
                        }
                    }
                }
            }
        }
        // Set event listeners for a stop and start auto spin buttons
        view.findViewById<AppCompatButton>(R.id.startAutoSpinButton).setOnClickListener {
            viewModel.startAutoSpin()
            it.isEnabled = false
            view.findViewById<AppCompatButton>(R.id.stopAutoSpinButton).isEnabled = true
        }
        view.findViewById<AppCompatButton>(R.id.stopAutoSpinButton).setOnClickListener {
            viewModel.stopAutoSpin()
            it.isEnabled = false
            view.findViewById<AppCompatButton>(R.id.startAutoSpinButton).isEnabled = true
        }
        // Set event listener for spin button.
        view.findViewById<AppCompatButton>(R.id.spinButton).setOnClickListener {
            viewModel.spin {
                it.performClick()
            }
        }
    }
}