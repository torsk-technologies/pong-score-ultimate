package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Defaults.BESTOFDEFAULT
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Scores.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.States.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Game
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber
import java.util.*

class PlayViewModel(application: Application) : AndroidViewModel(application) {
    val game = Game()

    private val undoList: LinkedList<Map<Int, Any>> = LinkedList<Map<Int, Any>>()

    private val _gameState: MutableLiveData<Map<Int, Any>> = MutableLiveData(
        mapOf(
            P1GAMESCORE.index to 0,
            P2GAMESCORE.index to 0,
            P1MATCHSCORE.index to 0,
            P2MATCHSCORE.index to 0,
            GAMENUMBER.index to 1,
            CURRENTPLAYERSERVER.index to 1,
            FIRSTPLAYERSERVER.index to game.firstServerPlayer.playerNumber,
            ISGAMESTART.index to false,
            ISMATCHSTART.index to false,
            BESTOF.index to BESTOFDEFAULT.int
        )
    )
    val gameState: LiveData<Map<Int, Any>>
        get() = _gameState


    init {
        Timber.d("viewModel initiated")
    }

    fun registerPoint(player: Player, otherPlayer: Player) {
        Timber.d("register point ma")
        game.registerPoint(player, otherPlayer)
        setGameState()
        undoList.add(_gameState.value ?: mapOf())
    }

    fun setGameState() {
        _gameState.value = mapOf(
            P1GAMESCORE.index to game.player1.gameScore,
            P2GAMESCORE.index to game.player2.gameScore,
            P1MATCHSCORE.index to game.player1.matchScore,
            P2MATCHSCORE.index to game.player2.matchScore,
            CURRENTPLAYERSERVER.index to game.currentPlayerServer,
            ISGAMESTART.index to game.isGameStart,
            ISMATCHSTART.index to game.isMatchStart,
            FIRSTPLAYERSERVER.index to game.firstServerPlayer,
            BESTOF.index to game.bestOf
        )
    }


    fun performUndo() {
        undoList.pollLast()
        val peekScores = undoList.peekLast()
        game.player1.gameScore = (peekScores?.get(P1GAMESCORE.index) ?: 0) as Int
        game.player2.gameScore = (peekScores?.get(P2GAMESCORE.index) ?: 0) as Int
        game.player1.matchScore = (peekScores?.get(P1MATCHSCORE.index) ?: 0) as Int
        game.player2.matchScore = (peekScores?.get(P2MATCHSCORE.index) ?: 0) as Int
        game.currentPlayerServer =
            (peekScores?.get(CURRENTPLAYERSERVER.index) ?: game.currentPlayerServer) as Player
        game.firstServerPlayer =
            (peekScores?.get(FIRSTPLAYERSERVER.index) ?: game.player1) as Player
        game.isGameStart = (peekScores?.get(ISGAMESTART.index) ?: false) as Boolean
        game.isMatchStart = (peekScores?.get(ISMATCHSTART.index) ?: false) as Boolean
        game.bestOf = (peekScores?.get(BESTOF.index) ?: BESTOFDEFAULT.int) as Int
        setGameState()
    }

    fun newMatch(playerServer: Player) {
        game.newMatch(playerServer)
        setGameState()
    }


    override fun onCleared() {
        super.onCleared()
    }
}
