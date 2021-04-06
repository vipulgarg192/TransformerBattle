package com.app.transformerbattle.presenter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.transformerbattle.R
import com.app.transformerbattle.databinding.FragmentTransformerListBinding
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.presenter.adapter.TransformerListAdapter
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.presenter.viewmodel.MainViewModel
import com.app.transformerbattle.utils.ItemClickEventHandler
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

        viewModel.loading.observe(viewLifecycleOwner, Observer { isLoading->
           if (isLoading){
               binding.progressbar.visibility = View.VISIBLE
           }else {
               binding.progressbar.visibility = View.GONE
           }
        })

        viewModel.transformerList.observe(viewLifecycleOwner, Observer {
            mTransformerList = it.transformer!!
            mTransformerListAdapter.setData(mTransformerList)
            binding.rvTransformerList.visibility = View.VISIBLE
        })

        viewModel.transformerList.observe(viewLifecycleOwner, Observer {
         val a =   it.transformer?.filter {
                it.team.contains("A")
            }
            val b = it.transformer?.filter {
                it.team.contains("D")
            }
            if (a!= null && b !=null){
                binding.battleFab.visibility = View.VISIBLE
            }
        })
    }

    private fun clickEvents() {
        binding.fab.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_transformerListFragment_to_create_transformer).apply { }
        }

        binding.battleFab.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.action_transformerListFragment_to_battleFragment).apply { }
        }
    }

    override fun itemClick(position: Int) {
        Log.e("TAG", "launchJob: Exception: ")
    }


}