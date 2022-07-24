package xyz.torsktechnologies.tabletennisscore.ez_desc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import xyz.torsktechnologies.tabletennisscore.R
import xyz.torsktechnologies.tabletennisscore.databinding.EzDescFragmentBinding

lateinit var binding: EzDescFragmentBinding

class EzDescFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.ez_desc_fragment, container, false)
        setupBindings()
        return binding.root
    }

    fun setupBindings() {
        binding.ezServe.text = EzStrings().ezServe
        binding.ezReturn.text = EzStrings().ezReturn
        binding.ezPoint.text = EzStrings().ezPoint
        binding.ezTurn.text = EzStrings().ezTurns
        binding.ezGame.text = EzStrings().ezGame
        binding.ezMatch.text = EzStrings().ezMatch
    }
}