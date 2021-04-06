package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.app.transformerbattle.databinding.FragmentBattleBinding
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.presenter.adapter.CountryArrayAdapter
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.presenter.viewmodel.MainViewModel
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BattleFragment: Fragment() {

    private lateinit var binding: FragmentBattleBinding
    private val viewModel: MainViewModel by activityViewModels()
    private  var autobotsList : MutableList<TransformerDto> = ArrayList()
    private  var decepticonsList :  MutableList<TransformerDto>  = ArrayList()

    private lateinit var  spinnerAutoBotsTeamAdapter : CountryArrayAdapter
    private lateinit var  spinnerDecepticonsTeamAdapter : CountryArrayAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBattleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindAdapters()
        subscribeViewModel()
        clickEvents()

    }


    private fun subscribeViewModel() {
        viewModel.transformerList.observe(viewLifecycleOwner, Observer {
            autobotsList.addAll(it.transformer?.filter {
                it.team.contains("A")
            }!!)
            decepticonsList.addAll(it.transformer?.filter {
                it.team.contains("D")
            }!!)

            spinnerAutoBotsTeamAdapter.notifyDataSetChanged()
            spinnerDecepticonsTeamAdapter.notifyDataSetChanged()
        })

        viewModel.battleResult.observe(viewLifecycleOwner, Observer { status->
            when(status){
                is Status.Success ->  binding.textResult.text = status.data.toString()
            }
        })
    }

    private fun bindAdapters() {
        spinnerAutoBotsTeamAdapter = CountryArrayAdapter(requireContext(), autobotsList)
        binding.spinnerAutobots.adapter = spinnerAutoBotsTeamAdapter
        spinnerDecepticonsTeamAdapter = CountryArrayAdapter(requireContext(), decepticonsList)
        binding.spinnerDecepticon.adapter = spinnerDecepticonsTeamAdapter
    }

    private fun clickEvents() {
            binding.btnBattle.setOnClickListener {
                viewModel.onTriggerEvent(TransformerEvents.LetBattleTransformer(autobotsList
                        .get(binding.spinnerAutobots.selectedItemPosition),
                        decepticonsList
                                .get(binding.spinnerDecepticon.selectedItemPosition)))
            }
    }
}