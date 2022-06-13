package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.carlosreiakvam.android.handsdowntabletennis.R
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding


class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private val viewModel: PlayViewModel by viewModels()

    private val P1_GAME_SCORE = "p1GameScore"
    private val P2_GAME_SCORE = "p2GameScore"
    private val P1_MATCH_SCORE = "p1MatchScore"
    private val P2_MATCH_SCORE = "p2MatchScore"
    private val P_TURN = "pTurn"


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
            viewModel.increaseGameScore(1)
            savePref(sharedPref)
        }

        binding.p2Container?.setOnClickListener {
            viewModel.increaseGameScore(2)
            savePref(sharedPref)
        }

        // Load saved scores from sharedPref
        loadPref(sharedPref)


        // Force landscape orientation
//        activity?.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)

        return binding.root
    }

    private fun loadPref(sharedPref: SharedPreferences) {
        val p1GameScore = sharedPref.getInt(P1_GAME_SCORE, 0)
        val p2GameScore = sharedPref.getInt(P2_GAME_SCORE, 0)
        val p1MatchScore = sharedPref.getInt(P1_MATCH_SCORE, 0)
        val p2MatchScore = sharedPref.getInt(P2_MATCH_SCORE, 0)
        val pTurn = sharedPref.getInt(P_TURN, 0)
        viewModel.setGamestate(
            p1GameScore,
            p2GameScore,
            p1MatchScore,
            p2MatchScore,
            pTurn
        )

    }

    private fun savePref(sharedPref: SharedPreferences) {
        with(sharedPref.edit()) {
            putInt(P1_GAME_SCORE, viewModel.p1GameScore.value ?: 0)
            putInt(P2_GAME_SCORE, viewModel.p2GameScore.value ?: 0)
            putInt(P1_MATCH_SCORE, viewModel.p1MatchScore.value ?: 0)
            putInt(P2_MATCH_SCORE, viewModel.p2MatchScore.value ?: 0)
            putInt(P_TURN, viewModel.pTurn.value ?: 0)
            apply()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.tvPlayer1Score.text = viewModel.p1GameScore.value.toString()
//        binding.tvPlayer2Score.text = viewModel.p2GameScore.value.toString()
//        binding.tvPlayer1ScoreGame.text = viewModel.p1MatchScore.value.toString()
//        binding.tvPlayer2ScoreGame.text = viewModel.p2MatchScore.value.toString()
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
