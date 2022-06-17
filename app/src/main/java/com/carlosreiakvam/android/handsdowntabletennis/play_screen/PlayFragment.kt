package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.SharedPreferences
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
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Constants.*


class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()
    private val mirroredMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAG", "onCreateView")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val ape = sharedPreferences.all
        if (ape["mirrored"] == true) {
            binding.p1Container?.rotation = 180f
        }

        setupOnClickListeners(sharedPref)
        loadPref(sharedPref)
        observeGameState()
        observeGameStart()


        return binding.root
    }


    private fun observeGameStart() {
        viewModel.gameStart.observe(viewLifecycleOwner) {
            if (it == true) {
                chooseSide()
                viewModel.setupNewGame()
                // popup fragment, choose sides
            }
        }
    }

    private fun setupOnClickListeners(sharedPref: SharedPreferences) {
        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(P1GAMESCORE.int, P2GAMESCORE.int, P1MATCHSCORE.int)
            savePref(sharedPref)
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(P2GAMESCORE.int, P1GAMESCORE.int, P2MATCHSCORE.int)
            savePref(sharedPref)
        }

    }


    private fun observeGameState() {
        viewModel.gameState.observe(viewLifecycleOwner) {
            if (it[PTURN.int] == 1) {
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

    private fun chooseSide() {
        Toast.makeText(requireContext(), "choose sides", Toast.LENGTH_SHORT).show()
        // viewModel.chooseServer(1)
    }


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
            putInt(P1GAMESCORE.str, viewModel.gameState.value?.get(P1GAMESCORE.int) ?: 0)
            putInt(P2GAMESCORE.str, viewModel.gameState.value?.get(P2GAMESCORE.int) ?: 0)
            putInt(P1MATCHSCORE.str, viewModel.gameState.value?.get(P1MATCHSCORE.int) ?: 0)
            putInt(P2MATCHSCORE.str, viewModel.gameState.value?.get(P2MATCHSCORE.int) ?: 0)
            putInt(PTURN.str, viewModel.gameState.value?.get(PTURN.int) ?: 0)
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.p1Container?.setOnLongClickListener() {
            alertInGameOptions()
            true
        }

        binding.p2Container?.setOnLongClickListener() {
            alertInGameOptions()
            true
        }

    }

    private fun alertInGameOptions() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it, R.style.in_game_options_style)
            builder.setView(R.layout.in_game_options)
            builder.create()
        }
        alertDialog?.show()
        alertDialog?.findViewById<Button>(R.id.btn_undo)?.setOnClickListener() {
            viewModel.undo()
        }
        alertDialog?.findViewById<Button>(R.id.btn_new_match)?.setOnClickListener() {
            viewModel.resetMatch()
            alertDialog.cancel()
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
}

