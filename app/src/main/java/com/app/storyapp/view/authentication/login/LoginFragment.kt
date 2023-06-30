package com.app.storyapp.view.authentication.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.app.storyapp.R
import com.app.storyapp.databinding.FragmentLoginBinding
import com.app.storyapp.datastore.SharedPref
import com.app.storyapp.view.authentication.CustomPasswordTextInputLayout
import com.app.storyapp.viewmodel.ViewModelStory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: ViewModelStory
    private lateinit var sharedPref: SharedPref

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = SharedPref(requireContext())
        viewModel = ViewModelProvider(this)[ViewModelStory::class.java]
        // Check if token is already saved
        lifecycleScope.launch(Dispatchers.IO) {
            val token = sharedPref.getToken.first()
            if (token.isNotBlank() && token != "Undefined") {
                navigateToHomeFragment()
            }
        }

        binding.intputPassword.setPasswordLengthListener(object : CustomPasswordTextInputLayout.PasswordLengthListener {
            override fun onPasswordLengthValid() {
                binding.editTextPassword.error = null
            }

            override fun onPasswordLengthInvalid() {
                binding.editTextPassword.error = "Password must be at least 8 characters"
            }
        })

        viewModel.login().observe(viewLifecycleOwner) {
            if (it != null) {
                val userId = it.loginResult.userId
                val name = it.loginResult.name
                val token = it.loginResult.token
                saveToken(userId, name, token)
                Toast.makeText(context, "Berhasil Login!", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = View.GONE
                findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            } else {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(
                    context,
                    "Gagal Login! Periksa Email dan Password Kembali!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.inputEmail.text.toString()
            val password = binding.intputPassword.text.toString()

            if (password.length < 8) {
                binding.intputPassword.error = "Password must be at least 8 characters"

            } else {
                binding.progressBar.visibility = View.VISIBLE
                binding.editTextPassword.error = null
                viewModel.loginUser(email, password)
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun saveToken(userId: String, name: String, token: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            sharedPref.saveToken(userId, name, token)
        }
    }

    private fun navigateToHomeFragment() {
        lifecycleScope.launch(Dispatchers.Main) {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the observer to prevent potential memory leaks
        viewModel.login().removeObservers(viewLifecycleOwner)
    }
}
