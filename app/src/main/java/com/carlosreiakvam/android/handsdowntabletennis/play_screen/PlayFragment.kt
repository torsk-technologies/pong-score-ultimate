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
import com.carlosreiakvam.android.handsdowntabletennis.BuildConfig
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.audio_logic.SoundPlayer
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Orientation.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Preferences.SOUNDENABLED
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player
import timber.log.Timber


class PlayFragment : Fragment() {

//    private val args: PlayFragmentDirections by navArgs()
    private var args: PlayFragmentDirections by navArgs()
    private lateinit var gameRulesFromArgs: GameRules
    private var isSoundEnabled: Boolean = true
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
        loadSharedPrefsGameState()
        setupGameClickListeners()
        setupMenuClickListeners()
        checkFirstRun()
        return binding.root
    }

    private fun checkFirstRun() {
        val DOESNTEXIST = -1
        val currentVersionCode = BuildConfig.VERSION_CODE
        val savedVersionCode = sharedPref.getInt("version_code", DOESNTEXIST)

        if (currentVersionCode == savedVersionCode) {
            // just a normal run
            return
        } else if (savedVersionCode == DOESNTEXIST) {
            // New install
            viewModel.onFirstRun()
        } else if (currentVersionCode > savedVersionCode) {
            // This is an upgrade
        }
        sharedPref.edit().putInt("version_code", currentVersionCode).apply()
    }

    private fun setupMenuClickListeners() {
        binding.btnUndo?.setOnClickListener {
            viewModel.onUndo()
        }

        binding.btnLayoutChange?.setOnClickListener {
            when {
                orientations[NORMAL] == true -> setOrientation(RIGHT)
                orientations[RIGHT] == true -> setOrientation(MIRRORED)
                orientations[MIRRORED] == true -> setOrientation(LEFT)
                orientations[LEFT] == true -> setOrientation(NORMAL)
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
            this.findNavController()
                .navigate(PlayFragmentDirections.actionPlayFragmentToBestOfFragment())

        }

        binding.btnInfo?.setOnClickListener {
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
            putBoolean(NORMAL.name, orientations[NORMAL] ?: false)
            putBoolean(MIRRORED.name, orientations[MIRRORED] ?: false)
            putBoolean(LEFT.name, orientations[LEFT] ?: false)
            putBoolean(RIGHT.name, orientations[RIGHT] ?: false)
            putBoolean(SOUNDENABLED.name, isSoundEnabled)
            apply()
        }
    }

    private fun loadSharedPrefsGameState() {
        Timber.d("orientation normal:  ${orientations[NORMAL]}")
        Timber.d("orientation mirrored:  ${orientations[MIRRORED]}")
        isSoundEnabled = sharedPref.getBoolean(SOUNDENABLED.name, true)
        if (isSoundEnabled) binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_note_24)
        else binding.btnToggleSound?.setImageResource(R.drawable.ic_baseline_music_off_24)

        val orientationNormal = sharedPref.getBoolean(NORMAL.name, false)
        val orientationMirrored = sharedPref.getBoolean(MIRRORED.name, false)
        val orientationLeft = sharedPref.getBoolean(LEFT.name, false)
        val orientationRight = sharedPref.getBoolean(RIGHT.name, false)
        Timber.d("orientation normal etter sharedpref:  ${orientations[NORMAL]}")
        Timber.d("orientation mirrored ettersharedpref:  ${orientations[MIRRORED]}")

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

//    private fun alertNewGame() {
//        val alertDialog: AlertDialog? = activity?.let {
//            val builder = AlertDialog.Builder(it)
//            builder.setView(R.layout.alert_new_game)
//            builder.create()
//        }
//        alertDialog?.show()
//
//        alertDialog?.findViewById<TextView>(R.id.alert_btn_new_game)?.setOnClickListener {
//            alertDialog.cancel()
//
//            this.findNavController()
//                .navigate(PlayFragmentDirections.actionPlayFragment2ToBestOfFragment())
//        }
//    }

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

    private fun setOrientationState(orientation: Orientation) {
        for (i in orientations) {
            orientations[i.key] = i.key == orientation
        }
    }

    private fun setOrientation(orientation: Orientation) {
        Timber.d("set orientation: ${orientation.name}")
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

    override fun onPause() {
        super.onPause()
        soundPlayer.release()
    }

    override fun onResume() {
        super.onResume()
//        loadSharedPrefsGameState()
    }

}


}

