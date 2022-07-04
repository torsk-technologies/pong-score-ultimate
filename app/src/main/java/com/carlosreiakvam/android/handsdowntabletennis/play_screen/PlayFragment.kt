package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.carlosreiakvam.android.handsdowntabletennis.ApplicationController
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.audio_logic.SoundPlayer
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Orientation.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Preferences.SOUNDENABLED
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber


class PlayFragment : Fragment() {

    private val args: PlayFragmentArgs by navArgs()
    private lateinit var gameRulesFromArgs: GameRules
    private var isSoundEnabled: Boolean = true
    private var isVoiceEnabled: Boolean = true
    private lateinit var binding: PlayFragmentBinding
    private lateinit var viewModel: PlayViewModel
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
        setupSoundPlayer()
        // args defaults to -1, -1 from action
        // best of screen sends different values
        Timber.d("args best of: ${args.bestOf}")
        gameRulesFromArgs = GameRules(args.bestOf,args.firstServer)
        viewModel =
            PlayViewModelFactory(
                (requireActivity().application as ApplicationController).database.gameStateDao(),
                gameRulesFromArgs)
                .create(PlayViewModel::class.java)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        observeCurrentServer()
        observePlayerScores()
        observeWinStates()
        loadSharedPrefsGameState()
//        setOrientation(NORMAL)
        setupGameClickListeners()
        setupMenuClickListeners()
        return binding.root
    }

    private fun setupMenuClickListeners() {
        binding.btnUndo?.setOnClickListener {
            viewModel.onUndo()
            // play sound on undo
//            if (isSoundEnabled) {
//                if (viewModel.currentServerLive.value == 1) playDing(viewModel.player1Live.value!!)
//                else playDing(viewModel.player2Live.value!!)
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

        binding.btnVoices?.setOnClickListener {
            if (!isVoiceEnabled) {
                isVoiceEnabled = true
                binding.btnVoices?.setImageResource(R.drawable.ic_baseline_record_voice_over_24)
            } else {
                isVoiceEnabled = false
                binding.btnVoices?.setImageResource(R.drawable.ic_baseline_voice_over_off_24)
            }
        }

        binding.btnRules?.setOnClickListener {
            alertBestOf()
        }

        binding.btnNewGame?.setOnClickListener {
            alertNewGame()
        }


        binding.btnToggleMenu?.setOnClickListener {
            if (binding.btnUndo?.isVisible == true) {
                binding.btnUndo?.isVisible = false
                binding.btnLayoutChange?.isVisible = false
                binding.btnVoices?.isVisible = false
                binding.btnNewGame?.isVisible = false
                binding.btnToggleSound?.isVisible = false
                binding.btnRules?.isVisible = false
            } else {
                binding.btnUndo?.isVisible = true
                binding.btnNewGame?.isVisible = true
                binding.btnLayoutChange?.isVisible = true
                binding.btnVoices?.isVisible = true
                binding.btnToggleSound?.isVisible = true
                binding.btnRules?.isVisible = true
            }
        }

    }

    private fun setupGameClickListeners() {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(Players.PLAYER1.i)
            if (isSoundEnabled) playDing(viewModel.player1Live.value ?: Player("", 1))
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(Players.PLAYER2.i)
            if (isSoundEnabled) playDing(viewModel.player2Live.value ?: Player("", 1))
        }

        binding.btnBack?.setOnClickListener {
            activity?.onBackPressed()
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

    private fun observeWinStates() {
        viewModel.winStates.observe(viewLifecycleOwner) { state ->
            if (state.isMatchReset) {
                Timber.d("match is reset")
            } else if (state.isMatchWon) {
                Timber.d("match won ")
                alertOnMatchWon()
            } else if (state.isGameWon) {
                Timber.d("game won ")
            } else {
                Timber.d("No win. Ordinary point")
            }
        }

    }

    private fun saveSharedPrefsGameState() {
        with(sharedPref.edit()) {
            //arguments: (key,value)
            putBoolean(NORMAL.name, orientations[NORMAL] ?: true)
            putBoolean(MIRRORED.name, orientations[MIRRORED] ?: false)
            putBoolean(LEFT.name, orientations[LEFT] ?: false)
            putBoolean(RIGHT.name, orientations[RIGHT] ?: false)
            putBoolean(SOUNDENABLED.name, isSoundEnabled)
            apply()
        }
    }

    private fun loadSharedPrefsGameState() {
        isSoundEnabled = sharedPref.getBoolean(SOUNDENABLED.name, true)
        if (isSoundEnabled) binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_note_24)
        else binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_off_24)

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
    }

    private fun setupSoundPlayer() {
        soundPlayer = SoundPlayer(requireContext())
        soundPlayer.fetchSounds()
    }

    private fun playDing(player: Player) {
        if (player.gameScore >= 20) soundPlayer.playSound(20)
        else soundPlayer.playSound(player.gameScore)
    }

    private fun alertNewGame() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(R.layout.alert_new_game)
            builder.create()
        }
        alertDialog?.show()

        alertDialog?.findViewById<TextView>(R.id.alert_btn_new_game)?.setOnClickListener {
            alertDialog.cancel()
//            viewModel.newGameOnMatchReset()

            this.findNavController()
                .navigate(PlayFragmentDirections.actionPlayFragmentToBestOfFragment())
        }
    }

    private fun alertOnMatchWon() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.in_game_options_style)
            builder.setCancelable(false)
            builder.setView(R.layout.match_won)
            builder.create()
        }
        alertDialog?.show()
        val prevGameRules = viewModel.gameRulesLive.value
        // new game button
        Timber.d("on match won, using following gamerules\nbestof: ${prevGameRules?.bestOf}")
        alertDialog?.findViewById<Button>(R.id.btn_woho)?.setOnClickListener {
            viewModel.newGameOnMatchWon()
            alertDialog.cancel()
        }
    }

    private fun alertBestOf() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setView(R.layout.alert_best_of)
            builder.create()
        }
        alertDialog?.show()
        alertDialog?.findViewById<TextView>(R.id.alert_tv_best_of)?.text =
            getString(R.string.best_of_complete, viewModel.gameRulesLive.value?.bestOf)
        alertDialog?.findViewById<TextView>(R.id.alert_tv_best_of)?.setOnClickListener {
            alertDialog.cancel()
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

