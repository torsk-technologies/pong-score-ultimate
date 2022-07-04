package com.carlosreiakvam.android.handsdowntabletennis.local_db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.InitialValues

@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @NonNull @ColumnInfo(name = "p1_game_score") val p1GameScore: Int = 0,
    @NonNull @ColumnInfo(name = "p1_match_score") val p1MatchScore: Int = 0,
    @NonNull @ColumnInfo(name = "p2_game_score") val p2GameScore: Int = 0,
    @NonNull @ColumnInfo(name = "p2_match_score") val p2MatchScore: Int = 0,
    @NonNull @ColumnInfo(name = "first_server") val firstServer: Int = InitialValues.FIRSTSERVER.i,
    @NonNull @ColumnInfo(name = "current_server") val currentServer: Int = 1,
    @NonNull @ColumnInfo(name = "game_won_by_best_of") val gameWonByBestOf: Int = 9,
    @NonNull @ColumnInfo(name = "points_played") val pointsPlayed: Int = 0,
    @NonNull @ColumnInfo(name = "game_winner") val gameWinner: Int = 1,
    @NonNull @ColumnInfo(name = "game_to_best_of") val gameToBestOf: Int = InitialValues.BESTOF.i,
    @NonNull @ColumnInfo(name = "is_game_won") val isGameWon: Boolean = false,
    @NonNull @ColumnInfo(name = "is_match_won") val isMatchWon: Boolean = false,
    @NonNull @ColumnInfo(name = "is_match_reset") val isMatchReset: Boolean = false,
)