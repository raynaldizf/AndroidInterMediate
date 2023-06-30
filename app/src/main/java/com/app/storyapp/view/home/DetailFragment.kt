package com.app.storyapp.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.storyapp.databinding.FragmentDetailBinding
import com.app.storyapp.datastore.SharedPref
import com.app.storyapp.viewmodel.ViewModelStory
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailFragment : Fragment() {
    lateinit var binding : FragmentDetailBinding
    lateinit var sharedPref : SharedPref
    lateinit var viewModel : ViewModelStory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedPref = SharedPref(requireContext())
        val idStory = arguments?.getString("id")
        viewModel = ViewModelProvider(this)[ViewModelStory::class.java]

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        // Check if token is already saved
        lifecycleScope.launch(Dispatchers.IO) {
            val token = sharedPref.getToken.first()
            if (token.isNotBlank()) {
                withContext(Dispatchers.Main){
                    viewModel.detail().observe(viewLifecycleOwner) {
                        if (it != null) {
                            binding.txtNama.text = it.story.name
                            binding.deskripsi.text = it.story.description
                            Glide.with(requireContext()).load(it.story.photoUrl).into(binding.images)
                        } else {
                            Toast.makeText(context, "Gagal Mengambil Data!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                    viewModel.detailStory(token, idStory!!)
                }
            }else{
                Toast.makeText(context, "Gagal Mengambil Data!", Toast.LENGTH_SHORT).show()
            }
        }

    }
}