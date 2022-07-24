package xyz.torsktechnologies.tabletennisscore.start_screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import xyz.torsktechnologies.tabletennisscore.R
import xyz.torsktechnologies.tabletennisscore.databinding.StartScreenBinding


class StartScreenFragment : Fragment() {

    private lateinit var binding: StartScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.bt_black)
        binding = DataBindingUtil.inflate(inflater, R.layout.start_screen, container, false)
        binding.lifecycleOwner = viewLifecycleOwner


        setupMenuClickListeners()
        return binding.root
    }


    private fun setupMenuClickListeners() {
        binding.btnPlay.setOnClickListener {
            this.findNavController()
                .navigate(StartScreenFragmentDirections.actionStartScreenFragmentToPlayFragment())
        }
        binding.btnEzDesc.setOnClickListener {
            this.findNavController()
                .navigate(StartScreenFragmentDirections.actionStartScreenFragmentToEzDescFragment())
        }
        binding.btnIttfRules.setOnClickListener {
            this.findNavController()
                .navigate(StartScreenFragmentDirections.actionStartScreenFragmentToIttfRulesFragment())
        }
        binding.btnAbout.setOnClickListener {
            this.findNavController()
                .navigate(StartScreenFragmentDirections.actionStartScreenFragmentToAboutFragment())
        }


    }
}
