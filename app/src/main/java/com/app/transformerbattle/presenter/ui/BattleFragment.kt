package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.app.transformerbattle.databinding.FragmentBattleBinding
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.network.model.TransformerListDto
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
        viewModel.onTriggerEvent(TransformerEvents.RefreshBattle)
        viewModel.transformerList.observe(viewLifecycleOwner, Observer {status->
            when(status){
                is Status.Success -> handleSuccess(status.data)
                is Status.Loading ->  binding.progressbar.visibility = View.VISIBLE
                is Status.Error -> Toast.makeText(requireContext(),status.exception.message, Toast.LENGTH_LONG).show()
            }
        })

        viewModel.battleResult.observe(viewLifecycleOwner, Observer { status->
            when(status){
                is Status.Success ->  handleBattleResult(status.data)
                is Status.Error -> Toast.makeText(requireContext(),status.exception.message, Toast.LENGTH_LONG).show()
                is Status.Loading ->  binding.progressbar.visibility = View.VISIBLE
            }
        })
    }

    private fun handleBattleResult(data: Any) {
        binding.progressbar.visibility = View.GONE
        binding.textResult.text = data.toString()
    }

    private fun handleSuccess(data: TransformerListDto) {
        autobotsList.addAll(data.transformer?.filter {
            it.team.contains("A")
        }!!)
        decepticonsList.addAll(data.transformer?.filter {
            it.team.contains("D")
        }!!)

        spinnerAutoBotsTeamAdapter.notifyDataSetChanged()
        spinnerDecepticonsTeamAdapter.notifyDataSetChanged()
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