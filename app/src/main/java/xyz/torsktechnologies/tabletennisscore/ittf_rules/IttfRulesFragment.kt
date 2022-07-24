package xyz.torsktechnologies.tabletennisscore.ittf_rules


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import timber.log.Timber
import xyz.torsktechnologies.tabletennisscore.R
import xyz.torsktechnologies.tabletennisscore.databinding.IttfRulesFragmentBinding


class IttfRulesFragment : Fragment() {

    private lateinit var binding: IttfRulesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().window.statusBarColor = requireContext().getColor(R.color.bt_black)
        binding = DataBindingUtil.inflate(inflater, R.layout.ittf_rules_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        setText()
        return binding.root
    }

    private fun setText() {
        binding.mainTitle.text = RulesStrings().mainTitle
        binding.underTitle.text = RulesStrings().underTitle
        binding.rulesDefinitions.text = RulesStrings().definitions
        binding.rulesService.text = RulesStrings().service
        binding.rulesReturn.text = RulesStrings().rulesReturn
        binding.rulesOrderPlay.text = RulesStrings().orderOfPlay
        binding.rulesLet.text = RulesStrings().let
        binding.rulesPoint.text = RulesStrings().point
        binding.rulesGame.text = RulesStrings().game
        binding.rulesMatch.text = RulesStrings().match
        binding.rulesOrderServe.text = RulesStrings().orderServing
    }



}
