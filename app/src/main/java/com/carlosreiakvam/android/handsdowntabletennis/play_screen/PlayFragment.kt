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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.audio_logic.SoundPlayer
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Orientation.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Scores.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber


class PlayFragment : Fragment() {

    private var isSoundEnabled: Boolean = true
    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var sharedPref: SharedPreferences
    private var orientations: MutableMap<Orientation, Boolean> = mutableMapOf(
        NORMAL to true,
        MIRRORED to false,
        LEFT to false,
        RIGHT to false
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel


        observeGameState(binding)
        setupSoundPlayer()


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOrientation(NORMAL)
        setupOnClickListeners()
        actOnPreferences()
        loadSharedPrefsGameState()

    }


    private fun setupOnClickListeners() {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(viewModel.game.player1, viewModel.game.player2)
            saveSharedPrefsGameState()
            if (isSoundEnabled) playDing(viewModel.game.player1)
            viewModel.undoMaintenance()
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(viewModel.game.player2, viewModel.game.player1)
            saveSharedPrefsGameState()
            if (isSoundEnabled) playDing(viewModel.game.player2)
            viewModel.undoMaintenance()
        }

        binding.p1Container?.setOnLongClickListener {
            alertInGameOptions()
            true
        }

        binding.p2Container?.setOnLongClickListener {
            alertInGameOptions()
            true
        }

        binding.btnFloatingUndo?.setOnClickListener {
            viewModel.performUndo()
            if (isSoundEnabled) {
                if (viewModel.game.player1.isCurrentServer) playDing(viewModel.game.player1)
                else playDing(viewModel.game.player2)
            }
        }

        binding.btnLayoutChange?.setOnClickListener {
            when {
                orientations[NORMAL] == true -> setOrientation(RIGHT)
                orientations[RIGHT] == true -> setOrientation(MIRRORED)
                orientations[MIRRORED] == true -> setOrientation(LEFT)
                orientations[LEFT] == true -> setOrientation(NORMAL)
            }
            Timber.d("orientations: $orientations")
        }

        binding.btnPopupMenu?.setOnClickListener {
            if (binding.btnFloatingUndo?.isVisible == true) {
                binding.btnFloatingUndo?.isVisible = false
                binding.btnLayoutChange?.isVisible = false
            } else {
                binding.btnFloatingUndo?.isVisible = true
                binding.btnLayoutChange?.isVisible = true
            }
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
                this.binding.tvP1GameScore?.paintFlags =
                    this.binding.tvP1GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                this.binding.tvP2GameScore?.paintFlags = 0

            } else {
                this.binding.tvP2GameScore?.paintFlags =
                    this.binding.tvP2GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                this.binding.tvP1GameScore?.paintFlags = 0
            }

            if (state.winStates.isMatchReset) {
                Timber.d("match is reset")
            } else if (state.winStates.isMatchWon) {
                Timber.d("match won ")
                val wonByBestOf: Int = (state.winStates.gameWonByBestOf) - 2
                Toast.makeText(
                    requireContext(),
                    "Best of $wonByBestOf won!",
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
        if (sharedPreferences["sound"] == true) {
            isSoundEnabled = false

        }
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
            //arguments: (key,value)
            putInt(P1GAMESCORE.name, viewModel.game.player1.gameScore)
            putInt(P2GAMESCORE.name, viewModel.game.player2.gameScore)
            putInt(P1MATCHSCORE.name, viewModel.game.player1.matchScore)
            putInt(P2MATCHSCORE.name, viewModel.game.player2.matchScore)
            putBoolean(P1CURRENTSERVER.name, viewModel.game.player1.isCurrentServer)
            putBoolean(P2CURRENTSERVER.name, viewModel.game.player2.isCurrentServer)
            putBoolean(NORMAL.name, orientations[NORMAL] ?: true)
            putBoolean(MIRRORED.name, orientations[MIRRORED] ?: false)
            putBoolean(LEFT.name, orientations[LEFT] ?: false)
            putBoolean(RIGHT.name, orientations[RIGHT] ?: false)
            apply()
        }
    }

    private fun loadSharedPrefsGameState() {
        viewModel.game.player1.gameScore = sharedPref.getInt(P1GAMESCORE.name, 0)
        viewModel.game.player2.gameScore = sharedPref.getInt(P2GAMESCORE.name, 0)
        viewModel.game.player1.matchScore = sharedPref.getInt(P1MATCHSCORE.name, 0)
        viewModel.game.player2.matchScore = sharedPref.getInt(P2MATCHSCORE.name, 0)

        if (sharedPref.getBoolean(P1CURRENTSERVER.name, true)) {
            viewModel.game.player1.isCurrentServer = true
        } else {
            viewModel.game.player2.isCurrentServer = true
        }

        val orientationNormal = sharedPref.getBoolean(NORMAL.name, true)
        val orientationMirrored = sharedPref.getBoolean(MIRRORED.name, false)
        val orientationLeft = sharedPref.getBoolean(LEFT.name, false)
        val orientationRight = sharedPref.getBoolean(RIGHT.name, false)

        when {
            orientationNormal -> setOrientation(NORMAL)
            orientationMirrored -> setOrientation(MIRRORED)
            orientationLeft -> setOrientation(LEFT)
            orientationRight -> setOrientation(RIGHT)
        }


        viewModel.updateGameState()
        Timber.d("sharedPrefs gameState loaded")
        viewModel.onLoadPrefs()

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

    private fun setOrientationState(orientation: Orientation) {
        for (i in orientations) {
            orientations[i.key] = i.key == orientation
        }
    }

    private fun setOrientation(orientation: Orientation) {
        setOrientationState(orientation)
        when (orientation) {
            NORMAL -> {
                binding.tvP1GameScore?.rotation = 0f
                binding.tvP2GameScore?.rotation = 0f
                binding.tvP1MatchScore?.rotation = 0f
                binding.tvP2MatchScore?.rotation = 0f
            }
            MIRRORED -> {
                binding.tvP1GameScore?.rotation = 180f
                binding.tvP1MatchScore?.rotation = 180f
                binding.tvP2GameScore?.rotation = 0f
                binding.tvP2MatchScore?.rotation = 0f
            }
            LEFT -> {
                binding.tvP1GameScore?.rotation = 90f
                binding.tvP1MatchScore?.rotation = 90f
                binding.tvP2GameScore?.rotation = 90f
                binding.tvP2MatchScore?.rotation = 90f
            }
            RIGHT -> {
                binding.tvP1GameScore?.rotation = 270f
                binding.tvP1MatchScore?.rotation = 270f
                binding.tvP2GameScore?.rotation = 270f
                binding.tvP2MatchScore?.rotation = 270f
            }
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

    override fun onResume() {
        super.onResume()
    }


}

