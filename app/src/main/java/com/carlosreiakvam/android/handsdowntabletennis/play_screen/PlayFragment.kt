package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
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
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.States.*
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Defaults.*
import com.carlosreiakvam.android.handsdowntabletennis.score_logic.Player


class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var soundPlayer: SoundPlayer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("slabras", "onCreateView")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        setupOnClickListeners(sharedPref)
        observeGameState()
        actOnPreferences()
        setupSoundPlayer()
        loadGameStateSharedPrefs(sharedPref)

        return binding.root
    }

    private fun setupOnClickListeners(sharedPref: SharedPreferences) {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(viewModel.game.player1, viewModel.game.player2)
            saveGameStateSharedPrefs(sharedPref)
            playDing(viewModel.game.player1)
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(viewModel.game.player2, viewModel.game.player1)
            saveGameStateSharedPrefs(sharedPref)
            playDing(viewModel.game.player2)
        }
    }

    private fun playDing(player: Player) {
        if (player.gameScore >= 30) soundPlayer.playSound(30)
        else soundPlayer.playSound(player.gameScore)
    }

    private fun observeGameState() {
        viewModel.gameState.observe(viewLifecycleOwner) { state ->

            if (state[CURRENTPLAYERSERVER.index] == viewModel.game.player1.playerNumber) {
                Log.d("slabras", "fragment sier at current server er player 1")
                binding.tvP1GameScore?.paintFlags =
                    binding.tvP1GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                binding.tvP2GameScore?.paintFlags = 0

            } else {
                binding.tvP2GameScore?.paintFlags =
                    binding.tvP2GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                binding.tvP1GameScore?.paintFlags = 0
            }

            if (state[ISMATCHSTART.index] == true) {
                Toast.makeText(requireContext(), "MATCHSTART", Toast.LENGTH_SHORT).show()
            } else if (state[ISGAMESTART.index] == true) {
                Toast.makeText(requireContext(), "GAMESTART", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun actOnPreferences() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext()).all
        if (sharedPreferences["mirrored"] == true) {
            binding.tvP1GameScore?.rotation = 180f
            binding.tvP1MatchScore?.rotation = 180f
        }

        viewModel.game.bestOf = sharedPreferences["best_of"] as Int
    }


    private fun setupSoundPlayer() {
        soundPlayer = SoundPlayer(requireContext())
        soundPlayer.fetchSounds()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("slabras", "onConfigurationChanged")
        super.onConfigurationChanged(newConfig)
        if (newConfig.layoutDirection == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(context, "layout portrait", Toast.LENGTH_SHORT).show()
        }
    }


    private fun saveGameStateSharedPrefs(sharedPref: SharedPreferences) {
        with(sharedPref.edit()) {
            putInt(P1GAMESCORE.str, viewModel.game.player1.gameScore)
            putInt(P2GAMESCORE.str, viewModel.game.player2.gameScore)
            putInt(P1MATCHSCORE.str, viewModel.game.player1.matchScore)
            putInt(P2MATCHSCORE.str, viewModel.game.player2.matchScore)
            putInt(CURRENTPLAYERSERVER.str, viewModel.game.currentPlayerServer.playerNumber)
            putInt(BESTOF.name, viewModel.game.bestOf)
            apply()
        }
    }

    private fun loadGameStateSharedPrefs(sharedPref: SharedPreferences) {
        viewModel.game.player1.gameScore = sharedPref.getInt(P1GAMESCORE.str, 0)
        viewModel.game.player2.gameScore = sharedPref.getInt(P2GAMESCORE.str, 0)
        viewModel.game.player1.matchScore = sharedPref.getInt(P1MATCHSCORE.str, 0)
        viewModel.game.player2.matchScore = sharedPref.getInt(P2MATCHSCORE.str, 0)
        viewModel.game.bestOf = sharedPref.getInt(BESTOF.name, BESTOFDEFAULT.int)

        val currentPlayerServerNumber =
            sharedPref.getInt(CURRENTPLAYERSERVER.str, 2)
        Log.d("slabras", "currentPLayerServerNumber: $currentPlayerServerNumber")

        if (currentPlayerServerNumber == viewModel.game.player1.playerNumber) {
            viewModel.game.currentPlayerServer = viewModel.game.player1
        } else {
            viewModel.game.currentPlayerServer = viewModel.game.player2
        }

        viewModel.setGameState()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            viewModel.newMatch(viewModel.game.player1)
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
        Log.d("slabras", "onDestroy")
    }

    override fun onStart() {
        super.onStart()
        Log.d("slabras", "onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("slabras", "onCreate")
    }

    override fun onStop() {
        soundPlayer.release()
        super.onStop()
    }

    override fun onPause() {
        Log.d("slabras", "onPause")
        soundPlayer.release()
        super.onPause()
    }

}

