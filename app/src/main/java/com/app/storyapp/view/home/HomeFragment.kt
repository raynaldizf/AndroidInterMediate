package com.app.storyapp.view.home

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.storyapp.R
import com.app.storyapp.adapter.HomeAdapter
import com.app.storyapp.adapter.LoadingStateAdapter
import com.app.storyapp.adapter.QuoteListAdapter
import com.app.storyapp.databinding.FragmentHomeBinding
import com.app.storyapp.datastore.SharedPref
import com.app.storyapp.viewmodel.MainViewModel
import com.app.storyapp.viewmodel.ViewModelFactory
import com.app.storyapp.viewmodel.ViewModelStory
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    lateinit var binding : FragmentHomeBinding
    lateinit var viewModel : ViewModelStory
    lateinit var adapter : HomeAdapter
    lateinit var sharedPref: SharedPref

    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
//        viewModel = ViewModelProvider(this)[ViewModelStory::class.java]

        binding.btnMap.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_mapsFragment)
        }
//        viewModel.story().observe(viewLifecycleOwner){
//            if (it != null){
//                binding.rvStory.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
//                adapter = HomeAdapter(it)
//                binding.rvStory.adapter = adapter
//            }else{
//                binding.rvStory.visibility = View.GONE
//            }
//        }

        mainViewModel = ViewModelProvider(this, ViewModelFactory(requireContext()))[MainViewModel::class.java]

        sharedPref.getToken.asLiveData().observe(viewLifecycleOwner){
            if (it != null){
                Log.d("token pas function gettoken", it)
//                viewModel.getAllStory(it)
                showRecyclist(it)
            }else{
                Toast.makeText(context, "Token Kosong", Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnAddStory.setOnClickListener{
            findNavController().navigate(R.id.action_homeFragment_to_addStoryFragment)
        }

        binding.btnLogout.setOnClickListener{
            removeToken()
        }
    }

    private fun removeToken() {
        GlobalScope.launch {
            sharedPref.removeToken()
        }
        Toast.makeText(context, "Logout Sukses!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
    }

    private fun showRecyclist(token : String) {

        binding.apply {
            rvStory.setHasFixedSize(true)
            val adapter = QuoteListAdapter()
            rvStory.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter{
                    adapter.retry()
                }
            )
            if (requireContext().applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvStory.layoutManager = GridLayoutManager(context, 2)
            } else {
                rvStory.layoutManager = GridLayoutManager(context, 1)
            }
            mainViewModel.quote("Bearer $token").observe(viewLifecycleOwner){
                adapter.submitData(lifecycle,it)
                Log.d("test : " , token)
            }
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.apply {
            visibility = if (isLoading) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }
}