package com.example.android.mymemory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymemory.models.BoardSize
import com.example.android.mymemory.models.MemoryCard
import com.example.android.mymemory.models.MemoryGame
import com.example.android.mymemory.utils.DEFAUlT_ICONS

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter

    //not created at time of construction
    private lateinit var  rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var  tvNumPairs: TextView

    private var boardSize :BoardSize =BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)

        memoryGame = MemoryGame(boardSize)


        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object:MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
              //  Log.i(TAG, "Card clicked $position")
                updateGameWithflip (position)

            }
        })
        rvBoard.adapter = adapter
            rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager (this,boardSize.getWith())
    }

    private fun updateGameWithflip(position: Int) {
        memoryGame.flipCard(position)
        adapter.notifyDataSetChanged()
    }
}