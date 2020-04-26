package com.polotechnologies.dropoadmin.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.polotechnologies.dropoadmin.R
import com.polotechnologies.dropoadmin.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var mBinding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_login, container, false)
        return mBinding.root
    }

}