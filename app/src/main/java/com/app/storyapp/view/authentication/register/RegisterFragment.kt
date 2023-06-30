package com.app.storyapp.view.authentication.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.app.storyapp.R
import com.app.storyapp.databinding.FragmentRegisterBinding
import com.app.storyapp.view.authentication.CustomPasswordTextInputLayout
import com.app.storyapp.viewmodel.ViewModelStory

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var viewModel: ViewModelStory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ViewModelStory::class.java]

        binding.intputPassword.setPasswordLengthListener(object : CustomPasswordTextInputLayout.PasswordLengthListener {
            override fun onPasswordLengthValid() {
                binding.editTextPassword.error = null
            }

            override fun onPasswordLengthInvalid() {
                binding.editTextPassword.error = "Password must be at least 8 characters"
            }
        })

        binding.btnRegister.setOnClickListener {
            val name = binding.intputName.text.toString()
            val email = binding.inputEmail.text.toString()
            val password = binding.intputPassword.text.toString()

            if (password.length < 8) {
                binding.intputPassword.error = "Password must be at least 8 characters"
            } else {
                binding.progressBar.visibility = View.VISIBLE
                viewModel.register().observe(viewLifecycleOwner) {
                    binding.progressBar.visibility = View.GONE
                    if (it != null) {
                        Toast.makeText(context, "Berhasil Register!", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                    } else {
                        Toast.makeText(context, "Gagal Register!", Toast.LENGTH_SHORT).show()
                    }
                }
                viewModel.registerUser(name, email, password)
            }
        }
    }
}
