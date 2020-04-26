package com.polotechnologies.dropoadmin.drinks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.polotechnologies.dropoadmin.R
import com.polotechnologies.dropoadmin.databinding.FragmentDrinksBinding

class DrinksFragment : Fragment() {

    private lateinit var mBinding: FragmentDrinksBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drinks, container, false)

        return mBinding.root
    }

}