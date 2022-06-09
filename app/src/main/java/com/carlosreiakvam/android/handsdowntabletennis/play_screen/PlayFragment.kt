package com.carlosreiakvam.android.handsdowntabletennis.play_screen

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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.play_fragment, container, false)
        setPlayersTurnSymbol()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewmodel = viewModel

        binding.player1Container.setOnClickListener {
            viewModel.p1IncreaseGameScore()
            if (viewModel.checkForPlayerSwitch()) {
                setPlayersTurnSymbol()
            }
        }

        binding.player2Container.setOnClickListener {
            viewModel.p2IncreaseGameScore()
            if (viewModel.checkForPlayerSwitch()) {
                setPlayersTurnSymbol()
            }
        }
    }

    fun setPlayersTurnSymbol() {
        if (viewModel.player1Turn.value == true) {
            Log.d("TAG", "p1 turn")
            binding.tvP1Turn.text = "X"
            binding.tvP2Turn.text = ""
        } else {
            Log.d("TAG", "p2 turn")
            binding.tvP1Turn.text = ""
            binding.tvP2Turn.text = "X"
        }

    }

}
