package com.example.androidcrown.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

abstract class PlayFieldViewModel: ViewModel() {

    private var spinRightNow = false

    private val _balance = MutableStateFlow(10000)
    val balance: StateFlow<Int>
        get() = _balance

    private val _bet = MutableStateFlow(10)
    val bet: StateFlow<Int>
        get() = _bet

    private val _autoSpinCount = MutableStateFlow(-1)
    val autoSpinCount: StateFlow<Int>
        get() = _autoSpinCount

    private fun addBalance(value: Int) {
        _balance.value += value
    }

    fun startAutoSpin() {
        _autoSpinCount.update { 10 }
    }

    fun stopAutoSpin() {
        _autoSpinCount.update { -1 }
    }

    protected fun spin(
        callback: (() -> Unit)? = null,
        addBalanceCondition: (Int) -> Int,
        /** Get random number and assign it to slotMachine element and wait*/
        grnaaitsmeaw: (Int) -> Unit
    ) {
        if(spinRightNow) return
        spinRightNow = true
        if (_balance.value - _bet.value >= 0) {
            Log.i("Spin", "Start")
            _balance.value -= _bet.value

            val stopSpins = arrayOf(false, false, false)

            viewModelScope.launch {
                Log.i("Spin(2)", "Start wait coroutine.")
                delay(Random.nextLong(4000, 7000))
                Log.i("Spin(2)", "Stop first column.")
                stopSpins[0] = true
                delay(if(_autoSpinCount.value != -1) 100 else 500)
                Log.i("Spin(2)", "Stop second column.")
                stopSpins[1] = true
                delay(if(_autoSpinCount.value != -1) 200 else 1000)
                Log.i("Spin(2)", "Stop second column.")
                stopSpins[2] = true
            }
            viewModelScope.launch {
                Log.i("Spin(1)", "Start main coroutine.")
                val currentBet = _bet.value

                if(stopSpins.any { !it }) {
                    Log.i("Spin(1)", "Visible spin start.")
                }
                while (stopSpins.any { !it }) {
                    stopSpins.forEachIndexed { ind, elem -> if(!elem) grnaaitsmeaw(ind) }
                    delay(100)
                }
                val win = addBalanceCondition(currentBet)
                Log.i("Spin(1)", "Balance increased ($win)")
                addBalance(win)
                if (_autoSpinCount.value != -1) {
                    _autoSpinCount.value--
                    Log.i("Spin(1)", "Auto spin (${_autoSpinCount.value} left)!")
                    delay(250)
                }
                Log.i("Spin(1)", "Spin right now stopped.")
                spinRightNow = false
                if (_autoSpinCount.value != -1) {
                    callback?.invoke()
                }
            }
        }
    }

    abstract fun spin( callback: (() -> Unit)? = null )

    fun addBet() {
        if(_bet.value + 10 <= _balance.value) {
            _bet.update { it + 10 }
        }
    }

    fun minusBet() {
        if(_bet.value >= 20) {
            _bet.update { it - 10 }
        }
    }

}