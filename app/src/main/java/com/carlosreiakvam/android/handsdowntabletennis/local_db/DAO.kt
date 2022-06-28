package com.carlosreiakvam.android.handsdowntabletennis.local_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameStateDAO {
    @Insert
    suspend fun insertGameState(gameState: GameStateEntity)

    @Query("SELECT * from game_state ORDER BY id DESC LIMIT 1")
    fun getLast(): Flow<GameStateEntity>

    @Query("DELETE FROM game_state WHERE id = (SELECT MAX (id) FROM game_state)")
    fun deleteLast()

    @Query("DELETE FROM game_state")
    fun deleteAll()

}