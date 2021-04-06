package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.transformerbattle.R
import com.app.transformerbattle.databinding.FragmentTransformerListBinding
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.network.model.TransformerListDto
import com.app.transformerbattle.presenter.adapter.TransformerListAdapter
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.presenter.viewmodel.MainViewModel
import com.app.transformerbattle.utils.ItemClickEventHandler
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TransformerListFragment: Fragment(), ItemClickEventHandler {

    private lateinit var binding: FragmentTransformerListBinding
    private lateinit var mTransformerListAdapter: TransformerListAdapter
    private  var mTransformerList: List<TransformerDto> = ArrayList()
    private val viewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTransformerListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TAG", "onViewCreated: " )
        bindAdapter()
        subscribeViewModel()
        clickEvents()

    }


    private fun bindAdapter() {
        mTransformerListAdapter = TransformerListAdapter(requireActivity(),this, ArrayList())
        binding.rvTransformerList.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.rvTransformerList.adapter = mTransformerListAdapter
    }

    private fun subscribeViewModel() {

        viewModel.onTriggerEvent(TransformerEvents.GetTransformer)

        viewModel.transformerList.observe(viewLifecycleOwner, Observer { status->
            when(status){
                is Status.Error -> handleError(status.exception)
                is Status.Success -> setAdapter(status.data)
                Status.Loading ->  binding.progressbar.visibility = View.VISIBLE
            }
        })
    }

    private fun handleError(exception: Exception) {
        binding.progressbar.visibility = View.GONE
        Toast.makeText(requireContext(),exception.message,Toast.LENGTH_LONG).show()
    }


    private fun setAdapter(data: TransformerListDto) {
        binding.progressbar.visibility = View.GONE
        mTransformerList = data.transformer!!
        mTransformerListAdapter.setData(mTransformerList)
        binding.rvTransformerList.visibility = View.VISIBLE

        val a =   data.transformer?.filter {
            it.team.contains("A")
        }
        val b = data.transformer?.filter {
            it.team.contains("D")
        }
        if (a?.size != 0 && b?.size !=0){
            binding.battleFab.visibility = View.VISIBLE
        }
    }

    private fun clickEvents() {
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_transformerListFragment_to_create_transformer).apply { }
        }

        binding.battleFab.setOnClickListener {
            findNavController().navigate(R.id.action_transformerListFragment_to_battleFragment).apply { }
        }


    }

    override fun itemClick(position: Int) {
        Log.e("TAG", "itemClick: " )

        val transformer = mTransformerList?.get(position)

        val bundle = bundleOf(
                Pair("id", transformer?.id.toString()),
                Pair("team", transformer?.team.toString()),
                Pair("name", transformer?.name.toString()),
                Pair("skill", transformer?.skill.toString()),
                Pair("strength", transformer?.strength.toString()),
                Pair("courage", transformer?.courage.toString()),
                Pair("rank", transformer?.rank.toString()),
                Pair("intelligence", transformer?.intelligence.toString()),
                Pair("firepower", transformer?.firepower.toString()),
                Pair("endurance", transformer?.endurance.toString()),
                Pair("speed", transformer?.speed.toString()),
                Pair("teamicon", transformer?.team_icon.toString())
        )
        findNavController().navigate(R.id.action_transformerListFragment_to_updateFragment,bundle)
    }


}