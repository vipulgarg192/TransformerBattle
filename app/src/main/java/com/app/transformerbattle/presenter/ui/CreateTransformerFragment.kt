package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.app.transformerbattle.databinding.FragmentCreateTransformerBinding
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.presenter.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CreateTransformerFragment: Fragment() {

    private lateinit var binding: FragmentCreateTransformerBinding
    private val mainViewModel: MainViewModel by activityViewModels()

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
    }

    private fun bindOperations() {

        binding.btnSubmit.setOnClickListener {
            val transformer = bindTransformer()
            mainViewModel.onTriggerEvent(TransformerEvents.CreateTransformer(transformer))
        }

    }

    private fun bindTransformer(): Transformer {
        return Transformer(id = null,name = binding.editName.text.toString(),
      strength = binding.editStrength.text.toString().toInt(),intelligence = binding.editIntelligence.text.toString().toInt(),
      speed = binding.editSpeed.text.toString().toInt(),endurance= binding.editEndurance.text.toString().toInt(),
     rank = binding.editRank.text.toString().toInt(), firepower = binding.editFirepower.text.toString().toInt(),
      skill = binding.editSkill.text.toString().toInt(), courage = binding.editCourage.text.toString().toInt(),
          team = binding.editTeam.text.toString(),team_icon = null)
    }
}