package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding
import com.carlosreiakvam.android.handsdowntabletennis.play_screen.Constants.*


class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("TAG", "onCreateView")
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return requireView()

        binding.p1Container?.setOnClickListener {
            viewModel.registerPoint(P1GAMESCORE.int, P2GAMESCORE.int, P1MATCHSCORE.int)
            savePref(sharedPref)
        }

        binding.p2Container?.setOnClickListener {
            viewModel.registerPoint(P2GAMESCORE.int, P1GAMESCORE.int, P2MATCHSCORE.int)
            savePref(sharedPref)
        }


        // Load saved scores from sharedPref
        loadPref(sharedPref)

        // Force landscape orientation
//        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        return binding.root
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
            alertReset()
            true
        }

        binding.p2Container?.setOnLongClickListener() {
            alertReset()
            true
        }

    }

    fun alertReset() {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setNeutralButton("Undo") { dialog, id ->
                    viewModel.undo()
                }
                setPositiveButton(
                    "Reset match"
                ) { dialog, id ->
                    viewModel.resetMatch()
                }
                setNegativeButton(
                    "cancel"
                ) { dialog, id ->
                    // User cancelled the dialog
                }
                setTitle("Reset Match")
            }
            // Set other dialog properties


            // Create the AlertDialog
            builder.create()
        }
        alertDialog?.show()
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

