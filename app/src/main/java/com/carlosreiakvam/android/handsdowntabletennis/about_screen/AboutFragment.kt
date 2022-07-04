package com.carlosreiakvam.android.handsdowntabletennis.about_screen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.carlosreiakvam.android.handsdowntabletennis.databinding.AboutFragmentBinding

class AboutFragment : Fragment() {
    private lateinit var binding: AboutFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = AboutFragmentBinding.inflate(layoutInflater, container, false)
        binding.btnBack.setOnClickListener{
            activity?.onBackPressed()
        }
        binding.btnLinktree.setOnClickListener{
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://linktr.ee/carlosreiakvam"))
            startActivity(browserIntent)
        }
        return binding.root
    }

}