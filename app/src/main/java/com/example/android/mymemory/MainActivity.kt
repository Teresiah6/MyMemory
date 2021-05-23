package com.example.android.mymemory

import android.animation.ArgbEvaluator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.android.mymemory.models.BoardSize
import com.example.android.mymemory.models.MemoryGame
import com.example.android.mymemory.utils.EXTRA_BOARD_SIZE
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
        private const val CREATE_REQUEST_CODE =248
    }

    private lateinit var memoryGame: MemoryGame
    private lateinit var adapter: MemoryBoardAdapter

    //not created at time of construction
    private lateinit var  clRoot: ConstraintLayout
    private lateinit var  rvBoard: RecyclerView
    private lateinit var tvNumMoves: TextView
    private lateinit var  tvNumPairs: TextView

    private var boardSize :BoardSize =BoardSize.EASY


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        clRoot = findViewById(R.id.clRoot)
        rvBoard = findViewById(R.id.rvBoard)
        tvNumMoves = findViewById(R.id.tvNumMoves)
        tvNumPairs = findViewById(R.id.tvNumPairs)
        //hack to improve efficiency
//        val intent = Intent (this, CreateActivity::class.java)
//        intent.putExtra(EXTRA_BOARD_SIZE, BoardSize.MEDIUM)
//        startActivity(intent)

        setupBoard()
    }

    private fun setupBoard() {
        when(boardSize){
            BoardSize.EASY -> {
                tvNumMoves.text ="Easy: 4x2"
                tvNumPairs.text = "Pairs: 0/4"
            }

            BoardSize.MEDIUM -> {
                tvNumMoves.text ="Medium: 6x3"
                tvNumPairs.text ="Pairs: 0/9"
            }
            BoardSize.HARD -> {
                tvNumMoves.text ="Medium: 6x4"
                tvNumPairs.text ="Pairs: 0/12"
            }
        }
        tvNumPairs.setTextColor(ContextCompat.getColor(this, R.color.color_progress_none))

        memoryGame = MemoryGame(boardSize)


        adapter = MemoryBoardAdapter(this, boardSize, memoryGame.cards, object:MemoryBoardAdapter.CardClickListener{
            override fun onCardClicked(position: Int) {
                //  Log.i(TAG, "Card clicked $position")
                updateGameWithflip (position)

            }
        })
        rvBoard.adapter = adapter
        rvBoard.setHasFixedSize(true)
        rvBoard.layoutManager = GridLayoutManager (this,boardSize.getWidth())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.mi_refresh -> {
                if (memoryGame.getNumMoves() >0 && !memoryGame.haveWonGame()){
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard()
                    })
                }else{
                    setupBoard()
                }

                return true
            }
            R.id.mi_new_size ->{
                showNewSizeDialog()
                return true
            }
            R.id.mi_custom ->{
                showCreationDialog()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCreationDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        showAlertDialog("Create your own memory board", boardSizeView, View.OnClickListener {
// setting new value for boardsize
            val desiredBoardSize: BoardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM

                else -> BoardSize.HARD

            }
            //navigate to a new activity
            val intent = Intent (this, CreateActivity::class.java)
            intent.putExtra(EXTRA_BOARD_SIZE,desiredBoardSize)
            startActivityForResult(intent,CREATE_REQUEST_CODE)
        })

    }

    private fun showNewSizeDialog() {
        val boardSizeView = LayoutInflater.from(this).inflate(R.layout.dialog_board_size, null)
        val radioGroupSize = boardSizeView.findViewById<RadioGroup>(R.id.radioGroup)
        when  (boardSize) {
            BoardSize.EASY ->radioGroupSize.check(R.id.rbEasy)
            BoardSize.MEDIUM -> radioGroupSize.check(R.id.rbMedium)
            BoardSize.HARD -> radioGroupSize.check(R.id.rbHard)
        }
        showAlertDialog("Choose new size", boardSizeView, View.OnClickListener {
// setting new value for boardsize
            boardSize = when (radioGroupSize.checkedRadioButtonId){
                R.id.rbEasy -> BoardSize.EASY
                R.id.rbMedium -> BoardSize.MEDIUM

                else -> BoardSize.HARD

            }
            setupBoard()
        })
    }

    private fun showAlertDialog(title:String, view: View?, positiveClickListener:View.OnClickListener) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("cancel", null)
            .setPositiveButton("OK"){ _, _ ->
                positiveClickListener.onClick(null)


            }.show()
    }

    private fun updateGameWithflip(position: Int) {
        // error handling
        if (memoryGame.haveWonGame()){
            // alert user of an invalid move
                Snackbar.make (clRoot, "you already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if(memoryGame.isCardFaceUp (position)){
            // alert user of  an error
            Snackbar.make (clRoot, "invalid move", Snackbar.LENGTH_SHORT).show()
            return
        }
      if(memoryGame.flipCard(position)) {
          Log.i(TAG, "found a match! Num pairs found: ${memoryGame.numPairsFound}")
          // use interpolation to determine color
          val color =ArgbEvaluator().evaluate(
              memoryGame.numPairsFound.toFloat()/boardSize.getNumPairs(),
              ContextCompat.getColor (this, R.color.color_progress_none),
              ContextCompat.getColor(this, R.color.color_progress_full)
          )as Int
          tvNumPairs.setTextColor(color)
          tvNumPairs.text ="pairs:${memoryGame.numPairsFound}/${boardSize.getNumPairs()}"
          if(memoryGame.haveWonGame()){
              Snackbar.make(clRoot,"You won! congratulations.", Snackbar.LENGTH_LONG ).show()
          }
      }
        tvNumMoves.text = "Moves:${memoryGame.getNumMoves()}"
          adapter.notifyDataSetChanged()
    }
}