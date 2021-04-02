package com.example.android.mymemory.models

enum class BoardSize (val numCards: Int){
    EASY (8),
    MEDIUM (18),
    HARD (24);

    fun getWith():Int{
        return when(this){
            EASY -> 2
            MEDIUM -> 3
            HARD -> 4
        }
    }
    fun getHeight():Int {
        return numCards/getWith()
    }

    fun getNumPairs():Int {
        return numCards/2
    }

}