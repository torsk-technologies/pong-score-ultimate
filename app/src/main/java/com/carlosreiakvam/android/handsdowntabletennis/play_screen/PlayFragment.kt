package com.carlosreiakvam.android.handsdowntabletennis.play_screen

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.ViewModelProvider
import com.carlosreiakvam.android.handsdowntabletennis.databinding.PlayFragmentBinding

abstract class PlayFragment : Fragment() {

    private lateinit var binding: PlayFragmentBinding
    private var playerOneHasTopPos = true
    private lateinit var viewModel: PlayViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[PlayViewModel::class.java]
        binding = PlayFragmentBinding.inflate(inflater)
        val playerOneContainer = binding.player1Container
        playerOneContainer.setOnClickListener{
            Log.d("TAG", "player one clicked")
        }

        return binding.root
    }

    fun switchPlayerContainers(container: ViewGroup?) {
        // Set constraints for player 1's container
        binding.player1Container.updateLayoutParams<ConstraintLayout.LayoutParams> {
            if (playerOneHasTopPos) {
                topToBottom = binding.tvPlayer2Score.id
            } else {
                topToTop = container!!.id
            }
        }

        // Set constraints for player 2's container
        binding.player2Container.updateLayoutParams<ConstraintLayout.LayoutParams> {
            if (playerOneHasTopPos) {
                topToTop = container!!.id
            } else {
                topToBottom = binding.tvPlayer1Score.id
            }
        }
    }


}