package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.carlosreiakvam.android.handsdowntabletennis.local_db.GameStateDAO

class PlayViewModelFactory(private val gameStateDAO: GameStateDAO) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayViewModel(gameStateDAO) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}