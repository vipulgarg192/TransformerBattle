package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.app.transformerbattle.R
import com.app.transformerbattle.databinding.FragmentCreateTransformerBinding
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.presenter.viewmodel.MainViewModel
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTransformerFragment: Fragment() {

    private lateinit var binding: FragmentCreateTransformerBinding
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var mTeam : String
    private lateinit var transformerTeamArrayList : List<String>
    private lateinit var  spinnerTeamAdapter : ArrayAdapter<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCreateTransformerBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindOperations()
        bindSpinners()
        subscribeViewModel()
    }

    private fun subscribeViewModel() {
        mainViewModel.result.observe(viewLifecycleOwner, Observer { status->
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
        findNavController().popBackStack()
    }

    private fun bindSpinners() {

        transformerTeamArrayList = requireActivity().resources.getStringArray(R.array.transformer_list).toList()
        spinnerTeamAdapter = ArrayAdapter(requireContext(), R.layout.support_simple_spinner_dropdown_item, transformerTeamArrayList)
        binding.spinnerName.adapter = spinnerTeamAdapter
    }

    private fun bindOperations() {

        binding.btnSubmit.setOnClickListener {
            val transformer = bindTransformer()
            mainViewModel.onTriggerEvent(TransformerEvents.CreateTransformer(transformer))
        }

    }

    private fun bindTransformer(): Transformer {
        mTeam = if (binding.radioAutoBots.isChecked){
            "A"
        }else{
            "D"
        }

        return Transformer(id = null, name = binding.spinnerName.selectedItem.toString().trim(),
                strength = binding.editStrength.text.toString().toInt(), intelligence = binding.editIntelligence.text.toString().toInt(),
                speed = binding.editSpeed.text.toString().toInt(), endurance = binding.editEndurance.text.toString().toInt(),
                rank = binding.editRank.text.toString().toInt(), firepower = binding.editFirepower.text.toString().toInt(),
                skill = binding.editSkill.text.toString().toInt(), courage = binding.editCourage.text.toString().toInt(),
                team = mTeam, team_icon = null)
    }

}

