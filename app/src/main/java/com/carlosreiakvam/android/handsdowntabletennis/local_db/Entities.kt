package com.carlosreiakvam.android.handsdowntabletennis.local_db

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.InitialValues

@Entity(tableName = "game_state")
data class GameStateEntity(
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @NonNull @ColumnInfo(name = "p1_game_score") var p1GameScore: Int = 0,
    @NonNull @ColumnInfo(name = "p1_match_score") var p1MatchScore: Int = 0,
    @NonNull @ColumnInfo(name = "p2_game_score") var p2GameScore: Int = 0,
    @NonNull @ColumnInfo(name = "p2_match_score") var p2MatchScore: Int = 0,
    @NonNull @ColumnInfo(name = "first_server") var firstServer: Int = InitialValues.FIRSTSERVER.i,
    @NonNull @ColumnInfo(name = "current_server") var currentServer: Int = 1,
    @NonNull @ColumnInfo(name = "game_won_by_best_of") var gameWonByBestOf: Int = 9,
    @NonNull @ColumnInfo(name = "points_played") var nGamesPlayed: Int = 0,
    @NonNull @ColumnInfo(name = "game_winner") var gameWinner: Int = 1,
    @NonNull @ColumnInfo(name = "game_to_best_of") var gameToBestOf: Int = InitialValues.BESTOF.i,
    @NonNull @ColumnInfo(name = "is_game_won") var isGameWon: Boolean = false,
    @NonNull @ColumnInfo(name = "is_match_won") var isMatchWon: Boolean = false,
    @NonNull @ColumnInfo(name = "is_match_reset") var isMatchReset: Boolean = false,
)