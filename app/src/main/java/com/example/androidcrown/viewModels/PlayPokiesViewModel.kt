package com.example.androidcrown.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidcrown.models.PokiesEntity
import com.example.androidcrown.models.SlotMachineEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

class PlayPokiesViewModel: PlayFieldViewModel() {
    val pokiesEntity = PokiesEntity()

    override fun spin(callback: (() -> Unit)?) {
        spin(
            callback = callback,
            addBalanceCondition = { pokiesEntity.elementsAreEquals(it) },
            grnaaitsmeaw = { i ->
                pokiesEntity.bottomElements[i].value = pokiesEntity.middleElements[i].value
                pokiesEntity.middleElements[i].value = pokiesEntity.topElements[i].value
                var randomNum = Random.nextInt(0, 9)
                while (randomNum == pokiesEntity.topElements[i].value) {
                    randomNum = Random.nextInt(0, 9)
                }
                pokiesEntity.topElements[i].value = randomNum
            }
        )
    }
}