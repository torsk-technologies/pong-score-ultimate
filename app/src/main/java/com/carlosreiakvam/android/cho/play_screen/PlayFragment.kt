package com.carlosreiakvam.android.cho.play_screen

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
import com.carlosreiakvam.android.cho.ApplicationController
import com.carlosreiakvam.android.cho.BuildConfig
import com.carlosreiakvam.android.cho.R
import com.carlosreiakvam.android.cho.audio_logic.SoundPlayer
import com.carlosreiakvam.android.cho.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.cho.play_screen.Orientation.*
import com.carlosreiakvam.android.cho.play_screen.Preferences.SOUNDENABLED
import com.carlosreiakvam.android.cho.score_logic.Player


class PlayFragment : Fragment() {

    private val args: PlayFragmentArgs by navArgs()
    private lateinit var gameRulesFromArgs: GameRules
    private var isSoundEnabled: Boolean = true
    private lateinit var binding: PlayFragmentBinding
    private lateinit var viewModel: PlayViewModel
    private lateinit var soundPlayer: SoundPlayer
    private lateinit var sharedPref: SharedPreferences

    private var orientationName: String = NORMAL.name


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        setupSoundPlayer()
        gameRulesFromArgs = GameRules(args.bestOf, args.firstServer)
        viewModel =
            PlayViewModelFactory(
                (requireActivity().application as ApplicationController).database.gameStateDao(),
                gameRulesFromArgs, isNewGame = args.isNewGame)
                .create(PlayViewModel::class.java)

        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        observeCurrentServer()
        observePlayerScores()
        observeWinStates()
        setupGameClickListeners()
        setupMenuClickListeners()
        loadSharedPrefsGameState()
        checkFirstRun()
        return binding.root
    }

    private fun checkFirstRun() {
        val doesNotExist = -1
        val currentVersionCode = BuildConfig.VERSION_CODE
        val savedVersionCode = sharedPref.getInt("version_code", doesNotExist)

        if (currentVersionCode == savedVersionCode) {
            // just a normal run
            return
        } else if (savedVersionCode == doesNotExist) {
            // New install
            viewModel.onFirstRun()
//        } else if (currentVersionCode > savedVersionCode) {
            // This is an upgrade
        }
        sharedPref.edit().putInt("version_code", currentVersionCode).apply()
    }

    private fun setupMenuClickListeners() {
        binding.btnUndo?.setOnClickListener {
            viewModel.onUndo()
        }

        binding.btnLayoutChange?.setOnClickListener {
            when (orientationName) {
                NORMAL.name -> setOrientation(RIGHT.name)
                RIGHT.name -> setOrientation(MIRRORED.name)
                MIRRORED.name -> setOrientation(LEFT.name)
                LEFT.name -> setOrientation(NORMAL.name)
            }
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

        binding.btnRules?.setOnClickListener {
            alertBestOf()
        }

        binding.btnNewGame?.setOnClickListener {
            saveSharedPrefsGameState()
            this.findNavController()
                .navigate(PlayFragmentDirections.actionPlayFragmentToBestOfFragment())

        }

        binding.btnInfo?.setOnClickListener {
            saveSharedPrefsGameState()
            this.findNavController()
                .navigate(PlayFragmentDirections.actionPlayFragmentToAboutFragment())

        }


        binding.btnToggleMenu?.setOnClickListener {
            if (binding.btnUndo?.isVisible == true) {
                binding.btnUndo?.isVisible = false
                binding.btnLayoutChange?.isVisible = false
                binding.btnInfo?.isVisible = false
                binding.btnNewGame?.isVisible = false
                binding.btnToggleSound?.isVisible = false
                binding.btnRules?.isVisible = false
            } else {
                binding.btnInfo?.isVisible = true
                binding.btnUndo?.isVisible = true
                binding.btnNewGame?.isVisible = true
                binding.btnLayoutChange?.isVisible = true
                binding.btnToggleSound?.isVisible = true
                binding.btnRules?.isVisible = true
            }
        }
    }

    private fun setupGameClickListeners() {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(Players.PLAYER1.i)
            if (isSoundEnabled) playDing(viewModel.player1Live.value ?: Player(""))
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(Players.PLAYER2.i)
            if (isSoundEnabled) playDing(viewModel.player2Live.value ?: Player(""))
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
            if (state.isMatchWon)
                alertOnMatchWon()
        }

    }

    private fun saveSharedPrefsGameState() {
        with(sharedPref.edit()) {
            putString(ORIENTATION.name, orientationName)
            putBoolean(SOUNDENABLED.name, isSoundEnabled)
            apply()
        }
    }

    private fun loadSharedPrefsGameState() {
        isSoundEnabled = sharedPref.getBoolean(SOUNDENABLED.name, true)
        if (isSoundEnabled) binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_note_24)
        else binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_off_24)

        orientationName = sharedPref.getString(ORIENTATION.name, NORMAL.name).toString()
        setOrientation(orientationName)

    }

    private fun setupSoundPlayer() {
        soundPlayer = SoundPlayer(requireContext())
        soundPlayer.fetchSounds()
    }


    private fun playDing(player: Player) {
        if (player.gameScore >= 20) soundPlayer.playSound(20)
        else soundPlayer.playSound(player.gameScore)
    }

    private fun alertOnMatchWon() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.in_game_options_style)
            builder.setCancelable(false)
            builder.setView(R.layout.match_won)
            builder.create()
        }
        alertDialog?.show()
        // new game button
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


    private fun setOrientation(orientation: String) {
        orientationName = orientation
        when (orientation) {
            NORMAL.name -> {
                binding.tvP1GameScore?.rotation = 0f
                binding.tvP2GameScore?.rotation = 0f
                binding.tvP1MatchScore?.rotation = 0f
                binding.tvP2MatchScore?.rotation = 0f


            }
            MIRRORED.name -> {
                binding.tvP1GameScore?.rotation = 180f
                binding.tvP1MatchScore?.rotation = 180f
                binding.tvP2GameScore?.rotation = 0f
                binding.tvP2MatchScore?.rotation = 0f
            }
            LEFT.name -> {
                binding.tvP1GameScore?.rotation = 90f
                binding.tvP1MatchScore?.rotation = 90f
                binding.tvP2GameScore?.rotation = 90f
                binding.tvP2MatchScore?.rotation = 90f
            }
            RIGHT.name -> {
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

}
