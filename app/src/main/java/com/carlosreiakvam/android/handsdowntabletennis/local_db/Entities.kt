package com.carlosreiakvam.android.handsdowntabletennis.local_db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @NonNull @ColumnInfo(name = "p1_game_score") val p1GameScore: Int,
    @NonNull @ColumnInfo(name = "p1_match_score") val p1MatchScore: Int,
    @NonNull @ColumnInfo(name = "p2_game_score") val p2GameScore: Int,
    @NonNull @ColumnInfo(name = "p2_match_score") val p2MatchScore: Int,
    @NonNull @ColumnInfo(name = "first_server") val firstServer: Int,
    @NonNull @ColumnInfo(name = "current_server") val currentServer: Int,
    @NonNull @ColumnInfo(name = "game_won_by_best_of") val gameWonByBestOf: Int,
    @NonNull @ColumnInfo(name = "game_winner") val gameWinner: Int,
    @NonNull @ColumnInfo(name = "game_to_best_of") val gameToBestOf: Int,
    @NonNull @ColumnInfo(name = "is_game_won") val isGameWon: Boolean,
    @NonNull @ColumnInfo(name = "is_match_won") val isMatchWon: Boolean,
    @NonNull @ColumnInfo(name = "is_match_reset") val isMatchReset: Boolean,
)