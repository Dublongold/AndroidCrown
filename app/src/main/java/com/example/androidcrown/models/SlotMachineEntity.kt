package com.example.androidcrown.models

import kotlinx.coroutines.flow.MutableStateFlow

class SlotMachineEntity {
    val elements = List(3) {MutableStateFlow(0)}

    fun elementsAreEquals(bet: Int): Int {
        return if(elements.all {it.value == elements.first().value}) {
            return bet * (1 + elements.first().value)
        }
        else {
            0
        }
    }
}