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


class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()
    private lateinit var soundPlayer: SoundPlayer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAG", "onCreateView")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        setupOnClickListeners(sharedPref)
        loadPref(sharedPref)
        observeGameState()
        observeGameStart()
        actOnPreferences()
        setupSoundPlayer()

        return binding.root
    }

    private fun setupSoundPlayer() {
        soundPlayer = SoundPlayer(requireContext())
        soundPlayer.fetchSounds()
    }


    private fun actOnPreferences() {
        val sharedPreferences =
            PreferenceManager.getDefaultSharedPreferences(requireContext()).all
        if (sharedPreferences["mirrored"] == true) {
            binding.p1Container?.rotation = 180f
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        Log.d("TAG", "onConfigurationChanged")
        super.onConfigurationChanged(newConfig)
        if (newConfig.layoutDirection == Configuration.ORIENTATION_PORTRAIT) {
            Toast.makeText(context, "layout portrait", Toast.LENGTH_SHORT).show()
        }
    }


    private fun observeGameStart() {
        viewModel.gameStart.observe(viewLifecycleOwner) {
            if (it == true) {
                Toast.makeText(context, "new game", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "gameNumber: ${viewModel.gameNumber.value}")
//                chooseSide()
//                viewModel.setupNewGame()
//                popup fragment, choose sides
                viewModel.setupNewGame()
            }
        }
    }


    private fun setupOnClickListeners(sharedPref: SharedPreferences) {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(1)
            savePref(sharedPref)
            var gamePoint: Int = viewModel.gameState.value?.get(P1GAMESCORE.index) ?: 0
            if (gamePoint >= 30) {
                gamePoint = 30
            }
            soundPlayer.playSound(gamePoint)
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(2)
            savePref(sharedPref)
            var gamePoint: Int = viewModel.gameState.value?.get(P2GAMESCORE.index) ?: 0
            if (gamePoint >= 30) {
                gamePoint = 30
            }
            soundPlayer.playSound(gamePoint)

        }
    }


    private fun observeGameState() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            if (it[PTURN.index] == 1) {
                binding.tvP1GameScore?.paintFlags =
                    binding.tvP1GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                binding.tvP2GameScore?.paintFlags = 0

            } else {
                binding.tvP2GameScore?.paintFlags =
                    binding.tvP2GameScore?.paintFlags?.or(Paint.UNDERLINE_TEXT_FLAG)!!
                binding.tvP1GameScore?.paintFlags = 0
            }
        }

    }

//    private fun chooseSide() {
//        Toast.makeText(requireContext(), "choose sides", Toast.LENGTH_SHORT).show()
// viewModel.chooseServer(1)
//    }


    private fun loadPref(sharedPref: SharedPreferences) {
        val p1GameScore = sharedPref.getInt(P1GAMESCORE.str, 0)
        val p2GameScore = sharedPref.getInt(P2GAMESCORE.str, 0)
        val p1MatchScore = sharedPref.getInt(P1MATCHSCORE.str, 0)
        val p2MatchScore = sharedPref.getInt(P2MATCHSCORE.str, 0)
        val pTurn = sharedPref.getInt(PTURN.str, 0)
        viewModel.setGameState(
            p1GameScore,
            p2GameScore,
            p1MatchScore,
            p2MatchScore,
            pTurn
        )

    }

    private fun savePref(sharedPref: SharedPreferences) {
        with(sharedPref.edit()) {
            putInt(P1GAMESCORE.str, viewModel.gameState.value?.get(P1GAMESCORE.index) ?: 0)
            putInt(P2GAMESCORE.str, viewModel.gameState.value?.get(P2GAMESCORE.index) ?: 0)
            putInt(P1MATCHSCORE.str, viewModel.gameState.value?.get(P1MATCHSCORE.index) ?: 0)
            putInt(P2MATCHSCORE.str, viewModel.gameState.value?.get(P2MATCHSCORE.index) ?: 0)
            putInt(PTURN.str, viewModel.gameState.value?.get(PTURN.index) ?: 0)
            apply()
        }
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
//        alertDialog?.findViewById<Button>(R.id.btn_undo)?.setOnClickListener() {
//            viewModel.undo()
//        }
        alertDialog?.findViewById<Button>(R.id.btn_new_match)?.setOnClickListener {
            viewModel.resetMatch()
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
        Log.d("TAG", "onDestroy")
    }

    override fun onStart() {
        super.onStart()
        Log.d("TAG", "onStart")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("TAG", "onCreate")
    }

    override fun onPause() {
        Log.d("TAG", "onPause")
        super.onPause()
    }

}

