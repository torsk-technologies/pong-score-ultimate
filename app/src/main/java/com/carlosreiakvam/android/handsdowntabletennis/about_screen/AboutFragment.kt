package com.carlosreiakvam.android.handsdowntabletennis.about_screen

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.carlosreiakvam.android.handsdowntabletennis.databinding.AboutFragmentBinding
import timber.log.Timber

class AboutFragment : Fragment() {
    private lateinit var binding: AboutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AboutFragmentBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener {
            this.findNavController()
                .navigate(AboutFragmentDirections.actionAboutFragmentToPlayFragment(
                    isNewGame = false))
        }
        binding.btnBuyCoffee.setOnClickListener {
            val browserIntent =
                Intent(Intent.ACTION_VIEW, Uri.parse("https://ko-fi.com/carlosreiakvam"))
            startActivity(browserIntent)
        }

        binding.btnGiveFeedback.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:torsktechnologies@gmail.com") // only email apps should handle this
                putExtra(Intent.EXTRA_SUBJECT, "Feedback for cho")
            }
            startActivity(intent)
        }

        return binding.root
    }

}