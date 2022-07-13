package xyz.torsktechnologies.pongcounter.new_game_screens

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import xyz.torsktechnologies.pongcounter.R
import xyz.torsktechnologies.pongcounter.databinding.FirstServerFragmentBinding
import xyz.torsktechnologies.pongcounter.play_screen.InitialValues
import kotlin.random.Random

class FirstServerFragment : Fragment() {

    private val args: FirstServerFragmentArgs by navArgs()
    private lateinit var binding: FirstServerFragmentBinding
    private var firstServer: Int = 1
    private var bestOf: Int = InitialValues.BESTOF.i

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FirstServerFragmentBinding.inflate(layoutInflater, container, false)
        setupFirstServer()
        setupPlayButton()
        setupBackButton()
        bestOf = args.bestOf

        return binding.root
    }


    private fun setupFirstServer() {
        val bgTableTop = binding.tableTop
        val bgTableBottom = binding.tableBottom
        val tvFirstTop = binding.tvFirstTop
        val tvFirstBottom = binding.tvFirstBottom

        tvFirstTop.text = getString(R.string.first_server)

        bgTableTop.setOnClickListener {
            firstServer = 1
            tvFirstTop.isVisible = true
            tvFirstBottom.isVisible = false
            tvFirstTop.text = getString(R.string.first_server)
        }
        bgTableBottom.setOnClickListener {
            firstServer = 2
            tvFirstTop.isVisible = false
            tvFirstBottom.isVisible = true
            tvFirstBottom.text = getString(R.string.first_server)
        }
        bgTableBottom.setOnLongClickListener {
            firstServer = Random.nextInt(1, 3)
            tvFirstTop.isVisible = true
            tvFirstBottom.isVisible = true
            tvFirstBottom.text = getString(R.string.random)
            tvFirstTop.text = getString(R.string.random)
            return@setOnLongClickListener true
        }
        bgTableTop.setOnLongClickListener {
            firstServer = Random.nextInt(1, 3)
            tvFirstTop.isVisible = true
            tvFirstBottom.isVisible = true
            tvFirstBottom.text = getString(R.string.random)
            tvFirstTop.text = getString(R.string.random)
            return@setOnLongClickListener true
        }
    }

    private fun setupPlayButton() {
        val btnPlay = binding.btnLetsPlay
        btnPlay.setOnClickListener {
            this.findNavController()
                .navigate(FirstServerFragmentDirections.actionFirstServerFragmentToPlayFragment(
                    isNewGame = true,
                    bestOf = bestOf,
                    firstServer = firstServer))
        }
    }

    private fun setupBackButton() {
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}