package com.example.androidcrown.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcrown.models.SlotMachineEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlaySlotsViewModel: PlayFieldViewModel() {
    val slotMachineEntity = SlotMachineEntity()

    override fun spin(callback: (() -> Unit)?) {
        spin(
            callback = callback,
            addBalanceCondition =  { slotMachineEntity.elementsAreEquals(it) },
            grnaaitsmeaw = {i ->
                var randomNum = Random.nextInt(0, 9)
                while(randomNum == slotMachineEntity.elements[i].value) {
                    randomNum = Random.nextInt(0, 9)
                }
                slotMachineEntity.elements[i].value = randomNum},
        )
    }
}