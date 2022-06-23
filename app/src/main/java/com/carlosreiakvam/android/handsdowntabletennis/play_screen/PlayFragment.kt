package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.audio_logic.SoundPlayer
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Scores.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber


class PlayFragment : Fragment() {

    private lateinit var pfBinding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var sharedPref: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        pfBinding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        pfBinding.lifecycleOwner = viewLifecycleOwner
        pfBinding.viewmodel = viewModel

        setupOnClickListeners()
        observeGameState(pfBinding)
        actOnPreferences()
        setupSoundPlayer()
        loadSharedPrefsGameState()

        return pfBinding.root
    }

    private fun setupOnClickListeners() {
        pfBinding.p1Container?.setOnClickListener {
            viewModel.registerPoint(viewModel.game.player1, viewModel.game.player2)
            saveSharedPrefsGameState()
            playDing(viewModel.game.player1)
        }

        pfBinding.p2Container?.setOnClickListener {
            viewModel.registerPoint(viewModel.game.player2, viewModel.game.player1)
            saveSharedPrefsGameState()
            playDing(viewModel.game.player2)
        }
    }

    private fun playDing(player: Player) {
        if (player.gameScore >= 30) soundPlayer.playSound(30)
        else soundPlayer.playSound(player.gameScore)
    }

    private fun observeGameState(binding: PlayFragmentBinding) {
        viewModel.gameState.observe(viewLifecycleOwner) { state ->
            Timber.d("Observing game state")

            binding.tvP1GameScore?.text = state.player1State.gameScore.toString()
            binding.tvP2GameScore?.text = state.player2State.gameScore.toString()
            binding.tvP1MatchScore?.text = state.player1State.matchScore.toString()
            binding.tvP2MatchScore?.text = state.player2State.matchScore.toString()

            if (state.player1State.isCurrentServer) {
                pfBinding.tvP1GameScore?.paintFlags =
                    pfBinding.tvP1GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                pfBinding.tvP2GameScore?.paintFlags = 0

            } else {
                pfBinding.tvP2GameScore?.paintFlags =
                    pfBinding.tvP2GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                pfBinding.tvP1GameScore?.paintFlags = 0
            }

            if (state.winStates.isMatchReset) {
                Timber.d("match is reset")
            } else if (state.winStates.isMatchWon) {
                Timber.d("match won ")
                val wonByBestOf: Int = (state.winStates.gameWonByBestOf) - 2
                Toast.makeText(
                    requireContext(),
                    "Best of $wonByBestOf won by player",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (state.winStates.isGameWon) {
                Timber.d("game won ")
            } else {
                Timber.d("No win. Ordinary point")
            }
        }

    }

    private fun actOnPreferences() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext()).all
        if (sharedPreferences["mirrored"] == true) {
            pfBinding.tvP1GameScore?.rotation = 180f
            pfBinding.tvP1MatchScore?.rotation = 180f
        }

//        viewModel.game.bestOf = sharedPreferences["best_of"] as Int
    }


    private fun setupSoundPlayer() {
        soundPlayer = SoundPlayer(requireContext())
        soundPlayer.fetchSounds()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.layoutDirection == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(context, "layout portrait", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveSharedPrefsGameState() {
        with(sharedPref.edit()) {
            putInt(P1GAMESCORE.str, viewModel.game.player1.gameScore)
            putInt(P2GAMESCORE.str, viewModel.game.player2.gameScore)
            putInt(P1MATCHSCORE.str, viewModel.game.player1.matchScore)
            putInt(P2MATCHSCORE.str, viewModel.game.player2.matchScore)
            putBoolean(P1CURRENTSERVER.str, viewModel.game.player1.isCurrentServer)
            putBoolean(P2CURRENTSERVER.str, viewModel.game.player2.isCurrentServer)
            apply()
        }
    }

    private fun loadSharedPrefsGameState() {
        viewModel.game.player1.gameScore = sharedPref.getInt(P1GAMESCORE.str, 0)
        viewModel.game.player2.gameScore = sharedPref.getInt(P2GAMESCORE.str, 0)
        viewModel.game.player1.matchScore = sharedPref.getInt(P1MATCHSCORE.str, 0)
        viewModel.game.player2.matchScore = sharedPref.getInt(P2MATCHSCORE.str, 0)

        if (sharedPref.getBoolean(P1CURRENTSERVER.str, true)) {
            viewModel.game.player1.isCurrentServer = true
        } else {
            viewModel.game.player2.isCurrentServer = true
        }

        viewModel.updateGameState()
        Timber.d("sharedPrefs gameState loaded")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pfBinding.p1Container?.setOnLongClickListener {
            alertInGameOptions()
            true
        }

        pfBinding.p2Container?.setOnLongClickListener {
            alertInGameOptions()
            true
        }

        pfBinding.btnFloatingUndo?.setOnClickListener {
            viewModel.performUndo()
        }

    }


    private fun alertInGameOptions() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.in_game_options_style)
            builder.setView(R.layout.in_game_options)
            builder.create()
        }
        alertDialog?.show()

        alertDialog?.findViewById<Button>(R.id.btn_new_match)?.setOnClickListener {
            viewModel.onMatchReset()
            alertDialog.cancel()
        }

        alertDialog?.findViewById<Button>(R.id.btn_settings)?.setOnClickListener {
            alertDialog.cancel()
            this.findNavController()
                .navigate(PlayFragmentDirections.actionPlayFragmentToOptionsFragment())
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPlayer.release()
        saveSharedPrefsGameState()
    }

    override fun onStart() {
        super.onStart()
        Timber.tag("lifecycle").d("onStart")
    }

    override fun onStop() {
        soundPlayer.release()
        super.onStop()
        Timber.tag("lifecycle").d("onStop")
    }

    override fun onPause() {
        soundPlayer.release()
        super.onPause()
        Timber.d("onPause")
    }

}

