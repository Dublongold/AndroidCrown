package com.example.androidcrown.models

import kotlinx.coroutines.flow.MutableStateFlow

class PokiesEntity {
    val topElements = List(3) {MutableStateFlow(0)}
    val middleElements = List(3) {MutableStateFlow(0)}
    val bottomElements = List(3) {MutableStateFlow(0)}

    operator fun get(ind: Int): MutableStateFlow<Int> {
        return when(ind) {
            0, 3, 6 -> topElements[ind/3]
            1, 4, 7 -> middleElements[ind/3]
            2, 5, 8 -> bottomElements[ind/3]
            else -> throw IndexOutOfBoundsException()
        }
    }
    fun elementsAreEquals(bat: Int): Int {
        val checkFirst = if(topElements.all { it.value == topElements[0].value }) bat * (1 + topElements[0].value) else 0
        val checkSecond = if(middleElements.all { it.value == middleElements[0].value }) bat * (1 + middleElements[0].value) else 0
        val checkThird = if(bottomElements.all { it.value == bottomElements[0].value }) bat * (1 + bottomElements[0].value) else 0
        return checkFirst + checkSecond + checkThird
    }
}