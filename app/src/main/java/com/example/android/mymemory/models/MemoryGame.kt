package com.example.android.mymemory.models

import com.example.android.mymemory.utils.DEFAUlT_ICONS

class MemoryGame (private val boardSize: BoardSize) {


    val cards: List<MemoryCard>
    val numPairsFound = 0

    init {
        val chosenImages = DEFAUlT_ICONS.shuffled().take(boardSize.getNumPairs())
        val randomizedImages = (chosenImages + chosenImages).shuffled()
        cards = randomizedImages.map { MemoryCard(it) }
    }

    fun flipCard(position: Int) {
       val card = cards[position]
        card.isFaceUp =!card.isFaceUp

    }
}