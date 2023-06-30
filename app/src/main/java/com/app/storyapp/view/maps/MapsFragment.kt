package com.app.storyapp.view.maps

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.storyapp.R
import com.app.storyapp.databinding.FragmentMapsBinding
import com.app.storyapp.datastore.SharedPref
import com.app.storyapp.model.request.Story
import com.app.storyapp.viewmodel.ViewModelStory
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapsFragment : Fragment(), OnMapReadyCallback {
    private lateinit var sharedPref: SharedPref
    private lateinit var mMap: GoogleMap
    private lateinit var binding: FragmentMapsBinding
    private lateinit var viewModel: ViewModelStory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        viewModel = ViewModelProvider(this).get(ViewModelStory::class.java)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.zoomInButton.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomIn())
        }

        binding.zoomOutButton.setOnClickListener {
            mMap.animateCamera(CameraUpdateFactory.zoomOut())
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val token = sharedPref.getToken.first()
            if (token.isNotBlank()) {
                withContext(Dispatchers.Main) {
                    viewModel.storymap().observe(viewLifecycleOwner) { storyList ->
                        storyList?.let {
                            showStoriesOnMap(storyList)
                        }
                    }
                    viewModel.storyHaveMap(token)
                }
            } else {
                Toast.makeText(requireContext(), "Token is empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        try {
            val style = MapStyleOptions.loadRawResourceStyle(
                requireContext(),
                R.raw.map_style
            )
            mMap.setMapStyle(style)
        } catch (e: Resources.NotFoundException) {
            Log.e("MapsFragment", "Failed to apply map style: ${e.message}")
        }
    }

    private fun showStoriesOnMap(stories: List<Story>) {
        mMap.clear()

        for (story in stories) {
            val location = LatLng(story.lat, story.lon)
            val markerOptions = MarkerOptions()
                .position(location)
                .title(story.name)
            mMap.addMarker(markerOptions)
        }

        if (stories.isNotEmpty()) {
            val firstStory = stories[0]
            val firstStoryLocation = LatLng(firstStory.lat, firstStory.lon)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(firstStoryLocation, 12f))
        }
    }
}
