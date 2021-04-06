package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.transformerbattle.databinding.FragmentUpdateTransformerBinding
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.presenter.viewmodel.MainViewModel
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateFragment: Fragment() {

    private lateinit var binding: FragmentUpdateTransformerBinding
    private val viewModel: MainViewModel by activityViewModels()
    private var id: String? = null
    private var teamIcon: String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentUpdateTransformerBinding.inflate(layoutInflater)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindData()
        subscribeViewModel()
        clickEvents()

    }

    private fun bindData() {
        id = arguments?.getString("id")
        binding.textTeam.text = arguments?.getString("team")
        binding.textName.text = arguments?.getString("name")
        binding.editCourage.setText(arguments?.getString("courage"))
        binding.editSkill.setText(arguments?.getString("skill"))
        binding.editEndurance.setText(arguments?.getString("endurance"))
        binding.editSpeed.setText(arguments?.getString("speed"))
        binding.editRank.setText(arguments?.getString("rank"))
        binding.editFirepower.setText(arguments?.getString("firepower"))
        binding.editIntelligence.setText(arguments?.getString("intelligence"))
        binding.editStrength.setText(arguments?.getString("strength"))
        teamIcon = arguments?.getString("teamicon")
    }


    private fun subscribeViewModel() {
        viewModel.onTriggerEvent(TransformerEvents.RefreshBattle)
        viewModel.result.observe(viewLifecycleOwner, Observer { status->
            when(status){
                is Status.Success -> handleSuccess()
                is Status.Loading ->  binding.progressbar.visibility = View.VISIBLE
                is Status.Error -> handleError(status.exception)
            }
        })
    }

    private fun handleError(exception: Exception) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(requireContext(),exception.message,Toast.LENGTH_LONG).show()
    }

    private fun handleSuccess() {
        binding.progressbar.visibility = View.GONE
        viewModel.resetModel()
        findNavController().navigateUp()
    }


    private fun clickEvents() {
        binding.btnSubmit.setOnClickListener {
            val transformer = bindTransformer()
            viewModel.onTriggerEvent(TransformerEvents.UpdateTransformer(transformer))
        }
    }

    private fun bindTransformer(): Transformer {

        return Transformer(id = id, name = binding.textName.text.toString().trim(),
                strength = binding.editStrength.text.toString().toInt(), intelligence = binding.editIntelligence.text.toString().toInt(),
                speed = binding.editSpeed.text.toString().toInt(), endurance = binding.editEndurance.text.toString().toInt(),
                rank = binding.editRank.text.toString().toInt(), firepower = binding.editFirepower.text.toString().toInt(),
                skill = binding.editSkill.text.toString().toInt(), courage = binding.editCourage.text.toString().toInt(),
                team = binding.textTeam.text.toString().trim(), team_icon = teamIcon)
    }
}