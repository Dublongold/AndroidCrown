package com.example.androidcrown.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.androidcrown.MainActivity
import com.example.androidcrown.R
import com.example.androidcrown.viewModels.LoadingViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoadingFragment: Fragment() {

    private var loadingView: ImageView? = null
    private val viewModel: LoadingViewModel by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loading_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Loading", "Loading")
        loadingView = view.findViewById(R.id.loadingImage)
        viewLifecycleOwner.lifecycleScope.launch {
            while(!isDetached) {
                val loadingView = loadingView
                if(loadingView != null) {
                    loadingView.rotation = if(loadingView.rotation < 360) loadingView.rotation + 45f else 45f
                }
                delay(250)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.loadingState.collect {
                    if(it) {
                        Log.i("Loading", "navigate")
                        findNavController().run {
                            popBackStack()
                            navigate(R.id.goToMenuAction)
                        }
                    }
                }
            }
        }
        (activity as MainActivity).endLoadingCallback = {
            viewModel.loaded()
        }
    }
}