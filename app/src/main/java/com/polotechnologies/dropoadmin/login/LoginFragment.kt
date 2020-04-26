package com.polotechnologies.dropoadmin.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.polotechnologies.dropoadmin.R
import com.polotechnologies.dropoadmin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private val RC_SIGN_IN  = 100
    private lateinit var mBinding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login, container, false)

        loginIntent()
        return mBinding.root
    }

    private fun loginIntent() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser

                Toast.makeText(context?.applicationContext, "Login Successful",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_drinksFragment)
                // ...
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                // ...
                if(response == null){
                    Toast.makeText(context?.applicationContext, "Failed to Log In",Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context?.applicationContext, "Failed to Log In: ${response.error?.localizedMessage}",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}