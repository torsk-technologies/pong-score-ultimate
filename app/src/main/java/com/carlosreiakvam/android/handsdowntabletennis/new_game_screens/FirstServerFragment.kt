package com.carlosreiakvam.android.handsdowntabletennis.new_game_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.carlosreiakvam.android.handsdowntabletennis.databinding.FirstServerFragmentBinding
import timber.log.Timber
import kotlin.random.Random

class FirstServerFragment : Fragment() {

    private val args: FirstServerFragmentArgs by navArgs()
    private lateinit var binding: FirstServerFragmentBinding
    private var firstServer: Int = 1
    private var bestOf: Int = 21

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FirstServerFragmentBinding.inflate(layoutInflater, container, false)
        setupFirstServer()
        setupPlayButton()
        bestOf = args.bestOf

        return binding.root
    }


    private fun setupFirstServer() {
        val bgTableTop = binding.tableTop
        val tvTableTop = binding.tvTableTop
        val bgTableBottom = binding.tableTop
        val tvTableBottom = binding.tvTableBottom
        val btnRandom = binding.btnRandom

        tvTableTop.text = "X"

        bgTableTop.setOnClickListener {
            firstServer = 1
            tvTableTop.text = "X"
            tvTableBottom.text = ""
        }
        bgTableBottom.setOnClickListener {
            firstServer = 2
            tvTableTop.text = ""
            tvTableBottom.text = "X"
        }
        btnRandom.setOnClickListener {
            firstServer = Random.nextInt(1, 3)
            Timber.d("random: $firstServer")
            tvTableTop.text = "?"
            tvTableBottom.text = "?"
        }
    }

    private fun setupPlayButton() {
        val btnPlay = binding.btnLetsPlay
        btnPlay.setOnClickListener {
            this.findNavController()
                .navigate(FirstServerFragmentDirections.actionFirstServerFragmentToPlayFragment(
                    bestOf = bestOf,
                    firstServer = firstServer))

        }
    }
}