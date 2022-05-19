package com.thor.storyapp.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.thor.storyapp.R
import com.thor.storyapp.databinding.FragmentRegisterBinding
import com.thor.storyapp.utils.viewBinding
import org.koin.android.ext.android.inject

class RegisterFragment : Fragment(R.layout.fragment_register) {
    private val binding by viewBinding(FragmentRegisterBinding::bind)
    private val viewModel: RegisterViewModel by inject()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpView()
        playAnimation()

        viewModel.stateList.observe(viewLifecycleOwner, observerStateList)
    }

    private fun setUpView() {
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
        }
        binding.myBtnRegister.setOnClickListener {
            val name = binding.myName.text.toString()
            val email = binding.myEmail.text.toString()
            val password = binding.myPassword.text.toString()
            if (nameIsValid(name) && binding.myBtnRegister.validateEmailPassword(email, password)) {
                showLoading(true)
                viewModel.register(name, email, password)
            }
        }
        binding.myPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.passwordTextInputLayout.errorPassword(s.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }


    private val observerStateList = Observer<RegisterState> {
        showLoading(it == RegisterState.OnLoading)
        when (it) {
            is RegisterState.OnSuccess -> {
                Toast.makeText(requireContext(), it.uploadResponse.message, Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
            }
            is RegisterState.OnError -> {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
            }
            RegisterState.OnLoading -> {}
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.registerProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }


    private fun nameIsValid(name: String): Boolean = (name.isNotEmpty())

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageAnimRegister, View.ROTATION, 2f, -2f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val nameTextView = ObjectAnimator.ofFloat(binding.myName, View.ALPHA, 1f).setDuration(300)
        val nameEditTextLayout =
            ObjectAnimator.ofFloat(binding.nameTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val emailTextView = ObjectAnimator.ofFloat(binding.myEmail, View.ALPHA, 1f).setDuration(300)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val passwordTextView =
            ObjectAnimator.ofFloat(binding.myPassword, View.ALPHA, 1f).setDuration(300)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordTextInputLayout, View.ALPHA, 1f).setDuration(300)
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(300)
        val textOr = ObjectAnimator.ofFloat(binding.textView2, View.ALPHA, 1f).setDuration(300)
        val register =
            ObjectAnimator.ofFloat(binding.myBtnRegister, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                nameTextView,
                nameEditTextLayout,
                emailTextView,
                emailEditTextLayout,
                passwordTextView,
                passwordEditTextLayout,
                register,
                textOr,
                login
            )
            startDelay = 200
        }.start()
    }
}