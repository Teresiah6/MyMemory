package com.example.android.mymemory.models

import com.example.android.mymemory.utils.DEFAUlT_ICONS

class MemoryGame(private val boardSize: BoardSize, private val customImages: List<String>?) {


    val cards: List<MemoryCard>
    var numPairsFound = 0

    private var numCardFlips =0
    private var indexOfSingleSelectedCard: Int? = null
    init {
        if (customImages == null){
            val chosenImages = DEFAUlT_ICONS.shuffled().take(boardSize.getNumPairs())
            val randomizedImages = (chosenImages + chosenImages).shuffled()
            cards = randomizedImages.map { MemoryCard(it) }
        } else{
            val randomizedImages = (customImages +customImages).shuffled()
            cards = randomizedImages.map{ MemoryCard((it.hashCode()), it) }
        }

    }

    fun flipCard(position: Int):Boolean {
        numCardFlips++
       val card = cards[position]
        //three cases
        //) cards previously filled over - flip over the card
        // 1 card previously flipped over- flip over the selected card and check if match
        //2 card previously flipped over - restire cards + flip over card at selected position
        var foundMatch = false;
        if (indexOfSingleSelectedCard == null){
            //zzero or two cards flipped over
            restoreCards()
            indexOfSingleSelectedCard = position
        } else {
            //only 1 card previously flipped over
            foundMatch = checkForMatch(indexOfSingleSelectedCard!!, position)
            indexOfSingleSelectedCard = null
        }
        card.isFaceUp =!card.isFaceUp
        return foundMatch

    }

    private fun checkForMatch(position1: Int, position2: Int): Boolean {
        if(cards[position1].identifier != cards[position2].identifier){
            return false
        }
        cards[position1].isMatched = true
        cards[position2].isMatched = true
        numPairsFound++
        return true

    }

    private fun restoreCards() {
        for (card in cards){
            if(!card.isMatched){
                card.isFaceUp = false

            }

        }
    }

    fun haveWonGame(): Boolean {
        return numPairsFound == boardSize.getNumPairs()

    }

    fun isCardFaceUp(position: Int): Boolean {
        return cards[position].isFaceUp
    }

    fun getNumMoves(): Int {
        return numCardFlips/2


    }
}