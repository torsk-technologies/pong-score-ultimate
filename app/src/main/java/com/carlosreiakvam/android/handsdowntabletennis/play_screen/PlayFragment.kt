package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.carlosreiakvam.android.handsdowntabletennis.ApplicationController
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.audio_logic.SoundPlayer
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Orientation.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber


class PlayFragment : Fragment() {

    private var isSoundEnabled: Boolean = true
    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by activityViewModels {
        PlayViewModelFactory(
            (activity?.application as ApplicationController).database.gameStateDao()
        )
    }

    private lateinit var soundPlayer: SoundPlayer

    //    private lateinit var sharedPref: SharedPreferences
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
//        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel
//        observeGameState(binding)
        observePlayerScores()
        observeCurrentServer()
        setupSoundPlayer()
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setOrientation(NORMAL)
        setupOnClickListeners()
    }


    private fun setupOnClickListeners() {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(Players.PLAYER1.i)
            if (isSoundEnabled) playDing(viewModel.player1Live.value ?: Player("", 1))
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(Players.PLAYER2.i)
            if (isSoundEnabled) playDing(viewModel.player2Live.value ?: Player("", 1))
        }

        binding.btnUndo?.setOnClickListener {
            viewModel.onUndo()
//            try {
//                lifecycle.coroutineScope.launch {
//                    viewModel.deleteLast()
//                    viewModel.getLast().collect { value ->
//                        viewModel.registerPoint(value.p1GameScore)
//                        viewModel.registerPoint(value.p2GameScore)
//                    }
//                }
//            } catch (e: Error) {
//            }

            // play sound on undo
//            if (isSoundEnabled) {
//                if (viewModel.game.player1.isCurrentServer) playDing(viewModel.game.player1)
//                else playDing(viewModel.game.player2)
//            }
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
        binding.btnToggleSound?.setOnClickListener {
            if (!isSoundEnabled) {
                isSoundEnabled = true
                binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_note_24)
            } else {
                isSoundEnabled = false
                binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_off_24)
            }


        }

        binding.btnNewMatch?.setOnClickListener {
            viewModel.onMatchReset()
        }

        binding.btnToggleMenu?.setOnClickListener {
            if (binding.btnUndo?.isVisible == true) {
                binding.btnUndo?.isVisible = false
                binding.btnLayoutChange?.isVisible = false
                binding.btnNewMatch?.isVisible = false
                binding.btnThemes?.isVisible = false
                binding.btnToggleSound?.isVisible = false
                binding.btnRules?.isVisible = false
            } else {
                binding.btnUndo?.isVisible = true
                binding.btnThemes?.isVisible = true
                binding.btnLayoutChange?.isVisible = true
                binding.btnNewMatch?.isVisible = true
                binding.btnToggleSound?.isVisible = true
                binding.btnRules?.isVisible = true
            }
        }

    }

    private fun observePlayerScores() {
        viewModel.player1Live.observe(viewLifecycleOwner) {
            this.binding.tvP1GameScore?.text = it.gameScore.toString()
            this.binding.tvP1MatchScore?.text = it.matchScore.toString()
        }
        viewModel.player2Live.observe(viewLifecycleOwner) {
            this.binding.tvP2GameScore?.text = it.gameScore.toString()
            this.binding.tvP2MatchScore?.text = it.matchScore.toString()
        }
    }

    private fun observeCurrentServer() {
        viewModel.currentServerLive.observe(viewLifecycleOwner) {
            if (it == Players.PLAYER1.i) {
                this.binding.tvP1GameScore?.paintFlags =
                    this.binding.tvP1GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                this.binding.tvP2GameScore?.paintFlags = 0

            } else {
                this.binding.tvP2GameScore?.paintFlags =
                    this.binding.tvP2GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                this.binding.tvP1GameScore?.paintFlags = 0
            }
        }
    }

//    private fun observeGameState(binding: PlayFragmentBinding) {
//        viewModel.gameState.observe(viewLifecycleOwner) { state ->
//            if (state.isMatchReset) {
//                Timber.d("match is reset")
//            } else if (state.isMatchWon) {
//                Timber.d("match won ")
//                val wonByBestOf: Int = (state.gameWonByBestOf) - 2
//                Toast.makeText(
//                    requireContext(),
//                    "Best of $wonByBestOf won!",
//                    Toast.LENGTH_SHORT
//                ).show()
//            } else if (state.isGameWon) {
//                Timber.d("game won ")
//            } else {
//                Timber.d("No win. Ordinary point")
//            }
//        }

//}


    private fun setupSoundPlayer() {
        soundPlayer = SoundPlayer(requireContext())
        soundPlayer.fetchSounds()
    }

    private fun playDing(player: Player) {
        if (player.gameScore >= 20) soundPlayer.playSound(30)
        else soundPlayer.playSound(player.gameScore)
    }


//    private fun alertInGameOptions() {
//        val alertDialog: AlertDialog? = activity?.let {
//            val builder = AlertDialog.Builder(it, R.style.in_game_options_style)
//            builder.setView(R.layout.in_game_options)
//            builder.create()
//        }
//        alertDialog?.show()
//
//        alertDialog?.findViewById<Button>(R.id.btn_new_match)?.setOnClickListener {
//            viewModel.onMatchReset()
//            alertDialog.cancel()
//        }

//        alertDialog?.findViewById<Button>(R.id.btn_settings)?.setOnClickListener {
//            alertDialog.cancel()
//            this.findNavController()
//                .navigate(PlayFragmentDirections.actionPlayFragmentToOptionsFragment())
//        }
//    }

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
//        saveSharedPrefsGameState()
    }

    override fun onStart() {
        super.onStart()
        Timber.tag("lifecycle").d("onStart")
    }

    override fun onStop() {
//        soundPlayer.release()
        super.onStop()
        Timber.tag("lifecycle").d("onStop")
    }

    override fun onPause() {
        soundPlayer.release()
        super.onPause()
        Timber.d("onPause")
    }


}

