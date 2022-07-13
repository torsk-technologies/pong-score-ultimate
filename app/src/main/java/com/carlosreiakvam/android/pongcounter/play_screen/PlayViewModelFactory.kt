package com.carlosreiakvam.android.pongcounter.play_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carlosreiakvam.android.pongcounter.local_db.GameStateDAO

class PlayViewModelFactory(
    private val gameStateDAO: GameStateDAO,
    private val gameRules: GameRules,
    private val isNewGame: Boolean,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayViewModel(gameStateDAO, gameRules, isNewGame) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}